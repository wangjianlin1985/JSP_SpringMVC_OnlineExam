// 
// 
// 

package exam.model.role;

import exam.model.Clazz;
import java.io.Serializable;

public class Student implements Serializable
{
    private static final long serialVersionUID = -4447237686888091087L;
    private String id;
    private String name;
    private String password;
    private Clazz clazz;
    private boolean modified;
    
    public String getId() {
        return this.id;
    }
    
    public Student setId(final String id) {
        this.id = id;
        return this;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Student setName(final String name) {
        this.name = name;
        return this;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public Student setPassword(final String password) {
        this.password = password;
        return this;
    }
    
    public Clazz getClazz() {
        return this.clazz;
    }
    
    public Student setClazz(final Clazz clazz) {
        this.clazz = clazz;
        return this;
    }
    
    public boolean isModified() {
        return this.modified;
    }
    
    public void setModified(final boolean modified) {
        this.modified = modified;
    }
}
