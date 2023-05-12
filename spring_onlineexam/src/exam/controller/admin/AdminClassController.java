// 
// 
// 

package exam.controller.admin;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import exam.util.json.JSON;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import exam.model.Major;
import exam.model.Grade;
import exam.model.Clazz;
import exam.model.page.PageBean;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import exam.util.DataUtil;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;
import exam.service.MajorService;
import exam.service.GradeService;
import javax.annotation.Resource;
import exam.service.ClazzService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/admin/clazz" })
public class AdminClassController
{
    @Resource
    private ClazzService clazzService;
    @Resource
    private GradeService gradeService;
    @Resource
    private MajorService majorService;
    @Value("#{properties['clazz.pageSize']}")
    private int pageSize;
    @Value("#{properties['clazz.pageNumber']}")
    private int pageNumber;
    
    @RequestMapping({ "/list" })
    public String list(final String pn, final String grade, final String major, final Model model) {
        final int pageCode = DataUtil.getPageCode(pn);
        String where = "where 1 = 1 ";
        final List<Object> params = new ArrayList<Object>(2);
        if (DataUtil.isNumber(grade)) {
            where = String.valueOf(where) + " and gid = ? ";
            params.add(Integer.parseInt(grade));
        }
        if (DataUtil.isNumber(major)) {
            where = String.valueOf(where) + " and mid = ? ";
            params.add(Integer.parseInt(major));
        }
        final PageBean<Clazz> pageBean = this.clazzService.pageSearch(pageCode, this.pageSize, this.pageNumber, where, params, null);
        final List<Grade> grades = this.gradeService.findAll();
        final List<Major> majors = this.majorService.findAll();
        model.addAttribute("pageBean", (Object)pageBean);
        model.addAttribute("grades", (Object)grades);
        model.addAttribute("majors", (Object)majors);
        model.addAttribute("grade", (Object)grade);
        model.addAttribute("major", (Object)major);
        return "admin/clazz_list";
    }
    
    @RequestMapping({ "/add" })
    @ResponseBody
    public void add(final String grade, final String major, final String clazz, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (!DataUtil.isNumber(grade, major, clazz)) {
            json.addElement("result", "0").addElement("message", "\u683c\u5f0f\u975e\u6cd5");
        }
        else {
            final int gradeId = Integer.parseInt(grade);
            final int majorId = Integer.parseInt(major);
            final int cno = Integer.parseInt(clazz);
            if (this.clazzService.isExist(gradeId, majorId, cno)) {
                json.addElement("result", "0").addElement("message", "\u6b64\u73ed\u5df2\u5b58\u5728");
            }
            else {
                final Clazz clazzObj = new Clazz();
                clazzObj.setCno(cno);
                clazzObj.setGrade(new Grade(gradeId));
                clazzObj.setMajor(new Major(majorId));
                this.clazzService.saveOrUpdate(clazzObj);
                json.addElement("result", "1").addElement("message", "\u6dfb\u52a0\u6210\u529f");
            }
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/delete/{cid}" })
    @ResponseBody
    public void delete(@PathVariable final Integer cid, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        this.clazzService.delete(cid);
        json.addElement("result", "1").addElement("message", "\u5220\u9664\u6210\u529f");
        DataUtil.writeJSON(json, response);
    }
}
