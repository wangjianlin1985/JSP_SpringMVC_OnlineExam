// 
// 
// 

package exam.intercepter;

import javax.servlet.http.HttpSession;
import exam.model.role.Teacher;
import exam.model.role.Student;
import exam.model.role.Manager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginIntercepter extends HandlerInterceptorAdapter
{
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final String path = request.getServletPath();
        final String contextPath = request.getContextPath();
        final HttpSession session = request.getSession();
        if (path.indexOf("admin") != -1) {
            final Manager manager = (Manager)session.getAttribute("admin");
            if (manager == null) {
                response.sendRedirect(String.valueOf(contextPath) + "/login");
                return false;
            }
            if (session.getAttribute("force") != null) {
                response.sendRedirect(String.valueOf(contextPath) + "/valid");
                session.invalidate();
                return false;
            }
            return true;
        }
        else {
            if (path.indexOf("teacher") == -1) {
                if (path.indexOf("student") != -1) {
                    final Student student = (Student)session.getAttribute("student");
                    if (student == null) {
                        response.sendRedirect(String.valueOf(contextPath) + "/login");
                        return false;
                    }
                    if (session.getAttribute("force") != null) {
                        response.sendRedirect(String.valueOf(contextPath) + "/valid");
                        session.invalidate();
                        return false;
                    }
                }
                return true;
            }
            final Teacher teacher = (Teacher)session.getAttribute("teacher");
            if (teacher == null) {
                response.sendRedirect(String.valueOf(contextPath) + "/login");
                return false;
            }
            if (session.getAttribute("force") != null) {
                response.sendRedirect(String.valueOf(contextPath) + "/valid");
                session.invalidate();
                return false;
            }
            return true;
        }
    }
}
