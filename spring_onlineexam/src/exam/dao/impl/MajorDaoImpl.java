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
import exam.dao.MajorDao;
import exam.model.Major;
import exam.dao.base.BaseDaoImpl;

@Repository("majorDao")
public class MajorDaoImpl extends BaseDaoImpl<Major> implements MajorDao
{
    private static RowMapper<Major> rowMapper;
    private static String sql;
    
    static {
        MajorDaoImpl.sql = "select * from major";
        MajorDaoImpl.rowMapper = (RowMapper<Major>)new RowMapper<Major>() {
            public Major mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final Major major = new Major();
                major.setId(rs.getInt("id"));
                major.setName(rs.getString("name"));
                return major;
            }
        };
    }
    
    @Override
    public List<Major> find(final Major enity) {
        final StringBuilder sqlBuilder = new StringBuilder(MajorDaoImpl.sql).append(" where 1 = 1");
        if (enity != null) {
            if (enity.getId() > 0) {
                sqlBuilder.append(" and id = ").append(enity.getId());
            }
            if (DataUtil.isValid(enity.getName())) {
                sqlBuilder.append(" and name = '").append(enity.getName()).append("'");
            }
        }
        return (List<Major>)this.jdbcTemplate.query(sqlBuilder.toString(), (RowMapper)MajorDaoImpl.rowMapper);
    }
    
    @Override
    public RowMapper<Major> getRowMapper() {
        return MajorDaoImpl.rowMapper;
    }
    
    @Override
    public String getSql() {
        return MajorDaoImpl.sql;
    }
    
    @Override
    public String getCountSql() {
        return "select count(id) from major";
    }
}
