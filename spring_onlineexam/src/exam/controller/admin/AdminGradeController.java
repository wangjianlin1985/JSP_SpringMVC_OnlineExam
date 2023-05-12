// 
// 
// 

package exam.controller.admin;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import exam.util.json.JSON;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import exam.model.Grade;
import exam.model.page.PageBean;
import java.util.HashMap;
import java.util.List;
import exam.util.DataUtil;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.Resource;
import exam.service.GradeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/admin/grade" })
public class AdminGradeController
{
    @Resource
    private GradeService gradeService;
    @Value("#{properties['grade.pageSize']}")
    private int pageSize;
    @Value("#{properties['grade.pageNumber']}")
    private int pageNumber;
    
    @RequestMapping({ "/list" })
    public String list(final String pn, final String search, final Model model) {
        final int pageCode = DataUtil.getPageCode(pn);
        String where = null;
        if (DataUtil.isNumber(search)) {
            where = " where grade = " + search;
        }
        final PageBean<Grade> pageBean = this.gradeService.pageSearch(pageCode, this.pageSize, this.pageNumber, where, null, null);
        model.addAttribute("pageBean", (Object)pageBean);
        model.addAttribute("search", (Object)search);
        return "admin/grade_list";
    }
    
    @RequestMapping({ "/add" })
    @ResponseBody
    public void add(final String grade, final HttpServletResponse response) {
        final JSON json = new JSONObject();
        if (!DataUtil.isNumber(grade)) {
            json.addElement("result", "0").addElement("message", "\u8bf7\u8f93\u5165\u6570\u5b57\uff0c\u6bd4\u59822012");
        }
        else if (this.gradeService.findByGrade(grade) != null) {
            json.addElement("result", "0").addElement("message", "\u6b64\u5e74\u7ea7\u5df2\u5b58\u5728");
        }
        else {
            this.gradeService.saveOrUpdate(new Grade(0, Integer.parseInt(grade)));
            json.addElement("result", "1").addElement("message", "\u5e74\u7ea7\u6dfb\u52a0\u6210\u529f");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/delete/{gid}" })
    @ResponseBody
    public void delete(@PathVariable final Integer gid, final HttpServletResponse response) {
        final JSON json = new JSONObject();
        this.gradeService.delete(gid);
        json.addElement("result", "1").addElement("message", "\u5220\u9664\u6210\u529f");
        DataUtil.writeJSON(json, response);
    }
}
