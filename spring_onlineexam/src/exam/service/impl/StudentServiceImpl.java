// 
// 
// 

package exam.service.impl;

import java.util.List;
import exam.util.DataUtil;
import exam.util.StringUtil;
import java.math.BigInteger;
import javax.annotation.Resource;
import exam.dao.base.BaseDao;
import exam.dao.StudentDao;
import org.springframework.stereotype.Service;
import exam.service.StudentService;
import exam.model.role.Student;
import exam.service.base.BaseServiceImpl;

@Service("studentService")
public class StudentServiceImpl extends BaseServiceImpl<Student> implements StudentService
{
    private StudentDao studentDao;
    
    @Resource(name = "studentDao")
    @Override
    protected void setBaseDao(final BaseDao<Student> baseDao) {
        super.baseDao = baseDao;
        this.studentDao = (StudentDao)baseDao;
    }
    
    @Override
    public boolean isExisted(final String id) {
        final BigInteger result = (BigInteger)this.studentDao.queryForObject("select count(id) from student where id = " + id, BigInteger.class);
        return result.intValue() > 0;
    }
    
    @Override
    public void updatePassword(final String id, final String password) {
        final String sql = "update student set password = ?, modified = 1 where id = ?";
        this.studentDao.executeSql(sql, new Object[] { StringUtil.md5(password), id });
    }
    
    @Override
    public void saveStudent(final String id, final String name, final String password, final int cid) {
        this.studentDao.executeSql("insert into student values(?, ?, ?, ?, 0)", new Object[] { id, name, password, cid });
    }
    
    @Override
    public void updateStudent(final int cid, final String name, final String id) {
        final String sql = "update student set name = ?, cid = ? where id = ?";
        this.studentDao.executeSql(sql, new Object[] { name, cid, id });
    }
    
    @Override
    public void delete(final Object id) {
        final String[] sqls = { "delete from examinationresult_question where erid in (select id from examinationresult where sid = '" + id + "')", "delete from examinationresult where sid = '" + id + "'", "delete from student where id = '" + id + "'" };
        this.studentDao.batchUpdate(sqls);
    }
    
    @Override
    public Student login(final String username, final String password) {
        final String sql = String.valueOf(this.studentDao.getSql()) + " where s.name = '" + username + "' and s.password = '" + StringUtil.md5(password) + "'";
        final List<Student> result = this.studentDao.queryBySQL(sql);
        return DataUtil.isValid(result) ? result.get(0) : null;
    }
}
