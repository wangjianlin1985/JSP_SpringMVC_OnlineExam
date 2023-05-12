// 
// 
// 

package exam.model;

import exam.util.json.JSONObject;
import exam.util.json.JSON;
import exam.util.json.JSONAble;
import java.io.Serializable;

public class Grade implements Serializable, JSONAble
{
    private static final long serialVersionUID = -3270807327813730922L;
    private int id;
    private int grade;
    
    public Grade(final int id) {
        this.id = id;
    }
    
    public Grade() {
    }
    
    public Grade(final int id, final int grade) {
        this.id = id;
        this.grade = grade;
    }
    
    @Override
    public JSON getJSON() {
        final JSONObject object = new JSONObject();
        object.addElement("id", String.valueOf(this.id)).addElement("grade", String.valueOf(this.grade));
        return object;
    }
    
    @Override
    public String toString() {
        return "Grade [id=" + this.id + ", grade=" + this.grade + "]";
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
}
