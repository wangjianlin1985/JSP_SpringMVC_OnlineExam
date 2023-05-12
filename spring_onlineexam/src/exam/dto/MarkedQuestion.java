// 
// 
// 

package exam.dto;

public class MarkedQuestion
{
    private int id;
    private int examinationResultId;
    private int questionId;
    private boolean right;
    private String wrongAnswer;
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public int getExaminationResultId() {
        return this.examinationResultId;
    }
    
    public void setExaminationResultId(final int examinationResultId) {
        this.examinationResultId = examinationResultId;
    }
    
    public int getQuestionId() {
        return this.questionId;
    }
    
    public void setQuestionId(final int questionId) {
        this.questionId = questionId;
    }
    
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
    }
}
