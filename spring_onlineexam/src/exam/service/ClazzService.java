// 
// 
// 

package exam.service;

import java.util.List;
import exam.model.Clazz;
import exam.service.base.BaseService;

public interface ClazzService extends BaseService<Clazz>
{
    List<Clazz> findByMajor(int p0);
    
    List<Clazz> findByGrade(int p0);
    
    List<Clazz> findByCNO(int p0);
    
    List<Clazz> findClazzOnly(Clazz p0);
    
    List<Clazz> findByTeacher(String p0);
    
    boolean isExist(int p0, int p1, int p2);
    
    List<Clazz> findByExam(Integer p0);
    
    void resetExamClass(int p0, String p1);
}
