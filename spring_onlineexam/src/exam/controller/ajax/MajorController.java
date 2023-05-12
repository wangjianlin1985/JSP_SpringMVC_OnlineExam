// 
// 
// 

package exam.controller.ajax;

import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Iterator;
import java.util.List;
import exam.util.json.JSON;
import exam.model.Major;
import exam.util.json.JSONArray;
import exam.util.DataUtil;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import exam.service.MajorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/major" })
public class MajorController
{
    @Resource
    private MajorService majorService;
    
    @RequestMapping({ "/ajax" })
    @ResponseBody
    public void ajax(final String grade, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        List<Major> majors = null;
        if (!DataUtil.isValid(grade)) {
            majors = this.majorService.findAll();
        }
        else if (!DataUtil.isNumber(grade)) {
            json.addElement("result", "0").addElement("message", "\u5e74\u7ea7\u683c\u5f0f\u975e\u6cd5");
        }
        else {
            majors = this.majorService.findByGrade(Integer.parseInt(grade));
        }
        if (majors != null) {
            json.addElement("result", "1");
            final JSONArray array = new JSONArray();
            for (final Major major : majors) {
                array.addObject(major.getJSON());
            }
            json.addElement("data", array);
        }
        DataUtil.writeJSON(json, response);
    }
}
