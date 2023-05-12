// 
// 
// 

package exam.controller;

import org.springframework.web.bind.annotation.ResponseBody;
import exam.util.json.JSON;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import exam.model.role.Student;
import exam.model.role.Teacher;
import exam.model.role.Manager;
import javax.servlet.http.HttpSession;
import exam.session.SessionContainer;
import exam.util.StringUtil;
import exam.util.DataUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import exam.service.StudentService;
import exam.service.TeacherService;
import javax.annotation.Resource;
import exam.service.ManagerService;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController
{
    @Resource
    private ManagerService managerService;
    @Resource
    private TeacherService teacherService;
    @Resource
    private StudentService studentService;
    
    @RequestMapping({ "/login" })
    public String login() {
        return "login";
    }
    
    @RequestMapping({ "/login/do" })
    public String doLogin(final String username, final String password, final String verify, final int role, final Model model, final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        if (!DataUtil.isValid(username, password) || !DataUtil.checkVerify(verify, session)) {
            return "error";
        }
        if (role == 3) {
            final Manager manager = this.managerService.login(username, StringUtil.md5(password));
            if (manager == null) {
                model.addAttribute("error", (Object)"\u7528\u6237\u540d\u6216\u5bc6\u7801\u9519\u8bef");
                return "login";
            }
            manager.setPassword(password);
            if (SessionContainer.adminSession != null) {
                SessionContainer.adminSession.setAttribute("force", (Object)Boolean.TRUE);
            }
            (SessionContainer.adminSession = session).setAttribute("admin", (Object)manager);
            return "redirect:/admin/index";
        }
        else if (role == 2) {
            final Teacher teacher = this.teacherService.login(username, password);
            if (teacher == null) {
                model.addAttribute("error", (Object)"\u7528\u6237\u540d\u6216\u5bc6\u7801\u9519\u8bef");
                return "login";
            }
            teacher.setPassword(password);
            if (SessionContainer.loginTeachers.containsKey(teacher.getId())) {
                SessionContainer.loginTeachers.get(teacher.getId()).setAttribute("force", (Object)Boolean.TRUE);
            }
            SessionContainer.loginTeachers.put(teacher.getId(), session);
            session.setAttribute("teacher", (Object)teacher);
            return "redirect:/teacher/index";
        }
        else {
            if (role != 1) {
                return "";
            }
            final Student student = this.studentService.login(username, password);
            if (student == null) {
                model.addAttribute("error", (Object)"\u7528\u6237\u540d\u6216\u5bc6\u7801\u9519\u8bef");
                return "login";
            }
            student.setPassword(password);
            if (SessionContainer.loginStudents.containsKey(student.getId())) {
                SessionContainer.loginStudents.get(student.getId()).setAttribute("force", (Object)Boolean.TRUE);
            }
            SessionContainer.loginStudents.put(student.getId(), session);
            session.setAttribute("student", (Object)student);
            return "redirect:/student/index";
        }
    }
    
    @RequestMapping({ "/login/verify" })
    @ResponseBody
    public void rand(final String verify, final HttpServletResponse response, final HttpSession session) {
        final JSON json = new JSONObject();
        if (DataUtil.checkVerify(verify, session)) {
            json.addElement("result", "1");
        }
        else {
            json.addElement("result", "0");
        }
        DataUtil.writeJSON(json, response);
    }
}
