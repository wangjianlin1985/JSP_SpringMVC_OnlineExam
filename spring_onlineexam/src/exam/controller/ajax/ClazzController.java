// 
// 
// 

package exam.controller.ajax;

import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Iterator;
import java.util.List;
import exam.util.json.JSON;
import exam.util.json.JSONArray;
import exam.model.Grade;
import exam.model.Major;
import exam.model.Clazz;
import exam.util.DataUtil;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import exam.service.ClazzService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/clazz" })
public class ClazzController
{
    @Resource
    private ClazzService clazzService;
    
    @RequestMapping({ "/ajax" })
    @ResponseBody
    public void ajax(final String grade, final String major, final HttpServletResponse response) {
        final JSON json = new JSONObject();
        if (!DataUtil.isNumber(grade, major)) {
            json.addElement("result", "0").addElement("message", "\u683c\u5f0f\u975e\u6cd5");
        }
        else {
            final Clazz clazz = new Clazz();
            clazz.setMajor(new Major(Integer.parseInt(major)));
            clazz.setGrade(new Grade(Integer.parseInt(grade)));
            final List<Clazz> clazzs = this.clazzService.findClazzOnly(clazz);
            json.addElement("result", "1");
            final JSONArray array = new JSONArray();
            for (final Clazz c : clazzs) {
                array.addObject(c.getJSON());
            }
            json.addElement("data", array);
        }
        DataUtil.writeJSON(json, response);
    }
}
