// 
// 
// 

package exam.dao.impl;

import java.sql.SQLException;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import exam.dao.ManagerDao;
import exam.model.role.Manager;
import exam.dao.base.BaseDaoImpl;

@Repository("managerDao")
public class ManagerDaoImpl extends BaseDaoImpl<Manager> implements ManagerDao
{
    private static RowMapper<Manager> rowMapper;
    
    static {
        ManagerDaoImpl.rowMapper = (RowMapper<Manager>)new RowMapper<Manager>() {
            public Manager mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final Manager manager = new Manager();
                manager.setId(rs.getInt("id"));
                manager.setName(rs.getString("name"));
                manager.setPassword(rs.getString("password"));
                manager.setModified(rs.getBoolean("modified"));
                return manager;
            }
        };
    }
    
    @Override
    public String getCountSql() {
        return "select count(id) from manager";
    }
    
    @Override
    public String getSql() {
        return "select * from manager";
    }
    
    @Override
    public RowMapper<Manager> getRowMapper() {
        return ManagerDaoImpl.rowMapper;
    }
}
