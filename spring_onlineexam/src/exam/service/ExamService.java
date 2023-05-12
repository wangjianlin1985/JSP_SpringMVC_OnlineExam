// 
// 
// 

package exam.service;

import exam.model.page.PageBean;
import exam.model.Exam;
import exam.service.base.BaseService;

public interface ExamService extends BaseService<Exam>
{
    void switchStatus(int p0, String p1, Integer p2);
    
    Exam findWithQuestions(Exam p0);
    
    boolean hasJoined(int p0, String p1);
    
    boolean isUseful(int p0);
    
    Exam getById(int p0);
    
    PageBean<Exam> pageSearchByTeacher(int p0, int p1, int p2, String p3);
    
    PageBean<Exam> pageSearchByStudent(int p0, int p1, int p2, String p3);
}
