// 
// 
// 

package exam.service.impl;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.springframework.dao.DataAccessException;
import java.sql.ResultSet;
import java.util.Date;
import exam.model.ExamStatus;
import java.sql.PreparedStatement;
import exam.model.page.PageBean;
import org.springframework.jdbc.core.CallableStatementCallback;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Connection;
import org.springframework.jdbc.core.CallableStatementCreator;
import java.math.BigInteger;
import exam.model.QuestionType;
import java.util.Calendar;
import exam.util.DataUtil;
import exam.model.Clazz;
import java.util.Iterator;
import exam.dao.base.GenerateKeyCallback;
import exam.model.Question;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;
import exam.dao.base.BaseDao;
import javax.annotation.Resource;
import exam.service.QuestionService;
import exam.dao.ExamDao;
import org.springframework.stereotype.Service;
import exam.service.ExamService;
import exam.model.Exam;
import exam.service.base.BaseServiceImpl;

@Service("examService")
public class ExamServiceImpl extends BaseServiceImpl<Exam> implements ExamService
{
    private ExamDao examDao;
    @Resource
    private QuestionService questionService;
    
    @Resource(name = "examDao")
    @Override
    protected void setBaseDao(final BaseDao<Exam> baseDao) {
        super.baseDao = baseDao;
        this.examDao = (ExamDao)baseDao;
    }
    
    @Override
    public void saveOrUpdate(final Exam entity) {
        if (entity.getId() <= 0) {
            final int examId = this.saveExam(entity);
            final List<Integer> questionIds = this.saveQuestions(entity);
            this.saveExamClassRelationships(entity.getClazzs(), examId);
            this.saveExamQuestionRelationships(questionIds, examId);
        }
        else {
            final String sql = "update exam set title = ?, timelimit = ? where id = ?";
            this.examDao.executeSql(sql, new Object[] { entity.getTitle(), entity.getLimit(), entity.getId() });
        }
    }
    
    private int saveExam(final Exam entity) {
        final String sql = "insert into exam values(null,?,?,?,?,?,?,?,?,?)";
       
        
        final int n;
        return this.examDao.getKeyHelper(sql, (ps, param) -> {
            final Exam exam = (Exam) param;
            Timestamp timestamp = null;
            ps.setString(1, exam.getTitle());
            ps.setInt(2, exam.getLimit());
            if (exam.getEndTime() != null) {
                // new(java.sql.Timestamp.class)
                new Timestamp(exam.getEndTime().getTime());
            }
            else {
                timestamp = null;
            }
            ps.setTimestamp(3, timestamp);
            ps.setString(4, exam.getStatus().name());
            ps.setInt(5, exam.getPoints());
            ps.setInt(6, exam.getSinglePoints());
            ps.setInt(7, exam.getMultiPoints());
            ps.setInt(8, exam.getJudgePoints());
            ps.setString(9, exam.getTeacher().getId());
        }, entity);
    }
    
    private List<Integer> saveQuestions(final Exam entity) {
        final List<Integer> questionIds = new ArrayList<Integer>();
        final String sql = "insert into question values(null,?,?,?,?,?,?,?,?,?)";
        final QuestionGenerateKeyCallback questionGenerateKeyCallback = new QuestionGenerateKeyCallback();
        for (final Question question : entity.getSingleQuestions()) {
            questionIds.add((question.getId() > 0) ? question.getId() : this.examDao.getKeyHelper(sql, questionGenerateKeyCallback, question));
        }
        for (final Question question : entity.getMultiQuestions()) {
            questionIds.add((question.getId() > 0) ? question.getId() : this.examDao.getKeyHelper(sql, questionGenerateKeyCallback, question));
        }
        for (final Question question : entity.getJudgeQuestions()) {
            questionIds.add((question.getId() > 0) ? question.getId() : this.examDao.getKeyHelper(sql, questionGenerateKeyCallback, question));
        }
        return questionIds;
    }
    
    private void saveExamClassRelationships(final List<Clazz> classes, final int examId) {
        final StringBuilder sb = new StringBuilder("insert into exam_class values");
        for (final Clazz clazz : classes) {
            sb.append("(null,").append(examId).append(",").append(clazz.getId()).append("),");
        }
        sb.deleteCharAt(sb.length() - 1);
        this.examDao.executeSql(sb.toString());
    }
    
    private void saveExamQuestionRelationships(final List<Integer> questionIds, final int examId) {
        final StringBuilder sb = new StringBuilder("insert into exam_question values");
        for (final Integer qid : questionIds) {
            sb.append("(null,").append(examId).append(",").append(qid).append("),");
        }
        this.examDao.executeSql(sb.deleteCharAt(sb.length() - 1).toString());
    }
    
    @Override
    public void switchStatus(final int examId, final String status, final Integer days) {
        String sql = "update exam set status = ?";
        final List<Object> params = new ArrayList<Object>(3);
        params.add(status);
        if (DataUtil.isValid(days)) {
            final Calendar calendar = Calendar.getInstance();
            calendar.add(5, days);
            sql = String.valueOf(sql) + ",endtime = ?";
            params.add(new Timestamp(calendar.getTime().getTime()));
        }
        sql = String.valueOf(sql) + " where id = ?";
        params.add(examId);
        this.examDao.executeSql(sql, params.toArray());
    }
    
    @Override
    public void delete(final Object id) {
        final int examId = (int)id;
        final String[] sqls = { "delete from exam_class where eid = " + examId, "delete from exam_question where eid = " + examId, "delete from examinationresult_question where erid in (select er.id from examinationresult er where er.eid = " + examId + ")", "delete from examinationresult where eid = " + examId, "delete from exam where id = " + examId };
        this.examDao.batchUpdate(sqls);
    }
    
    @Override
    public Exam findWithQuestions(final Exam exam) {
        final Exam result = this.getById(exam.getId());
        final List<Question> questions = this.questionService.findByExam(result.getId());
        return this.filterQuestions(result, questions);
    }
    
    private Exam filterQuestions(final Exam exam, final List<Question> questions) {
        questions.stream().forEach(question -> {
            if (question.getType() == QuestionType.SINGLE) {
                exam.addSingleQuestion(question);
            }
            else if (question.getType() == QuestionType.MULTI) {
                exam.addMultiQuestion(question);
            }
            else {
                exam.addJudgeQuestion(question);
            }
            return;
        });
        return exam;
    }
    
    @Override
    public boolean hasJoined(final int eid, final String sid) {
        final String sql = "select count(id) from examinationresult where eid = " + eid + " and sid = '" + sid + "'";
        final BigInteger count = (BigInteger)this.examDao.queryForObject(sql, BigInteger.class);
        return count.intValue() > 0;
    }
    
    @Override
    public boolean isUseful(final int eid) {
        final String sql = "select count(id) from exam where id = " + eid + " and status = 'RUNNING'";
        final BigInteger result = (BigInteger)this.examDao.queryForObject(sql, BigInteger.class);
        return result.intValue() > 0;
    }
    
    @Override
    public Exam getById(final int eid) {
        final List<Exam> list = this.examDao.execute((CallableStatementCreator)new CallableStatementCreator() {
            public CallableStatement createCallableStatement(final Connection con) throws SQLException {
                final String sql = "{call getExamById(?)}";
                final CallableStatement cs = con.prepareCall(sql);
                cs.setInt(1, eid);
                return cs;
            }
        }, (CallableStatementCallback<List<Exam>>)ExamCallableStatementCallback.instance);
        return list.get(0);
    }
    
    @Override
    public PageBean<Exam> pageSearchByStudent(final int pageCode, final int pageSize, final int pageNumber, final String sid) {
        final List<Exam> list = this.examDao.execute((CallableStatementCreator)new CallableStatementCreator() {
            public CallableStatement createCallableStatement(final Connection con) throws SQLException {
                final String sql = "{call getExamByStudent(?, ?, ?)}";
                final CallableStatement cs = con.prepareCall(sql);
                cs.setString(1, sid);
                cs.setInt(2, pageCode);
                cs.setInt(3, pageSize);
                return cs;
            }
        }, (CallableStatementCallback<List<Exam>>)ExamCallableStatementCallback.instance);
        final String sql = "select count(e.id) from exam e where e.id in (select eid from exam_class where cid = (select cid from student where id = '" + sid + "'))";
        final BigInteger count = (BigInteger)this.examDao.queryForObject(sql, BigInteger.class);
        return new PageBean<Exam>(list, pageSize, pageCode, count.intValue(), pageNumber);
    }
    
    @Override
    public PageBean<Exam> pageSearchByTeacher(final int pageCode, final int pageSize, final int pageNumber, final String tid) {
        final List<Exam> list = this.examDao.execute((CallableStatementCreator)new CallableStatementCreator() {
            public CallableStatement createCallableStatement(final Connection con) throws SQLException {
                final String sql = "{call getExamByTeacher(?, ?, ?)}";
                CallableStatement cs = null;
                try {
                    cs = con.prepareCall(sql);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                cs.setInt(1, pageCode);
                cs.setInt(2, pageSize);
                cs.setString(3, tid);
                return cs;
            }
        }, (CallableStatementCallback<List<Exam>>)ExamCallableStatementCallback.instance);
        final String sql = "select count(e.id) from exam e where e.tid = '" + tid + "'";
        final BigInteger count = (BigInteger)this.examDao.queryForObject(sql, BigInteger.class);
        return new PageBean<Exam>(list, pageSize, pageCode, count.intValue(), pageNumber);
    }
    
    private static class ExamCallableStatementCallback implements CallableStatementCallback<List<Exam>>
    {
        static ExamCallableStatementCallback instance;
        
        static {
            ExamCallableStatementCallback.instance = new ExamCallableStatementCallback();
        }
        
        public List<Exam> doInCallableStatement(final CallableStatement cs) throws SQLException, DataAccessException {
            final List<Exam> list = new ArrayList<Exam>();
            final ResultSet rs = cs.executeQuery();
            Exam exam = null;
            while (rs.next()) {
                exam = new Exam();
                exam.setId(rs.getInt("id"));
                exam.setTitle(rs.getString("title"));
                exam.setStatus(ExamStatus.valueOf(rs.getString("status")));
                exam.setEndTime(rs.getTimestamp("endtime"));
                exam.setJudgePoints(rs.getInt("judgepoints"));
                exam.setLimit(rs.getInt("timelimit"));
                exam.setMultiPoints(rs.getInt("multipoints"));
                exam.setPoints(rs.getInt("points"));
                exam.setSinglePoints(rs.getInt("singlepoints"));
                list.add(exam);
            }
            return list;
        }
    }
    
    private static class QuestionGenerateKeyCallback implements GenerateKeyCallback
    {
        @Override
        public void setParameters(final PreparedStatement ps, final Object param) throws SQLException {
            final Question question = (Question)param;
            ps.setString(1, question.getTitle());
            ps.setString(2, question.getOptionA());
            ps.setString(3, question.getOptionB());
            ps.setString(4, question.getOptionC());
            ps.setString(5, question.getOptionD());
            ps.setInt(6, question.getPoint());
            ps.setString(7, question.getType().name());
            ps.setString(8, question.getAnswer());
            ps.setString(9, question.getTeacher().getId());
        }
    }
}
