// 
// 
// 

package exam.controller.admin;

import org.springframework.web.bind.annotation.PathVariable;
import java.util.Iterator;
import exam.model.Clazz;
import exam.util.json.JSONArray;
import org.springframework.web.bind.annotation.ResponseBody;
import exam.util.json.JSON;
import exam.util.StringUtil;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import exam.model.role.Teacher;
import exam.model.page.PageBean;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import exam.util.DataUtil;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;
import exam.service.ClazzService;
import javax.annotation.Resource;
import exam.service.TeacherService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/admin/teacher" })
public class AdminTeacherController
{
    @Resource
    private TeacherService teacherService;
    @Resource
    private ClazzService clazzService;
    @Value("#{properties['teacher.pageSize']}")
    private int pageSize;
    @Value("#{properties['teacher.pageNumber']}")
    private int pageNumber;
    
    @RequestMapping({ "/list" })
    public String list(final String pn, final String id, final String name, final Model model) {
        final int pageCode = DataUtil.getPageCode(pn);
        String where = " where 1 = 1 ";
        final List<Object> params = new ArrayList<Object>(1);
        if (DataUtil.isValid(id)) {
            where = String.valueOf(where) + "and id = ?";
            params.add(id);
        }
        else if (DataUtil.isValid(name)) {
            where = String.valueOf(where) + "and name like '%" + name + "%'";
        }
        final PageBean<Teacher> pageBean = this.teacherService.pageSearch(pageCode, this.pageSize, this.pageNumber, where, params, null);
        model.addAttribute("pageBean", (Object)pageBean);
        model.addAttribute("name", (Object)name);
        return "admin/teacher_list";
    }
    
    @RequestMapping({ "/add" })
    @ResponseBody
    public void add(final String id, String name, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        name = StringUtil.htmlEncode(name);
        if (!DataUtil.isValid(id, name)) {
            json.addElement("result", "0").addElement("message", "\u683c\u5f0f\u975e\u6cd5");
        }
        else if (this.teacherService.isExist(id)) {
            json.addElement("result", "0").addElement("message", "\u6b64\u6559\u5e08\u5df2\u5b58\u5728");
        }
        else {
            this.teacherService.saveTeacher(id, name, StringUtil.md5("1234"));
            json.addElement("result", "1").addElement("message", "\u4fdd\u5b58\u6210\u529f");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/edit" })
    @ResponseBody
    public void edit(final String id, String name, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        name = StringUtil.htmlEncode(name);
        if (!DataUtil.isValid(id, name)) {
            json.addElement("result", "0").addElement("message", "\u683c\u5f0f\u975e\u6cd5");
        }
        else {
            this.teacherService.updateName(id, name);
            json.addElement("result", "1").addElement("message", "\u4fdd\u5b58\u6210\u529f");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/clazz/list" })
    @ResponseBody
    public void clazz(final String tid, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (!DataUtil.isValid(tid)) {
            json.addElement("result", "0").addElement("message", "\u6570\u636e\u683c\u5f0f\u975e\u6cd5");
        }
        else {
            final List<Clazz> clazzs = this.clazzService.findByTeacher(tid);
            final JSONArray array = new JSONArray();
            for (final Clazz c : clazzs) {
                array.addObject(c.getJSON());
            }
            json.addElement("result", "1").addElement("data", array);
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/clazz/save" })
    @ResponseBody
    public void clazzSave(final String ids, final String tid, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (!DataUtil.isValid(ids, tid)) {
            json.addElement("result", "0").addElement("message", "\u683c\u5f0f\u975e\u6cd5");
        }
        else {
            this.teacherService.updateTeachClazzs(ids, tid);
            json.addElement("result", "1").addElement("message", "\u4fee\u6539\u6210\u529f");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/delete/{tid}" })
    public void delete(@PathVariable final String tid, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        this.teacherService.delete(tid);
        json.addElement("result", "1").addElement("message", "\u5220\u9664\u6210\u529f");
        DataUtil.writeJSON(json, response);
    }
}
