// 
// 
// 

package exam.dto;

import exam.model.Question;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ERView
{
    private int singlePoints;
    private int multiPoints;
    private int judgePoints;
    private int point;
    private Date time;
    private List<ERViewQuestion> singleQuestions;
    private List<ERViewQuestion> multiQuestions;
    private List<ERViewQuestion> judgeQuestions;
    
    public ERView() {
        this.singleQuestions = new ArrayList<ERViewQuestion>();
        this.multiQuestions = new ArrayList<ERViewQuestion>();
        this.judgeQuestions = new ArrayList<ERViewQuestion>();
    }
    
    public List<ERViewQuestion> getsingleQuestions() {
        return Collections.unmodifiableList((List<? extends ERViewQuestion>)this.singleQuestions);
    }
    
    public void addSingleQuestion(final ERViewQuestion question) {
        this.singleQuestions.add(question);
    }
    
    public List<ERViewQuestion> getmultiQuestions() {
        return Collections.unmodifiableList((List<? extends ERViewQuestion>)this.multiQuestions);
    }
    
    public void addMultiQuestion(final ERViewQuestion question) {
        this.multiQuestions.add(question);
    }
    
    public List<ERViewQuestion> getjudgeQuestions() {
        return Collections.unmodifiableList((List<? extends ERViewQuestion>)this.judgeQuestions);
    }
    
    public void addJudgeQuestion(final ERViewQuestion question) {
        this.judgeQuestions.add(question);
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
    
    public static class ERViewQuestion extends Question
    {
        private static final long serialVersionUID = -5131397582765204363L;
        private boolean right;
        private String wrongAnswer;
        private String wrongAnswerFacade;
        
        public boolean isRight() {
            return this.right;
        }
        
        public void setRight(final boolean right) {
            this.right = right;
        }
        
        public String getWrongAnswer() {
            return this.wrongAnswer;
        }
        
        public void setWrongAnswer(final String wrongAnswer) {
            this.wrongAnswer = wrongAnswer;
            this.wrongAnswerFacade = this.generateFacade(wrongAnswer);
        }
        
        public String getWrongAnswerFacade() {
            return this.wrongAnswerFacade;
        }
    }
}
