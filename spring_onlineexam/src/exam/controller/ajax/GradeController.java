// 
// 
// 

package exam.controller.ajax;

import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Iterator;
import java.util.List;
import exam.util.DataUtil;
import exam.util.json.JSON;
import exam.model.Grade;
import exam.util.json.JSONArray;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import exam.service.GradeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/grade" })
public class GradeController
{
    @Resource
    private GradeService gradeService;
    
    @RequestMapping({ "/ajax" })
    @ResponseBody
    public void ajax(final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        final JSONArray array = new JSONArray();
        final List<Grade> grades = this.gradeService.findAll();
        json.addElement("result", "1");
        for (final Grade grade : grades) {
            array.addObject(grade.getJSON());
        }
        json.addElement("data", array);
        DataUtil.writeJSON(json, response);
    }
}
