// 
// 
// 

package exam.controller.student;

import exam.dto.ERView;
import exam.model.ExaminationResult;
import exam.model.page.PageBean;
import java.util.HashMap;
import java.util.List;
import exam.util.DataUtil;
import exam.model.role.Student;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.Resource;
import exam.service.ExaminationResultService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller("exam.controller.student.ExaminationResultController")
@RequestMapping({ "/student/notes" })
public class ExaminationResultController
{
    @Resource
    private ExaminationResultService examinationResultService;
    @Value("#{properties['student.examinationResult.pageSize']}")
    private int pageSize;
    @Value("#{properties['student.examinationResult.pageNumber']}")
    private int pageNumber;
    
    @RequestMapping
    public String notesHelper(final HttpServletRequest request, final Model model) {
        return this.notes("1", request, model);
    }
    
    @RequestMapping({ "/{pn}" })
    public String notes(@PathVariable final String pn, final HttpServletRequest request, final Model model) {
        final Student student = (Student)request.getSession().getAttribute("student");
        final int pageCode = DataUtil.getPageCode(pn);
        final String where = " where sid = '" + student.getId() + "' ";
        final PageBean<ExaminationResult> pageBean = this.examinationResultService.pageSearch(pageCode, this.pageSize, this.pageNumber, where, null, null);
        model.addAttribute("pageBean", (Object)pageBean);
        return "student/examinationResult_list";
    }
    
    @RequestMapping({ "/view/{id}" })
    public String view(@PathVariable final Integer id, final Model model) {
        if (!DataUtil.isValid(id)) {
            return "error";
        }
        final ERView view = this.examinationResultService.getViewById(id);
        model.addAttribute("view", (Object)view);
        return "student/examinationResult_view";
    }
}
