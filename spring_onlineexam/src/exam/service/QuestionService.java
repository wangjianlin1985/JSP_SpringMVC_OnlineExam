// 
// 
// 

package exam.service;

import java.util.List;
import exam.model.Question;
import exam.service.base.BaseService;

public interface QuestionService extends BaseService<Question>
{
    List<Question> getSingles(String p0);
    
    List<Question> getMultis(String p0);
    
    List<Question> getJudges(String p0);
    
    boolean isUsedByExam(int p0);
    
    List<Question> findByExam(int p0);
    
    Double articulationScore(int p0);
}
