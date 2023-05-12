// 
// 
// 

package exam.dao.impl;

import java.util.List;
import java.sql.SQLException;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import exam.dao.GradeDao;
import exam.model.Grade;
import exam.dao.base.BaseDaoImpl;

@Repository("gradeDao")
public class GradeDaoImpl extends BaseDaoImpl<Grade> implements GradeDao
{
    private static RowMapper<Grade> rowMapper;
    private static String sql;
    
    static {
        GradeDaoImpl.sql = "select * from grade";
        GradeDaoImpl.rowMapper = (RowMapper<Grade>)new RowMapper<Grade>() {
            public Grade mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final Grade grade = new Grade();
                grade.setId(rs.getInt("id"));
                grade.setGrade(rs.getInt("grade"));
                return grade;
            }
        };
    }
    
    @Override
    public List<Grade> find(final Grade entity) {
        final StringBuilder sqlBuilder = new StringBuilder(GradeDaoImpl.sql).append(" where 1 = 1");
        if (entity != null) {
            if (entity.getId() > 0) {
                sqlBuilder.append(" and id = ").append(entity.getId());
            }
            if (entity.getGrade() > 0) {
                sqlBuilder.append(" and grade = ").append(entity.getGrade());
            }
        }
        return (List<Grade>)this.jdbcTemplate.query(sqlBuilder.toString(), (RowMapper)GradeDaoImpl.rowMapper);
    }
    
    @Override
    public RowMapper<Grade> getRowMapper() {
        return GradeDaoImpl.rowMapper;
    }
    
    @Override
    public String getSql() {
        return GradeDaoImpl.sql;
    }
    
    @Override
    public String getCountSql() {
        return "select count(id) from grade";
    }
}
