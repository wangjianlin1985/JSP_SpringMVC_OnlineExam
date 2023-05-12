// 
// 
// 

package exam.service.impl;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import exam.dto.StudentReport;
import java.util.ArrayList;
import exam.util.DataUtil;
import exam.dto.StatisticsData;
import java.util.List;
import exam.model.QuestionType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.DataAccessException;
import java.util.Date;
import java.sql.ResultSet;
import org.springframework.jdbc.core.ResultSetExtractor;
import exam.dto.ERView;
import java.util.Iterator;
import exam.dto.MarkedQuestion;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import exam.dao.base.GenerateKeyCallback;
import javax.annotation.Resource;
import exam.dao.base.BaseDao;
import exam.dao.ExaminationResultDao;
import org.springframework.stereotype.Service;
import exam.service.ExaminationResultService;
import exam.model.ExaminationResult;
import exam.service.base.BaseServiceImpl;

@Service("examinationResultService")
public class ExaminationResultServiceImpl extends BaseServiceImpl<ExaminationResult> implements ExaminationResultService
{
    private ExaminationResultDao examinationResultDao;
    
    @Resource(name = "examinationResultDao")
    @Override
    protected void setBaseDao(final BaseDao<ExaminationResult> baseDao) {
        super.baseDao = baseDao;
        this.examinationResultDao = (ExaminationResultDao)baseDao;
    }
    
    @Override
    public void saveOrUpdate(final ExaminationResult entity) {
        final String sql = "insert into examinationresult values(null, ?, ?, ?, ?, ?)";
        final int erId = this.examinationResultDao.getKeyHelper(sql, new GenerateKeyCallback() {
            @Override
            public void setParameters(final PreparedStatement ps, final Object param) throws SQLException {
                final ExaminationResult er = (ExaminationResult)param;
                ps.setInt(1, er.getExamId());
                ps.setString(2, er.getStudentId());
                ps.setInt(3, er.getPoint());
                ps.setTimestamp(4, new Timestamp(er.getTime().getTime()));
                ps.setString(5, er.getExamTitle());
            }
        }, entity);
        final StringBuilder sb = new StringBuilder("insert into examinationresult_question values");
        for (final MarkedQuestion mq : entity.getMarkedQuestions()) {
            sb.append("(null,").append(erId).append(",").append(mq.getQuestionId()).append(",").append(mq.isRight()).append(",'").append(mq.getWrongAnswer()).append("'),");
        }
        this.examinationResultDao.executeSql(sb.deleteCharAt(sb.length() - 1).toString());
    }
    
    @Override
    public ERView getViewById(final int id) {
        String sql = "select e.singlepoints, e.multipoints, e.judgepoints, er.point, er.time from examinationresult er join exam e on e.id = er.eid where er.id = " + id;
        final ERView view = this.examinationResultDao.query(sql, (org.springframework.jdbc.core.ResultSetExtractor<ERView>)new ResultSetExtractor<ERView>() {
            public ERView extractData(final ResultSet rs) throws SQLException, DataAccessException {
                final ERView view = new ERView();
                if (rs.next()) {
                    view.setJudgePoints(rs.getInt("judgepoints"));
                    view.setSinglePoints(rs.getInt("singlepoints"));
                    view.setMultiPoints(rs.getInt("multipoints"));
                    view.setPoint(rs.getInt("point"));
                    view.setTime(rs.getTimestamp("time"));
                }
                return view;
            }
        });
        sql = "select q.*, erq.isright, erq.wronganswer from examinationresult_question erq join question q on q.id = erq.qid where erq.erid = " + id;
        final List<ERView.ERViewQuestion> questions = this.examinationResultDao.query(sql, (org.springframework.jdbc.core.RowMapper<ERView.ERViewQuestion>)new RowMapper<ERView.ERViewQuestion>() {
            public ERView.ERViewQuestion mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final ERView.ERViewQuestion question = new ERView.ERViewQuestion();
                question.setType(QuestionType.valueOf(rs.getString("type")));
                question.setAnswer(rs.getString("answer"));
                question.setId(rs.getInt("id"));
                question.setOptionA(rs.getString("optionA"));
                question.setOptionB(rs.getString("optionB"));
                question.setOptionC(rs.getString("optionC"));
                question.setOptionD(rs.getString("optionD"));
                question.setPoint(rs.getInt("point"));
                question.setTitle(rs.getString("title"));
                question.setRight(rs.getBoolean("isright"));
                question.setWrongAnswer(rs.getString("wronganswer"));
                return question;
            }
        });
        return this.filterQuestions(questions, view);
    }
    
    @Override
    public StatisticsData getStatisticsData(final int eid) {
        final String sql = "select er.sid, er.point, er.examtitle, s.name, e.points from examinationresult er join student s on s.id = er.sid join exam e on e.id = er.eid where er.eid = " + eid;
        final List<Helper> helpers = this.examinationResultDao.query(sql, (org.springframework.jdbc.core.RowMapper<Helper>)new RowMapper<Helper>() {
            public Helper mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                return Helper.Builder().name(rs.getString("name")).point(rs.getInt("point")).sid(rs.getString("sid")).title(rs.getString("examtitle")).total(rs.getInt("points"));
            }
        });
        return DataUtil.isValid(helpers) ? this.prepareData(helpers) : null;
    }
    
    private StatisticsData prepareData(final List<Helper> helpers) {
        final StatisticsData data = new StatisticsData();
        final Helper first = helpers.get(0);
        final int totalPoint = first.total;
        int max = first.point;
        int min = first.point;
        int point = 0;
        final int sp = (int)(totalPoint * 0.6);
        final int ep = (int)(totalPoint * 0.8);
        final int np = (int)(totalPoint * 0.9);
        final List<String> maxNames = new ArrayList<String>();
        List<String> minNames = new ArrayList<String>();
        data.setPersonCount(helpers.size());
        data.setTitle(first.examTitle);
        data.setExamPoints(totalPoint);
        data.setSixtyPoint(sp);
        data.setEighttyPoint(ep);
        data.setNinetyPoint(np);
        for (final Helper helper : helpers) {
            point = helper.point;
            if (point > max) {
                max = point;
            }
            else if (point < min) {
                min = point;
            }
            if (point < sp) {
                data.addUnderSixty(point);
            }
            else if (point < ep) {
                data.addSixtyAndEighty(point);
            }
            else if (point < np) {
                data.addEightyAndNinety(point);
            }
            else {
                data.addAboveNinety(point);
            }
        }
        for (final Helper helper : helpers) {
            if (helper.point == max) {
                maxNames.add(helper.name);
            }
            else {
                if (helper.point != min) {
                    continue;
                }
                minNames.add(helper.name);
            }
        }
        if (max == min) {
            minNames = maxNames;
        }
        data.setHighestPoint(max);
        data.setLowestPoint(min);
        data.addHightestName(maxNames);
        data.addLowestNames(minNames);
        return data;
    }
    
    private ERView filterQuestions(final List<ERView.ERViewQuestion> questions, final ERView view) {
        questions.stream().forEach(question -> {
            if (question.getType() == QuestionType.SINGLE) {
                view.addSingleQuestion(question);
            }
            else if (question.getType() == QuestionType.MULTI) {
                view.addMultiQuestion(question);
            }
            else {
                view.addJudgeQuestion(question);
            }
            return;
        });
        return view;
    }
    
    @Override
    public List<StudentReport> getReportData(final int eid) {
        final String sql = "select er.examtitle, er.sid, er.point, s.name from examinationresult er join student s on s.id = er.sid where er.eid = " + eid;
        return this.examinationResultDao.query(sql, (org.springframework.jdbc.core.RowMapper<StudentReport>)new RowMapper<StudentReport>() {
            public StudentReport mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final StudentReport sr = new StudentReport();
                sr.setName(rs.getString("name"));
                sr.setPoint(rs.getInt("point"));
                sr.setSid(rs.getString("sid"));
                sr.setTitle(rs.getString("examtitle"));
                return sr;
            }
        });
    }
    
    private static class Helper
    {
        String sid;
        int point;
        int total;
        String examTitle;
        String name;
        
        public static Helper Builder() {
            return new Helper();
        }
        
        public Helper sid(final String sid) {
            this.sid = sid;
            return this;
        }
        
        public Helper point(final int point) {
            this.point = point;
            return this;
        }
        
        public Helper name(final String name) {
            this.name = name;
            return this;
        }
        
        public Helper title(final String title) {
            this.examTitle = title;
            return this;
        }
        
        public Helper total(final int total) {
            this.total = total;
            return this;
        }
    }
}
