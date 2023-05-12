// 
// 
// 

package exam.dao;

import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import exam.model.role.Teacher;
import exam.dao.base.BaseDao;

public interface TeacherDao extends BaseDao<Teacher>
{
     <T> List<T> query(String p0, RowMapper<T> p1);
}
