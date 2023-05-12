// 
// 
// 

package exam.controller.admin;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import exam.util.json.JSON;
import exam.util.StringUtil;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import exam.model.role.Student;
import exam.model.page.PageBean;
import java.util.HashMap;
import java.util.List;
import exam.util.DataUtil;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.Resource;
import exam.service.StudentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/admin/student" })
public class AdminStudentController
{
    @Resource
    private StudentService studentService;
    @Value("#{properties['student.pageSize']}")
    private int pageSize;
    @Value("#{properties['student.pageNumber']}")
    private int pageNumber;
    
    @RequestMapping({ "/list" })
    public String list(final String pn, final String search, final Model model) {
        final int pageCode = DataUtil.getPageCode(pn);
        String where = null;
        if (DataUtil.isValid(search)) {
            where = " where s.name like '%" + search + "%'";
        }
        final PageBean<Student> pageBean = this.studentService.pageSearch(pageCode, this.pageSize, this.pageNumber, where, null, null);
        model.addAttribute("pageBean", (Object)pageBean);
        model.addAttribute("search", (Object)search);
        return "admin/student_list";
    }
    
    @RequestMapping({ "/add" })
    @ResponseBody
    public void add(final String id, final String clazz, String name, final HttpServletResponse response) {
        final JSON json = new JSONObject();
        name = StringUtil.htmlEncode(name);
        if (!DataUtil.isNumber(id, clazz)) {
            json.addElement("result", "0").addElement("message", "\u6570\u636e\u683c\u5f0f\u975e\u6cd5");
        }
        else if (!DataUtil.isValid(name)) {
            json.addElement("result", "0").addElement("message", "\u8bf7\u8f93\u5165\u5b66\u751f\u59d3\u540d");
        }
        else {
            this.studentService.saveStudent(id, name, StringUtil.md5("1234"), Integer.parseInt(clazz));
            json.addElement("result", "1").addElement("message", "\u5b66\u751f\u6dfb\u52a0\u6210\u529f");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/edit" })
    @ResponseBody
    public void edit(final String id, String name, final String clazz, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        name = StringUtil.htmlEncode(name);
        if (!DataUtil.isNumber(clazz) || !DataUtil.isValid(id, name)) {
            json.addElement("result", "0").addElement("message", "\u683c\u5f0f\u975e\u6cd5");
        }
        else {
            this.studentService.updateStudent(Integer.parseInt(clazz), name, id);
            json.addElement("result", "1").addElement("message", "\u4fee\u6539\u6210\u529f");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/delete/{sid}" })
    @ResponseBody
    public void delete(@PathVariable final String sid, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        this.studentService.delete(sid);
        json.addElement("result", "1").addElement("message", "\u5220\u9664\u6210\u529f");
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/check" })
    @ResponseBody
    public void check(final String id, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (!DataUtil.isNumber(id)) {
            json.addElement("result", "0").addElement("message", "\u683c\u5f0f\u975e\u6cd5");
        }
        else if (this.studentService.isExisted(id)) {
            json.addElement("result", "1").addElement("exist", "1");
        }
        else {
            json.addElement("result", "1").addElement("exist", "0");
        }
        DataUtil.writeJSON(json, response);
    }
}
