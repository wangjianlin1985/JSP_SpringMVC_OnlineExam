// 
// 
// 

package exam.controller.student;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import exam.util.json.JSON;
import exam.util.DataUtil;
import exam.model.role.Student;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;
import exam.service.StudentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller("exam.controller.student.StudentController")
@RequestMapping({ "/student" })
public class StudentController
{
    @Resource
    private StudentService studentService;
    
    @RequestMapping({ "/index" })
    public String index() {
        return "student/index";
    }
    
    @RequestMapping({ "/password" })
    public String password() {
        return "student/password";
    }
    
    @RequestMapping({ "/password/check" })
    @ResponseBody
    public void check(final String password, final HttpServletRequest request, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        final Student student = (Student)request.getSession().getAttribute("student");
        if (student.getPassword().equals(password)) {
            json.addElement("result", "1");
        }
        else {
            json.addElement("result", "0");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/password/modify" })
    public String modifyPassword(final String oldPassword, final String newPassword, final HttpServletRequest request, final Model model) {
        final Student student = (Student)request.getSession().getAttribute("student");
        if (!this.checkPassword(oldPassword, newPassword, student)) {
            return "error";
        }
        this.studentService.updatePassword(student.getId(), newPassword);
        student.setPassword(newPassword);
        student.setModified(true);
        model.addAttribute("message", (Object)"\u5bc6\u7801\u4fee\u6539\u6210\u529f");
        model.addAttribute("url", (Object)(String.valueOf(request.getContextPath()) + "/student/index"));
        return "success";
    }
    
    private boolean checkPassword(final String oldPassword, final String newPassword, final Student student) {
        return student.getPassword().equals(oldPassword) && DataUtil.isValid(newPassword) && newPassword.matches("^\\w{4,10}$");
    }
}
