// 
// 
// 

package exam.model.role;

import java.io.Serializable;

public class Teacher implements Serializable
{
    private static final long serialVersionUID = -1615504534445176240L;
    private String id;
    private String name;
    private String password;
    private boolean modified;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public boolean isModified() {
        return this.modified;
    }
    
    public void setModified(final boolean modified) {
        this.modified = modified;
    }
}
