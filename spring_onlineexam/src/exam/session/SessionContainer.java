// 
// 
// 

package exam.session;

import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentMap;

public final class SessionContainer
{
    public static final ConcurrentMap<String, HttpSession> loginStudents;
    public static final ConcurrentMap<String, HttpSession> loginTeachers;
    public static volatile HttpSession adminSession;
    
    static {
        loginStudents = new ConcurrentHashMap<String, HttpSession>();
        loginTeachers = new ConcurrentHashMap<String, HttpSession>();
    }
}
