// 
// 
// 

package exam.service.impl;

import java.math.BigInteger;
import exam.model.QuestionType;
import java.util.List;
import javax.annotation.Resource;
import exam.dao.base.BaseDao;
import exam.dao.QuestionDao;
import org.springframework.stereotype.Service;
import exam.service.QuestionService;
import exam.model.Question;
import exam.service.base.BaseServiceImpl;

@Service("questionService")
public class QuestionServiceImpl extends BaseServiceImpl<Question> implements QuestionService
{
    private QuestionDao questionDao;
    
    @Resource(name = "questionDao")
    @Override
    protected void setBaseDao(final BaseDao<Question> baseDao) {
        super.baseDao = baseDao;
        this.questionDao = (QuestionDao)baseDao;
    }
    
    @Override
    public List<Question> getSingles(final String tid) {
        return this.getQuestionsByType(tid, QuestionType.SINGLE);
    }
    
    @Override
    public List<Question> getMultis(final String tid) {
        return this.getQuestionsByType(tid, QuestionType.MULTI);
    }
    
    @Override
    public List<Question> getJudges(final String tid) {
        return this.getQuestionsByType(tid, QuestionType.JUDGE);
    }
    
    @Override
    public void saveOrUpdate(final Question question) {
        if (question.getId() > 0) {
            final String sql = "update question set title = ?, optionA = ?, optionB = ?, optionC = ?, optionD = ?, answer = ?, point = ? where id = ?";
            this.questionDao.executeSql(sql, new Object[] { question.getTitle(), question.getOptionA(), question.getOptionB(), question.getOptionC(), question.getOptionD(), question.getAnswer(), question.getPoint(), question.getId() });
        }
        else {
            final String sql = "insert into question values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            this.questionDao.executeSql(sql, new Object[] { question.getTitle(), question.getOptionA(), question.getOptionB(), question.getOptionC(), question.getOptionD(), question.getPoint(), question.getType().name(), question.getAnswer(), question.getTeacher().getId() });
        }
    }
    
    @Override
    public boolean isUsedByExam(final int id) {
        final String sql = "select count(id) from exam_question where qid = " + id;
        final BigInteger count = (BigInteger)this.questionDao.queryForObject(sql, BigInteger.class);
        return count.intValue() > 0;
    }
    
    @Override
    public void delete(final Object id) {
        final String sql = "delete from question where id = " + id;
        this.questionDao.executeSql(sql);
    }
    
    @Override
    public List<Question> findByExam(final int eid) {
        final String sql = "select * from question where id in (select qid from exam_question where eid = " + eid + ")";
        return this.questionDao.queryBySQL(sql);
    }
    
    private List<Question> getQuestionsByType(final String tid, final QuestionType type) {
        final String sql = String.valueOf(this.questionDao.getSql()) + " where tid = '" + tid + "' and type = '" + type.name() + "'";
        return this.questionDao.queryBySQL(sql);
    }
    
    @Override
    public Double articulationScore(final int qid) {
        final String sql = "select sum(case when isright = 1 then 1 else 0 end) / count(id) from examinationresult_question where qid = " + qid;
        final Double result = (Double)this.questionDao.queryForObject(sql, Double.class);
        return result;
    }
}
