// 
// 
// 

package exam.controller.student;

import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;
import exam.session.SessionContainer;
import exam.model.role.Student;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;

@Controller("exam.controller.student.LogoutController")
public class LogoutController
{
    @RequestMapping({ "/student/logout" })
    public String logout(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final Student student = (Student)session.getAttribute("student");
        SessionContainer.loginStudents.remove(student.getId());
        session.invalidate();
        return "redirect:/login";
    }
}
