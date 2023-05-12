// 
// 
// 

package exam.dto;

public class StudentReport
{
    private String title;
    private String sid;
    private String name;
    private int point;
    
    public String getSid() {
        return this.sid;
    }
    
    public void setSid(final String sid) {
        this.sid = sid;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getPoint() {
        return this.point;
    }
    
    public void setPoint(final int point) {
        this.point = point;
    }
}
