// 
// 
// 

package exam.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExaminationAnswer
{
    private int examId;
    private Map<Integer, String> answers;
    
    public ExaminationAnswer() {
        this.answers = new HashMap<Integer, String>();
    }
    
    public void addQuestion(final int id, final String answer) {
        this.answers.put(id, answer);
    }
    
    public Map<Integer, String> getAnswers() {
        return Collections.unmodifiableMap((Map<? extends Integer, ? extends String>)this.answers);
    }
    
    public int getExamId() {
        return this.examId;
    }
    
    public void setExamId(final int examId) {
        this.examId = examId;
    }
}
