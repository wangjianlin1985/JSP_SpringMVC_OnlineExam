// 
// 
// 

package exam.model;

import exam.util.json.JSONObject;
import exam.util.json.JSON;
import exam.util.json.JSONAble;
import java.io.Serializable;

public class Major implements Serializable, JSONAble
{
    private static final long serialVersionUID = 4732029763783198033L;
    private int id;
    private String name;
    
    public Major(final int id) {
        this.id = id;
    }
    
    public Major(final String name) {
        this.name = name;
    }
    
    public Major() {
    }
    
    public Major(final int id, final String name) {
        this.id = id;
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "Major [id=" + this.id + ", name=" + this.name + "]";
    }
    
    @Override
    public JSON getJSON() {
        final JSONObject json = new JSONObject();
        json.addElement("id", String.valueOf(this.id)).addElement("name", this.name);
        return json;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
