// 
// 
// 

package exam.dao;

import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import exam.model.ExaminationResult;
import exam.dao.base.BaseDao;

public interface ExaminationResultDao extends BaseDao<ExaminationResult>
{
     <T> T query(String p0, ResultSetExtractor<T> p1);
    
     <T> List<T> query(String p0, RowMapper<T> p1);
}
