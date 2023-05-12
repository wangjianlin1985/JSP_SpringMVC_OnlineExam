// 
// 
// 

package exam.dao.impl;

import java.util.List;
import org.springframework.jdbc.core.ResultSetExtractor;
import java.sql.SQLException;
import java.util.Date;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import exam.dao.ExaminationResultDao;
import exam.model.ExaminationResult;
import exam.dao.base.BaseDaoImpl;

@Repository("examinationResultDao")
public class ExaminationResultDaoImpl extends BaseDaoImpl<ExaminationResult> implements ExaminationResultDao
{
    private static String sql;
    private static String countSql;
    private static RowMapper<ExaminationResult> rowMapper;
    
    static {
        ExaminationResultDaoImpl.sql = "select * from examinationresult";
        ExaminationResultDaoImpl.countSql = "select count(id) from examinationresult";
        ExaminationResultDaoImpl.rowMapper = (RowMapper<ExaminationResult>)new RowMapper<ExaminationResult>() {
            public ExaminationResult mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final ExaminationResult er = new ExaminationResult();
                er.setId(rs.getInt("id"));
                er.setPoint(rs.getInt("point"));
                er.setExamId(rs.getInt("eid"));
                er.setExamTitle(rs.getString("examtitle"));
                er.setStudentId(rs.getString("sid"));
                er.setTime(rs.getTimestamp("time"));
                return er;
            }
        };
    }
    
    @Override
    public <T> T query(final String sql, final ResultSetExtractor<T> resultSetExtractor) {
        return (T)this.jdbcTemplate.query(sql, (ResultSetExtractor)resultSetExtractor);
    }
    
    @Override
    public <T> List<T> query(final String sql, final RowMapper<T> rowMapper) {
        return (List<T>)this.jdbcTemplate.query(sql, (RowMapper)rowMapper);
    }
    
    @Override
    public String getCountSql() {
        return ExaminationResultDaoImpl.countSql;
    }
    
    @Override
    public String getSql() {
        return ExaminationResultDaoImpl.sql;
    }
    
    @Override
    public RowMapper<ExaminationResult> getRowMapper() {
        return ExaminationResultDaoImpl.rowMapper;
    }
}
