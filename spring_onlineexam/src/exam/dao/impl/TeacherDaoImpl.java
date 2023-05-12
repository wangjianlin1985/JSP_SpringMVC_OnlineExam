// 
// 
// 

package exam.dao.impl;

import exam.util.DataUtil;
import java.util.List;
import java.sql.SQLException;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import exam.dao.TeacherDao;
import exam.model.role.Teacher;
import exam.dao.base.BaseDaoImpl;

@Repository("teacherDao")
public class TeacherDaoImpl extends BaseDaoImpl<Teacher> implements TeacherDao
{
    private static RowMapper<Teacher> rowMapper;
    private static String sql;
    
    static {
        TeacherDaoImpl.sql = "select * from teacher";
        TeacherDaoImpl.rowMapper = (RowMapper<Teacher>)new RowMapper<Teacher>() {
            public Teacher mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final Teacher teacher = new Teacher();
                teacher.setId(rs.getString("id"));
                teacher.setName(rs.getString("name"));
                teacher.setPassword(rs.getString("password"));
                teacher.setModified(rs.getBoolean("modified"));
                return teacher;
            }
        };
    }
    
    @Override
    public List<Teacher> find(final Teacher entity) {
        final StringBuilder sqlBuilder = new StringBuilder(TeacherDaoImpl.sql).append(" where 1 = 1");
        if (entity != null) {
            if (DataUtil.isValid(entity.getId())) {
                sqlBuilder.append(" and id = '").append(entity.getId()).append("'");
            }
            if (DataUtil.isValid(entity.getName())) {
                sqlBuilder.append(" and name = '").append(entity.getName()).append("'");
            }
            if (DataUtil.isValid(entity.getPassword())) {
                sqlBuilder.append(" and password = '").append(entity.getPassword()).append("'");
            }
        }
        return (List<Teacher>)this.jdbcTemplate.query(sqlBuilder.toString(), (RowMapper)TeacherDaoImpl.rowMapper);
    }
    
    @Override
    public <T> List<T> query(final String sql, final RowMapper<T> rowMapper) {
        return (List<T>)this.jdbcTemplate.query(sql, (RowMapper)rowMapper);
    }
    
    @Override
    public RowMapper<Teacher> getRowMapper() {
        return TeacherDaoImpl.rowMapper;
    }
    
    @Override
    public String getSql() {
        return TeacherDaoImpl.sql;
    }
    
    @Override
    public String getCountSql() {
        return "select count(id) from teacher";
    }
}
