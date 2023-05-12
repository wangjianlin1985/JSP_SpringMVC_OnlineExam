// 
// 
// 

package exam.model;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import exam.util.DataUtil;
import exam.util.json.JSONObject;
import exam.util.json.JSON;
import exam.model.role.Teacher;
import exam.util.json.JSONAble;
import java.io.Serializable;

public class Question implements Serializable, JSONAble
{
    private static final long serialVersionUID = 3817117285809180416L;
    private static String[] answerFacades;
    private static String[] judgeAnserFacades;
    private int id;
    private String title;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;
    private String answerFacade;
    private QuestionType type;
    private int point;
    private Teacher teacher;
    
    static {
        Question.answerFacades = new String[] { "A", "B", "C", "D" };
        Question.judgeAnserFacades = new String[] { "\u5bf9", "\u9519" };
    }
    
    public String getAnswerFacade() {
        return this.answerFacade;
    }
    
    @Override
    public JSON getJSON() {
        final JSONObject json = new JSONObject();
        json.addElement("id", String.valueOf(this.id)).addElement("title", this.title).addElement("optionA", this.optionA).addElement("optionB", this.optionB).addElement("optionC", this.optionC).addElement("optionD", this.optionD).addElement("answer", this.answer).addElement("point", String.valueOf(this.point));
        return json;
    }
    
    @Override
    public String toString() {
        return "Question [id=" + this.id + ", title=" + this.title + ", optionA=" + this.optionA + ", optionB=" + this.optionB + ", optionC=" + this.optionC + ", optionD=" + this.optionD + ", answer=" + this.answer + ", type=" + this.type + ", point=" + this.point + "]";
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
    
    public String getOptionA() {
        return this.optionA;
    }
    
    public void setOptionA(final String optionA) {
        this.optionA = optionA;
    }
    
    public String getOptionB() {
        return this.optionB;
    }
    
    public void setOptionB(final String optionB) {
        this.optionB = optionB;
    }
    
    public String getOptionC() {
        return this.optionC;
    }
    
    public void setOptionC(final String optionC) {
        this.optionC = optionC;
    }
    
    public String getOptionD() {
        return this.optionD;
    }
    
    public void setOptionD(final String optionD) {
        this.optionD = optionD;
    }
    
    public String getAnswer() {
        return this.answer;
    }
    
    protected String generateFacade(final String answer) {
        String facade = "";
        if (DataUtil.isValid(answer)) {
            if (this.type == QuestionType.SINGLE) {
                facade = Question.answerFacades[Integer.parseInt(answer)];
            }
            else if (this.type == QuestionType.MULTI) {
                final String[] answers = answer.split(",");
                final StringBuilder sb = new StringBuilder();
                String[] array;
                for (int length = (array = answers).length, i = 0; i < length; ++i) {
                    final String a = array[i];
                    sb.append(Question.answerFacades[Integer.parseInt(a)]).append(",");
                }
                facade = sb.deleteCharAt(sb.length() - 1).toString();
            }
            else {
                facade = Question.judgeAnserFacades[Integer.parseInt(answer)];
            }
        }
        return facade;
    }
    
    public void setAnswer(final String answer) {
        this.answer = answer;
        this.answerFacade = this.generateFacade(answer);
    }
    
    public QuestionType getType() {
        return this.type;
    }
    
    public void setType(final QuestionType type) {
        this.type = type;
    }
    
    public int getPoint() {
        return this.point;
    }
    
    public void setPoint(final int point) {
        this.point = point;
    }
    
    public Teacher getTeacher() {
        return this.teacher;
    }
    
    public void setTeacher(final Teacher teacher) {
        this.teacher = teacher;
    }
}
