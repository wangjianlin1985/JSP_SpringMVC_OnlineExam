// 
// 
// 

package exam.service;

import exam.model.role.Student;
import exam.service.base.BaseService;

public interface StudentService extends BaseService<Student>
{
    boolean isExisted(String p0);
    
    void updatePassword(String p0, String p1);
    
    Student login(String p0, String p1);
    
    void updateStudent(int p0, String p1, String p2);
    
    void saveStudent(String p0, String p1, String p2, int p3);
}
