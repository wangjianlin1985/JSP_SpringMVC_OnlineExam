// 
// 
// 

package exam.model;

import java.util.Collections;
import java.util.ArrayList;
import exam.dto.MarkedQuestion;
import java.util.List;
import java.util.Date;

public class ExaminationResult
{
    private int id;
    private int examId;
    private String examTitle;
    private String studentId;
    private int point;
    private Date time;
    private List<MarkedQuestion> markedQuestions;
    
    public ExaminationResult() {
        this.markedQuestions = new ArrayList<MarkedQuestion>();
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public int getExamId() {
        return this.examId;
    }
    
    public void setExamId(final int examId) {
        this.examId = examId;
    }
    
    public String getStudentId() {
        return this.studentId;
    }
    
    public void setStudentId(final String studentId) {
        this.studentId = studentId;
    }
    
    public String getExamTitle() {
        return this.examTitle;
    }
    
    public void setExamTitle(final String examTitle) {
        this.examTitle = examTitle;
    }
    
    public int getPoint() {
        return this.point;
    }
    
    public void setPoint(final int point) {
        this.point = point;
    }
    
    public Date getTime() {
        return this.time;
    }
    
    public void setTime(final Date time) {
        this.time = time;
    }
    
    public List<MarkedQuestion> getMarkedQuestions() {
        return Collections.unmodifiableList((List<? extends MarkedQuestion>)this.markedQuestions);
    }
    
    public void addMarkedQuestion(final MarkedQuestion question) {
        this.markedQuestions.add(question);
    }
}
