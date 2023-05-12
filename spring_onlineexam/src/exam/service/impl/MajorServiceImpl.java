// 
// 
// 

package exam.service.impl;

import java.util.List;
import exam.util.DataUtil;
import javax.annotation.Resource;
import exam.dao.base.BaseDao;
import exam.dao.MajorDao;
import org.springframework.stereotype.Service;
import exam.service.MajorService;
import exam.model.Major;
import exam.service.base.BaseServiceImpl;

@Service("majorService")
public class MajorServiceImpl extends BaseServiceImpl<Major> implements MajorService
{
    private MajorDao majorDao;
    
    @Resource(name = "majorDao")
    @Override
    protected void setBaseDao(final BaseDao<Major> baseDao) {
        super.baseDao = baseDao;
        this.majorDao = (MajorDao)baseDao;
    }
    
    @Override
    public Major findByName(final String name) {
        final Major major = new Major();
        major.setName(name);
        final List<Major> majors = this.majorDao.find(major);
        return DataUtil.isValid(majors) ? majors.get(0) : null;
    }
    
    @Override
    public List<Major> findAll() {
        return this.majorDao.find(null);
    }
    
    @Override
    public List<Major> findByGrade(final int grade) {
        final String sql = "select * from major where id in (select mid from class where gid = " + grade + ")";
        return this.majorDao.queryBySQL(sql);
    }
    
    @Override
    public void saveOrUpdate(final Major entity) {
        if (entity.getId() <= 0) {
            this.majorDao.executeSql("insert into major values(null, ?)", new Object[] { entity.getName() });
        }
        else {
            this.majorDao.executeSql("update major set name = ? where id = ?", new Object[] { entity.getName(), entity.getId() });
        }
    }
    
    @Override
    public void delete(final Object id) {
        final String[] sqls = { "delete from examinationresult_question where erid in (select id from examinationresult where eid in (select eid from exam_class where cid in (select id from class where mid = " + id + ")))", "delete from examinationresult where eid in (select eid from exam_class where cid in (select cid from class where mid = " + id + "))", "delete from student where cid in (select id from class where mid = " + id + ")", "delete from teacher_class where cid in (select id from class where mid = " + id + ")", "delete from class where mid = " + id, "delete from major where id = " + id };
        this.majorDao.batchUpdate(sqls);
    }
}
