// 
// 
// 

package exam.service.impl;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import exam.model.Grade;
import exam.model.Major;
import java.util.List;
import javax.annotation.Resource;
import exam.dao.base.BaseDao;
import exam.dao.ClazzDao;
import org.springframework.stereotype.Service;
import exam.service.ClazzService;
import exam.model.Clazz;
import exam.service.base.BaseServiceImpl;

@Service("clazzService")
public class ClazzServiceImpl extends BaseServiceImpl<Clazz> implements ClazzService
{
    private ClazzDao clazzDao;
    
    @Resource(name = "clazzDao")
    @Override
    protected void setBaseDao(final BaseDao<Clazz> baseDao) {
        super.baseDao = baseDao;
        this.clazzDao = (ClazzDao)baseDao;
    }
    
    @Override
    public void delete(final Object id) {
        final String[] sqls = { "delete from examinationresult_question where erid in (select id from examinationresult where eid in (select eid from exam_class where cid = " + id + "))", "delete from examinationresult where eid in (select eid from exam_class where cid = " + id + ")", "delete from student where cid = " + id, "delete from teacher_class where cid = " + id, "delete from class where id = " + id };
        this.clazzDao.batchUpdate(sqls);
    }
    
    @Override
    public void saveOrUpdate(final Clazz entity) {
        if (entity.getId() <= 0) {
            final String sql = "insert into class values(null, ?, ?, ?)";
            this.clazzDao.executeSql(sql, new Object[] { entity.getCno(), entity.getGrade().getId(), entity.getMajor().getId() });
        }
    }
    
    @Override
    public List<Clazz> findByMajor(final int majorId) {
        final Clazz clazz = new Clazz();
        clazz.setMajor(new Major(majorId));
        return this.clazzDao.find(clazz);
    }
    
    @Override
    public List<Clazz> findByGrade(final int grade) {
        final Clazz clazz = new Clazz();
        clazz.setGrade(new Grade(grade));
        return this.clazzDao.find(clazz);
    }
    
    @Override
    public List<Clazz> findByCNO(final int cno) {
        final Clazz clazz = new Clazz();
        clazz.setCno(cno);
        return this.clazzDao.find(clazz);
    }
    
    @Override
    public List<Clazz> findAll() {
        return this.clazzDao.find(null);
    }
    
    @Override
    public List<Clazz> findByExam(final Integer examId) {
        final String sql = String.valueOf(this.clazzDao.getSql()) + " where c.id in (select cid from exam_class where eid = " + examId + ")";
        return this.clazzDao.queryBySQL(sql);
    }
    
    @Override
    public List<Clazz> findClazzOnly(final Clazz clazz) {
        return this.clazzDao.findClazzOnly(clazz);
    }
    
    @Override
    public List<Clazz> findByTeacher(final String tid) {
        final StringBuilder sqlBuilder = new StringBuilder(this.clazzDao.getSql());
        sqlBuilder.append(" where c.id in (select cid from teacher_class where tid = '").append(tid).append("')");
        return this.clazzDao.queryBySQL(sqlBuilder.toString());
    }
    
    @Override
    public boolean isExist(final int grade, final int major, final int cno) {
        final StringBuilder sqlBuilder = new StringBuilder("select count(id) from class where gid = ");
        sqlBuilder.append(grade).append(" and mid = ").append(major).append(" and cno = ").append(cno);
        final BigInteger result = (BigInteger)this.clazzDao.queryForObject(sqlBuilder.toString(), BigInteger.class);
        return result.intValue() > 0;
    }
    
    @Override
    public void resetExamClass(final int examId, final String clazzIds) {
        final String sql = "delete from exam_class where eid = " + examId;
        this.clazzDao.executeSql(sql);
        final StringBuilder sb = new StringBuilder("insert into exam_class values");
        String[] split;
        for (int length = (split = clazzIds.split(",")).length, i = 0; i < length; ++i) {
            final String id = split[i];
            sb.append("(null,").append(examId).append(",").append(id).append("),");
        }
        this.clazzDao.executeSql(sb.deleteCharAt(sb.length() - 1).toString());
    }
}
