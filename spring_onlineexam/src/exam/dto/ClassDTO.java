// 
// 
// 

package exam.dto;

import exam.util.json.JSONObject;
import exam.util.json.JSON;
import exam.util.json.JSONAble;

public class ClassDTO implements JSONAble
{
    private int gid;
    private int grade;
    private int mid;
    private String major;
    private int cid;
    private int cno;
    
    @Override
    public JSON getJSON() {
        final JSONObject json = new JSONObject();
        json.addElement("gid", String.valueOf(this.gid)).addElement("grade", String.valueOf(this.grade)).addElement("mid", String.valueOf(this.mid)).addElement("major", this.major).addElement("cid", String.valueOf(this.cid)).addElement("cno", String.valueOf(this.cno));
        return json;
    }
    
    public int getGid() {
        return this.gid;
    }
    
    public void setGid(final int gid) {
        this.gid = gid;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
    
    public int getMid() {
        return this.mid;
    }
    
    public void setMid(final int mid) {
        this.mid = mid;
    }
    
    public String getMajor() {
        return this.major;
    }
    
    public void setMajor(final String major) {
        this.major = major;
    }
    
    public int getCid() {
        return this.cid;
    }
    
    public void setCid(final int cid) {
        this.cid = cid;
    }
    
    public int getCno() {
        return this.cno;
    }
    
    public void setCno(final int cno) {
        this.cno = cno;
    }
}
