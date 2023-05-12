// 
// 
// 

package exam.dao.impl;

import java.util.List;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import java.sql.SQLException;
import java.util.Date;
import exam.model.ExamStatus;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import exam.dao.ExamDao;
import exam.model.Exam;
import exam.dao.base.BaseDaoImpl;

@Repository("examDao")
public class ExamDaoImpl extends BaseDaoImpl<Exam> implements ExamDao
{
    private static String sql;
    private static String countSql;
    private static RowMapper<Exam> rowMapper;
    
    static {
        ExamDaoImpl.sql = "select * from exam";
        ExamDaoImpl.countSql = "select count(id) from exam";
        ExamDaoImpl.rowMapper = (RowMapper<Exam>)new RowMapper<Exam>() {
            public Exam mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final Exam exam = new Exam();
                exam.setId(rs.getInt("id"));
                exam.setTitle(rs.getString("title"));
                exam.setStatus(ExamStatus.valueOf(rs.getString("status")));
                exam.setEndTime(rs.getTimestamp("endtime"));
                exam.setJudgePoints(rs.getInt("judgepoints"));
                exam.setLimit(rs.getInt("timelimit"));
                exam.setMultiPoints(rs.getInt("multipoints"));
                exam.setPoints(rs.getInt("points"));
                exam.setSinglePoints(rs.getInt("singlepoints"));
                return exam;
            }
        };
    }
    
    @Override
    public List<Exam> execute(final CallableStatementCreator creator, final CallableStatementCallback<List<Exam>> callback) {
        return (List<Exam>)this.jdbcTemplate.execute(creator, (CallableStatementCallback)callback);
    }
    
    @Override
    public String getCountSql() {
        return ExamDaoImpl.countSql;
    }
    
    @Override
    public String getSql() {
        return ExamDaoImpl.sql;
    }
    
    @Override
    public RowMapper<Exam> getRowMapper() {
        return ExamDaoImpl.rowMapper;
    }
}
