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
import exam.model.Major;
import exam.model.page.PageBean;
import java.util.HashMap;
import java.util.List;
import exam.util.DataUtil;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.Resource;
import exam.service.MajorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/admin/major" })
public class AdminMajorController
{
    @Resource
    private MajorService majorService;
    @Value("#{properties['major.pageSize']}")
    private int pageSize;
    @Value("#{properties['major.pageNumber']}")
    private int pageNumber;
    
    @RequestMapping({ "/list" })
    public String list(final String pn, final String search, final Model model) {
        final int pageCode = DataUtil.getPageCode(pn);
        String where = null;
        if (DataUtil.isValid(search)) {
            where = " where name like '%" + search + "%'";
        }
        final PageBean<Major> pageBean = this.majorService.pageSearch(pageCode, this.pageSize, this.pageNumber, where, null, null);
        model.addAttribute("pageBean", (Object)pageBean);
        model.addAttribute("search", (Object)search);
        return "admin/major_list";
    }
    
    @RequestMapping({ "/add" })
    @ResponseBody
    public void add(String major, final HttpServletResponse response) {
        final JSON json = new JSONObject();
        major = StringUtil.htmlEncode(major);
        if (!DataUtil.isValid(major)) {
            json.addElement("result", "0").addElement("message", "\u8bf7\u8f93\u5165\u4e13\u4e1a\u540d\u79f0");
        }
        else if (this.majorService.findByName(major) != null) {
            json.addElement("result", "0").addElement("message", "\u6b64\u4e13\u4e1a\u5df2\u5b58\u5728");
        }
        else {
            this.majorService.saveOrUpdate(new Major(major));
            json.addElement("result", "1").addElement("message", "\u4e13\u4e1a\u6dfb\u52a0\u6210\u529f");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/edit" })
    @ResponseBody
    public void edit(final String id, String major, final HttpServletResponse response) {
        final JSON json = new JSONObject();
        major = StringUtil.htmlEncode(major);
        if (!DataUtil.isNumber(id)) {
            json.addElement("result", "0").addElement("message", "\u975e\u6cd5\u6570\u636e");
        }
        else if (!DataUtil.isValid(major)) {
            json.addElement("result", "0").addElement("message", "\u8bf7\u8f93\u5165\u4e13\u4e1a\u540d\u79f0");
        }
        else {
            final int _id = Integer.parseInt(id);
            final Major m = new Major();
            m.setId(_id);
            m.setName(major);
            this.majorService.saveOrUpdate(m);
            json.addElement("result", "1").addElement("message", "\u4fee\u6539\u6210\u529f");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/delete/{mid}" })
    @ResponseBody
    public void delete(@PathVariable final Integer mid, final HttpServletResponse response) {
        final JSON json = new JSONObject();
        this.majorService.delete(mid);
        json.addElement("result", "1").addElement("message", "\u5220\u9664\u6210\u529f");
        DataUtil.writeJSON(json, response);
    }
}
