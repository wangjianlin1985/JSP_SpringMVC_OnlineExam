// 
// 
// 

package exam.service;

import exam.dto.ClassDTO;
import java.util.List;
import exam.model.role.Teacher;
import exam.service.base.BaseService;

public interface TeacherService extends BaseService<Teacher>
{
    void updateName(String p0, String p1);
    
    void updatePassword(String p0, String p1);
    
    boolean isExist(String p0);
    
    void updateTeachClazzs(String p0, String p1);
    
    Teacher login(String p0, String p1);
    
    void saveTeacher(String p0, String p1, String p2);
    
    List<ClassDTO> getClassesWithMajorAndGrade(String p0);
}
