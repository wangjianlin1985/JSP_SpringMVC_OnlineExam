// 
// 
// 

package exam.controller.teacher;

import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Iterator;
import java.util.List;
import exam.util.json.JSON;
import exam.model.Clazz;
import exam.util.json.JSONArray;
import exam.util.DataUtil;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import exam.service.ClazzService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller("exam.controller.teacher.ClazzController")
@RequestMapping({ "/teacher/clazz" })
public class ClazzController
{
    @Resource
    private ClazzService clazzService;
    
    @RequestMapping({ "/list" })
    @ResponseBody
    public void list(final Integer examId, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (!DataUtil.isValid(examId)) {
            json.addElement("result", "0").addElement("message", "\u53c2\u6570\u65e0\u6548");
        }
        else {
            final JSONArray array = new JSONArray();
            final List<Clazz> clazzs = this.clazzService.findByExam(examId);
            for (final Clazz clazz : clazzs) {
                array.addObject(clazz.getJSON());
            }
            json.addElement("result", "1").addElement("data", array);
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/reset" })
    @ResponseBody
    public void reset(final Integer examId, final String clazzIds, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (!DataUtil.isValid(examId) || !DataUtil.isValid(clazzIds)) {
            json.addElement("result", "0");
        }
        else {
            this.clazzService.resetExamClass(examId, clazzIds);
            json.addElement("result", "1");
        }
        DataUtil.writeJSON(json, response);
    }
}
