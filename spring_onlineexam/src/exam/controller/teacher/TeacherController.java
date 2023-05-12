// 
// 
// 

package exam.controller.teacher;

import org.springframework.ui.Model;
import java.util.Iterator;
import java.util.List;
import exam.dto.ClassDTO;
import exam.util.json.JSONArray;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ResponseBody;
import exam.util.json.JSON;
import exam.util.DataUtil;
import exam.model.role.Teacher;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;
import exam.service.TeacherService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/teacher" })
public class TeacherController
{
    @Resource
    private TeacherService teacherService;
    
    @RequestMapping({ "/index" })
    public String index() {
        return "teacher/index";
    }
    
    @RequestMapping({ "/password" })
    public String password() {
        return "teacher/password";
    }
    
    @RequestMapping({ "/password/check" })
    @ResponseBody
    public void check(final String password, final HttpServletRequest request, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        final Teacher teacher = (Teacher)request.getSession().getAttribute("teacher");
        if (teacher.getPassword().equals(password)) {
            json.addElement("result", "1");
        }
        else {
            json.addElement("result", "0");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/classes" })
    @ResponseBody
    public void classes(final HttpSession session, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        final Teacher teacher = (Teacher)session.getAttribute("teacher");
        final List<ClassDTO> dtoes = this.teacherService.getClassesWithMajorAndGrade(teacher.getId());
        final JSONArray array = new JSONArray();
        for (final ClassDTO dto : dtoes) {
            array.addObject(dto.getJSON());
        }
        json.addElement("result", "1").addElement("data", array);
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/password/modify" })
    public String modifyPassword(final String oldPassword, final String newPassword, final HttpServletRequest request, final Model model) {
        final Teacher teacher = (Teacher)request.getSession().getAttribute("teacher");
        if (!this.checkPassword(oldPassword, newPassword, teacher)) {
            return "error";
        }
        this.teacherService.updatePassword(teacher.getId(), newPassword);
        teacher.setPassword(newPassword);
        teacher.setModified(true);
        model.addAttribute("message", (Object)"\u5bc6\u7801\u4fee\u6539\u6210\u529f");
        model.addAttribute("url", (Object)(String.valueOf(request.getContextPath()) + "/teacher/index"));
        return "success";
    }
    
    private boolean checkPassword(final String oldPassword, final String newPassword, final Teacher teacher) {
        return teacher.getPassword().equals(oldPassword) && DataUtil.isValid(newPassword) && newPassword.matches("^\\w{4,10}$");
    }
}
