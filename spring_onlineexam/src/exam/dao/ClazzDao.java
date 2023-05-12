// 
// 
// 

package exam.dao;

import java.util.List;
import exam.model.Clazz;
import exam.dao.base.BaseDao;

public interface ClazzDao extends BaseDao<Clazz>
{
    List<Clazz> findClazzOnly(Clazz p0);
}
