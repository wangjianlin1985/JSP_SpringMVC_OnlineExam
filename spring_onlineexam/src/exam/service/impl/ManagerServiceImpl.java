// 
// 
// 

package exam.service.impl;

import exam.util.StringUtil;
import java.util.List;
import exam.util.DataUtil;
import exam.model.role.Manager;
import javax.annotation.Resource;
import exam.dao.ManagerDao;
import org.springframework.stereotype.Service;
import exam.service.ManagerService;

@Service("managerService")
public class ManagerServiceImpl implements ManagerService
{
    @Resource
    private ManagerDao managerDao;
    
    @Override
    public Manager login(final String name, final String password) {
        final String sql = "select * from manager where name = ? and password = ?";
        final List<Manager> managers = this.managerDao.queryBySQL(sql, name, password);
        return DataUtil.isValid(managers) ? managers.get(0) : null;
    }
    
    @Override
    public void updatePassword(final int id, final String password) {
        final String sql = "update manager set password = ?, modified = 1 where id = ?";
        this.managerDao.executeSql(sql, new Object[] { StringUtil.md5(password), id });
    }
}
