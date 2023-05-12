// 
// 
// 

package exam.dao.impl;

import exam.util.DataUtil;
import java.util.List;
import java.sql.SQLException;
import exam.model.Grade;
import exam.model.Major;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import exam.dao.ClazzDao;
import exam.model.Clazz;
import exam.dao.base.BaseDaoImpl;

@Repository("clazzDao")
public class ClazzDaoImpl extends BaseDaoImpl<Clazz> implements ClazzDao
{
    private static RowMapper<Clazz> rowMapper;
    private static RowMapper<Clazz> clazzOnlyRowMapper;
    private static String sql;
    
    static {
        ClazzDaoImpl.sql = "select c.id as c_id, c.cno as c_cno, g.id as g_id, g.grade as g_grade,m.id as m_id, m.name as m_name from class c join grade g on c.gid = g.id join major m on m.id = c.mid";
        ClazzDaoImpl.rowMapper = (RowMapper<Clazz>)new RowMapper<Clazz>() {
            public Clazz mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final Clazz clazz = new Clazz();
                clazz.setId(rs.getInt("c_id"));
                clazz.setCno(rs.getInt("c_cno"));
                final Major major = new Major();
                major.setId(rs.getInt("m_id"));
                major.setName(rs.getString("m_name"));
                final Grade grade = new Grade();
                grade.setId(rs.getInt("g_id"));
                grade.setGrade(rs.getInt("g_grade"));
                clazz.setMajor(major);
                clazz.setGrade(grade);
                return clazz;
            }
        };
        ClazzDaoImpl.clazzOnlyRowMapper = (RowMapper<Clazz>)new RowMapper<Clazz>() {
            public Clazz mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final Clazz clazz = new Clazz();
                clazz.setId(rs.getInt("id"));
                clazz.setCno(rs.getInt("cno"));
                return clazz;
            }
        };
    }
    
    @Override
    public List<Clazz> find(final Clazz entity) {
        return this.find(ClazzDaoImpl.sql, ClazzDaoImpl.rowMapper, entity, "c");
    }
    
    @Override
    public List<Clazz> findClazzOnly(final Clazz clazz) {
        return this.find("select * from class", ClazzDaoImpl.clazzOnlyRowMapper, clazz, null);
    }
    
    private List<Clazz> find(final String querySql, final RowMapper<Clazz> rowMapper, final Clazz clazz, final String alias) {
        final StringBuilder sqlBuilder = new StringBuilder(querySql).append(" where 1 = 1");
        final String _alias = DataUtil.isValid(alias) ? (String.valueOf(alias) + ".") : "";
        if (clazz != null) {
            if (clazz.getId() > 0) {
                sqlBuilder.append(" and ").append(_alias).append("id = ").append(clazz.getId());
            }
            if (clazz.getCno() > 0) {
                sqlBuilder.append(" and ").append(_alias).append("cno = ").append(clazz.getCno());
            }
            if (clazz.getGrade() != null) {
                sqlBuilder.append(" and ").append(_alias).append("gid = ").append(clazz.getGrade().getId());
            }
            if (clazz.getMajor() != null) {
                sqlBuilder.append(" and ").append(_alias).append("mid = ").append(clazz.getMajor().getId());
            }
        }
        return (List<Clazz>)this.jdbcTemplate.query(sqlBuilder.toString(), (RowMapper)rowMapper);
    }
    
    @Override
    public RowMapper<Clazz> getRowMapper() {
        return ClazzDaoImpl.rowMapper;
    }
    
    @Override
    public String getSql() {
        return ClazzDaoImpl.sql;
    }
    
    @Override
    public String getCountSql() {
        return "select count(id) from class";
    }
}
