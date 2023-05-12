// 
// 
// 

package exam.service.impl;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;
import exam.dto.ClassDTO;
import java.util.List;
import exam.util.DataUtil;
import java.math.BigInteger;
import exam.util.StringUtil;
import javax.annotation.Resource;
import exam.dao.base.BaseDao;
import exam.dao.TeacherDao;
import org.springframework.stereotype.Service;
import exam.service.TeacherService;
import exam.model.role.Teacher;
import exam.service.base.BaseServiceImpl;

@Service("teacherService")
public class TeacherServiceImpl extends BaseServiceImpl<Teacher> implements TeacherService
{
    private TeacherDao teacherDao;
    
    @Resource(name = "teacherDao")
    @Override
    protected void setBaseDao(final BaseDao<Teacher> baseDao) {
        super.baseDao = baseDao;
        this.teacherDao = (TeacherDao)baseDao;
    }
    
    @Override
    public void updateName(final String id, final String name) {
        final String sql = "update teacher set name = ? where id = ?";
        this.teacherDao.executeSql(sql, new Object[] { name, id });
    }
    
    @Override
    public void updatePassword(final String id, final String password) {
        final String sql = "update teacher set password = ?, modified = 1 where id = ?";
        this.teacherDao.executeSql(sql, new Object[] { StringUtil.md5(password), id });
    }
    
    @Override
    public boolean isExist(final String id) {
        final String sql = "select count(id) from teacher where id = '" + id + "'";
        final BigInteger result = (BigInteger)this.teacherDao.queryForObject(sql, BigInteger.class);
        return result.intValue() > 0;
    }
    
    @Override
    public void updateTeachClazzs(final String ids, final String tid) {
        final String sql = "delete from teacher_class where tid = '" + tid + "'";
        this.teacherDao.executeSql(sql);
        final StringBuilder sqlBuilder = new StringBuilder("insert into teacher_class values");
        final String[] notes = ids.split(",");
        String[] array;
        for (int length = (array = notes).length, i = 0; i < length; ++i) {
            final String note = array[i];
            sqlBuilder.append("(null, '").append(tid).append("', '").append(note).append("'),");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        this.teacherDao.executeSql(sqlBuilder.toString());
    }
    
    @Override
    public Teacher login(final String name, final String password) {
        final String sql = "select * from teacher where name = ? and password = ?";
        final List<Teacher> result = this.teacherDao.queryBySQL(sql, name, StringUtil.md5(password));
        return DataUtil.isValid(result) ? result.get(0) : null;
    }
    
    @Override
    public void saveTeacher(final String id, final String name, final String password) {
        this.teacherDao.executeSql("insert into teacher values(?, ?, ?, 0)", new Object[] { id, name, password });
    }
    
    @Override
    public void delete(final Object id) {
        final String[] sqls = { "delete from examinationresult_question where qid in (select id from question where tid = '" + id + "')", "delete from examinationresult where eid in (select id from exam where tid = '" + id + "')", "delete from exam where tid = '" + id + "'", "delete from question where tid = '" + id + "'", "delete from teacher_class where tid = '" + id + "'", "delete from teacher where id = '" + id + "'" };
        this.teacherDao.batchUpdate(sqls);
    }
    
    @Override
    public List<ClassDTO> getClassesWithMajorAndGrade(final String tid) {
        final String sql = "select g.id as gid, g.grade, m.id as mid, m.name as major, c.id as cid, c.cno from class c join grade g on c.gid = g.id join major m on c.mid = m.id where c.id in (select cid from teacher_class where tid = '" + tid + "')";
        return this.teacherDao.query(sql, (org.springframework.jdbc.core.RowMapper<ClassDTO>)new RowMapper<ClassDTO>() {
            public ClassDTO mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final ClassDTO dto = new ClassDTO();
                dto.setCid(rs.getInt("cid"));
                dto.setCno(rs.getInt("cno"));
                dto.setGid(rs.getInt("gid"));
                dto.setGrade(rs.getInt("grade"));
                dto.setMajor(rs.getString("major"));
                dto.setMid(rs.getInt("mid"));
                return dto;
            }
        });
    }
}
