// 
// 
// 

package exam.model;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import exam.model.role.Teacher;
import java.util.Date;
import java.io.Serializable;

public class Exam implements Serializable
{
    private static final long serialVersionUID = -5053744055036244916L;
    private int id;
    private String title;
    private int limit;
    private Date endTime;
    private ExamStatus status;
    private int points;
    private int singlePoints;
    private int multiPoints;
    private int judgePoints;
    private Teacher teacher;
    private List<Question> singleQuestions;
    private List<Question> multiQuestions;
    private List<Question> judgeQuestions;
    private List<Clazz> clazzs;
    
    public Exam() {
        this.singleQuestions = new ArrayList<Question>();
        this.multiQuestions = new ArrayList<Question>();
        this.judgeQuestions = new ArrayList<Question>();
        this.clazzs = new ArrayList<Clazz>();
    }
    
    @Override
    public String toString() {
        return "Exam [id=" + this.id + ", title=" + this.title + ", limit=" + this.limit + ", endTime=" + this.endTime + ", status=" + this.status + ", points=" + this.points + ", singlePoints=" + this.singlePoints + ", multiPoints=" + this.multiPoints + ", judgePoints=" + this.judgePoints + ", singleQuestions=" + this.singleQuestions + ", multiQuestions=" + this.multiQuestions + ", judgeQuestions=" + this.judgeQuestions + ", clazzs=" + this.clazzs + "]";
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public int getLimit() {
        return this.limit;
    }
    
    public void setLimit(final int limit) {
        this.limit = limit;
    }
    
    public Date getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }
    
    public int getPoints() {
        return this.points;
    }
    
    public void setPoints(final int points) {
        this.points = points;
    }
    
    public int getSinglePoints() {
        return this.singlePoints;
    }
    
    public void setSinglePoints(final int singlePoints) {
        this.singlePoints = singlePoints;
    }
    
    public int getMultiPoints() {
        return this.multiPoints;
    }
    
    public void setMultiPoints(final int multiPoints) {
        this.multiPoints = multiPoints;
    }
    
    public int getJudgePoints() {
        return this.judgePoints;
    }
    
    public void setJudgePoints(final int judgePoints) {
        this.judgePoints = judgePoints;
    }
    
    public Teacher getTeacher() {
        return this.teacher;
    }
    
    public void setTeacher(final Teacher teacher) {
        this.teacher = teacher;
    }
    
    public ExamStatus getStatus() {
        return this.status;
    }
    
    public void setStatus(final ExamStatus status) {
        this.status = status;
    }
    
    public void addSingleQuestion(final Question question) {
        if (question == null || question.getType() != QuestionType.SINGLE) {
            return;
        }
        this.singleQuestions.add(question);
    }
    
    public List<Question> getSingleQuestions() {
        return Collections.unmodifiableList((List<? extends Question>)this.singleQuestions);
    }
    
    public void addMultiQuestion(final Question question) {
        if (question == null || question.getType() != QuestionType.MULTI) {
            return;
        }
        this.multiQuestions.add(question);
    }
    
    public List<Question> getMultiQuestions() {
        return Collections.unmodifiableList((List<? extends Question>)this.multiQuestions);
    }
    
    public void addJudgeQuestion(final Question question) {
        if (question == null || question.getType() != QuestionType.JUDGE) {
            return;
        }
        this.judgeQuestions.add(question);
    }
    
    public List<Question> getJudgeQuestions() {
        return Collections.unmodifiableList((List<? extends Question>)this.judgeQuestions);
    }
    
    public void addClazz(final Clazz clazz) {
        this.clazzs.add(clazz);
    }
    
    public List<Clazz> getClazzs() {
        return Collections.unmodifiableList((List<? extends Clazz>)this.clazzs);
    }
}
