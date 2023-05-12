// 
// 
// 

package exam.dao.impl;

import java.sql.SQLException;
import exam.model.QuestionType;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import exam.dao.QuestionDao;
import exam.model.Question;
import exam.dao.base.BaseDaoImpl;

@Repository("questionDao")
public class QuestionDaoImpl extends BaseDaoImpl<Question> implements QuestionDao
{
    private static String sql;
    private static String countSql;
    private static RowMapper<Question> rowMapper;
    
    static {
        QuestionDaoImpl.sql = "select * from question";
        QuestionDaoImpl.countSql = "select count(id) from question";
        QuestionDaoImpl.rowMapper = (RowMapper<Question>)new RowMapper<Question>() {
            public Question mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final Question question = new Question();
                question.setId(rs.getInt("id"));
                question.setOptionA(rs.getString("optionA"));
                question.setOptionB(rs.getString("optionB"));
                question.setOptionC(rs.getString("optionC"));
                question.setOptionD(rs.getString("optionD"));
                question.setPoint(rs.getInt("point"));
                question.setTitle(rs.getString("title"));
                question.setType(QuestionType.valueOf(rs.getString("type")));
                question.setAnswer(rs.getString("answer"));
                return question;
            }
        };
    }
    
    @Override
    public String getCountSql() {
        return QuestionDaoImpl.countSql;
    }
    
    @Override
    public String getSql() {
        return QuestionDaoImpl.sql;
    }
    
    @Override
    public RowMapper<Question> getRowMapper() {
        return QuestionDaoImpl.rowMapper;
    }
}
