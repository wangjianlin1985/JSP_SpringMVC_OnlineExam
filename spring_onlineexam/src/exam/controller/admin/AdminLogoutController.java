// 
// 
// 

package exam.controller.admin;

import exam.session.SessionContainer;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/admin" })
public class AdminLogoutController
{
    @RequestMapping({ "/logout" })
    public String logout(final HttpSession session) {
        SessionContainer.adminSession = null;
        session.invalidate();
        return "login";
    }
}
