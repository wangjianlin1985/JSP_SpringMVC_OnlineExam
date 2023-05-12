// 
// 
// 

package exam.dao;

import java.util.List;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import exam.model.Exam;
import exam.dao.base.BaseDao;

public interface ExamDao extends BaseDao<Exam>
{
    List<Exam> execute(CallableStatementCreator p0, CallableStatementCallback<List<Exam>> p1);
}
