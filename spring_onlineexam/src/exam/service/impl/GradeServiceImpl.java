// 
// 
// 

package exam.service.impl;

import java.util.List;
import exam.util.DataUtil;
import javax.annotation.Resource;
import exam.dao.base.BaseDao;
import exam.dao.GradeDao;
import org.springframework.stereotype.Service;
import exam.service.GradeService;
import exam.model.Grade;
import exam.service.base.BaseServiceImpl;

@Service("gradeService")
public class GradeServiceImpl extends BaseServiceImpl<Grade> implements GradeService
{
    private GradeDao gradeDao;
    
    @Resource(name = "gradeDao")
    @Override
    protected void setBaseDao(final BaseDao<Grade> baseDao) {
        super.baseDao = baseDao;
        this.gradeDao = (GradeDao)baseDao;
    }
    
    @Override
    public Grade findByGrade(final String grade) {
        final Grade _grade = new Grade();
        _grade.setGrade(Integer.parseInt(grade));
        final List<Grade> grades = this.gradeDao.find(_grade);
        return DataUtil.isValid(grades) ? grades.get(0) : null;
    }
    
    @Override
    public List<Grade> findAll() {
        return this.gradeDao.find(null);
    }
    
    @Override
    public void saveOrUpdate(final Grade entity) {
        if (entity.getId() <= 0) {
            this.gradeDao.executeSql("insert into grade values(null, " + entity.getGrade() + ")");
        }
    }
    
    @Override
    public void delete(final Object id) {
        final String[] sqls = { "delete from examinationresult_question where erid in (select er.id from examinationresult er where er.eid in (select ec.eid from exam_class ec where ec.cid in(select c.id from class c where c.gid = " + id + ")))", "delete from examinationresult where eid in (select ec.eid from exam_class ec where ec.cid in(select c.id from class c where c.gid = " + id + "))", "delete from student where cid in (select id from class where gid = " + id + ")", "delete from exam_class where cid in (select id from class where gid = " + id + ")", "delete from teacher_class where cid in (select id from class where gid = " + id + ")", "delete from class where gid = " + id, "delete from grade where id = " + id };
        this.gradeDao.batchUpdate(sqls);
    }
}
