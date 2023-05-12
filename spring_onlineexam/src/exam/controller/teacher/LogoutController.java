// 
// 
// 

package exam.controller.teacher;

import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;
import exam.session.SessionContainer;
import exam.model.role.Teacher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;

@Controller("exam.controller.teacher")
public class LogoutController
{
    @RequestMapping({ "/teacher/logout" })
    public String logout(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final Teacher teacher = (Teacher)session.getAttribute("teacher");
        SessionContainer.loginTeachers.remove(teacher.getId());
        session.invalidate();
        return "redirect:/login";
    }
}
