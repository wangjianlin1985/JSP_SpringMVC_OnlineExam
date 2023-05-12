// 
// 
// 

package exam.model;

import exam.util.json.JSONObject;
import exam.util.json.JSON;
import exam.util.json.JSONAble;
import java.io.Serializable;

public class Clazz implements Serializable, JSONAble
{
    private static final long serialVersionUID = 3193377452706700152L;
    private int id;
    private int cno;
    private Major major;
    private Grade grade;
    
    public Clazz(final int id) {
        this.id = id;
    }
    
    public Clazz() {
    }
    
    @Override
    public String toString() {
        return "Clazz [id=" + this.id + ", cno=" + this.cno + ", major=" + this.major + ", grade=" + this.grade + "]";
    }
    
    @Override
    public JSON getJSON() {
        final JSONObject json = new JSONObject();
        json.addElement("id", String.valueOf(this.id)).addElement("cno", String.valueOf(this.cno));
        if (this.major != null && this.grade != null) {
            json.addElement("major", this.major.getJSON()).addElement("grade", this.grade.getJSON());
        }
        return json;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public int getCno() {
        return this.cno;
    }
    
    public void setCno(final int cno) {
        this.cno = cno;
    }
    
    public Major getMajor() {
        return this.major;
    }
    
    public void setMajor(final Major major) {
        this.major = major;
    }
    
    public Grade getGrade() {
        return this.grade;
    }
    
    public void setGrade(final Grade grade) {
        this.grade = grade;
    }
}
