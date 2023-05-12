// 
// 
// 

package exam.controller.admin;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import exam.util.json.JSON;
import exam.util.DataUtil;
import exam.model.role.Manager;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;
import exam.service.ManagerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/admin" })
public class AdminIndexController
{
    @Resource
    private ManagerService managerService;
    
    @RequestMapping({ "/index" })
    public String index() {
        return "admin/index";
    }
    
    @RequestMapping({ "/password" })
    public String password() {
        return "admin/password";
    }
    
    @RequestMapping({ "/password/check" })
    @ResponseBody
    public void check(final String password, final HttpServletRequest request, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        final Manager manager = (Manager)request.getSession().getAttribute("admin");
        if (manager.getPassword().equals(password)) {
            json.addElement("result", "1");
        }
        else {
            json.addElement("result", "0");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/password/modify" })
    public String modifyPassword(final String oldPassword, final String newPassword, final HttpServletRequest request, final Model model) {
        final Manager manager = (Manager)request.getSession().getAttribute("admin");
        if (!this.checkPassword(oldPassword, newPassword, manager)) {
            return "error";
        }
        this.managerService.updatePassword(manager.getId(), newPassword);
        manager.setPassword(newPassword);
        manager.setModified(true);
        model.addAttribute("message", (Object)"\u5bc6\u7801\u4fee\u6539\u6210\u529f");
        model.addAttribute("url", (Object)(String.valueOf(request.getContextPath()) + "/admin/index"));
        return "success";
    }
    
    private boolean checkPassword(final String oldPassword, final String newPassword, final Manager manager) {
        return manager.getPassword().equals(oldPassword) && DataUtil.isValid(newPassword) && newPassword.matches("^\\w{4,10}$");
    }
}
