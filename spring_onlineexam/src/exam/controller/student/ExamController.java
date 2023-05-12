// 
// 
// 

package exam.controller.student;

import org.springframework.web.bind.annotation.ResponseBody;
import exam.model.ExaminationResult;
import exam.dto.ExaminationAnswer;
import exam.util.json.JSON;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import exam.model.ExamStatus;
import exam.model.Exam;
import exam.model.page.PageBean;
import exam.util.DataUtil;
import exam.model.role.Student;
import org.springframework.web.bind.annotation.PathVariable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;
import exam.service.ExaminationResultService;
import javax.annotation.Resource;
import exam.service.ExamService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller("exam.controller.student.ExamController")
@RequestMapping({ "/student/exam" })
public class ExamController
{
    @Resource
    private ExamService examService;
    @Resource
    private ExaminationResultService examinationResultService;
    @Value("#{properties['student.exam.pageSize']}")
    private int pageSize;
    @Value("#{properties['student.exam.pageNumber']}")
    private int pageNumber;
    
    @RequestMapping({ "/list" })
    public String listHelper(final Model model, final HttpServletRequest request) {
        return this.list("1", model, request);
    }
    
    @RequestMapping({ "/list/{pn}" })
    public String list(@PathVariable final String pn, final Model model, final HttpServletRequest request) {
        final Student student = (Student)request.getSession().getAttribute("student");
        final int pageCode = DataUtil.getPageCode(pn);
        final PageBean<Exam> pageBean = this.examService.pageSearchByStudent(pageCode, this.pageSize, this.pageNumber, student.getId());
        model.addAttribute("pageBean", (Object)pageBean);
        return "student/exam_list";
    }
    
    @RequestMapping({ "/{eid}" })
    public String take(@PathVariable final Integer eid, final Model model, final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final String sid = ((Student)session.getAttribute("student")).getId();
        if (this.examService.hasJoined(eid, sid)) {
            model.addAttribute("message", (Object)"\u60a8\u5df2\u53c2\u52a0\u8fc7\u6b64\u6b21\u8003\u8bd5");
            return "error";
        }
        final Exam exam = new Exam();
        exam.setId(eid);
        final Exam result = this.examService.findWithQuestions(exam);
        if (result == null) {
            return "error";
        }
        if (result.getStatus() == ExamStatus.RUNNED) {
            model.addAttribute("message", (Object)"\u5f88\u62b1\u6b49\uff0c\u6b64\u8003\u8bd5\u5df2\u5173\u95ed");
            return "error";
        }
        model.addAttribute("exam", (Object)result);
        model.addAttribute("eid", (Object)eid);
        session.setAttribute("exam", (Object)result);
        return "student/exam_take";
    }
    
    @RequestMapping({ "/submit" })
    @ResponseBody
    public void submit(final String result, final HttpServletRequest request, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (!DataUtil.isValid(result)) {
            json.addElement("result", "0").addElement("message", "\u4ea4\u5377\u5931\u8d25\uff0c\u53c2\u6570\u975e\u6cd5");
        }
        else {
            final HttpSession session = request.getSession();
            final ExaminationAnswer ea = DataUtil.parseAnswers(result);
            if (!this.examService.isUseful(ea.getExamId())) {
                json.addElement("result", "0").addElement("message", "\u62b1\u6b49\uff0c\u6b64\u8bd5\u9898\u5df2\u505c\u6b62\u8fd0\u884c\u6216\u88ab\u5220\u9664");
            }
            else {
                final Exam exam = (Exam)session.getAttribute("exam");
                final String studentId = ((Student)session.getAttribute("student")).getId();
                final ExaminationResult er = DataUtil.markExam(ea, exam, studentId);
                this.examinationResultService.saveOrUpdate(er);
                session.removeAttribute("exam");
                json.addElement("result", "1").addElement("point", String.valueOf(er.getPoint()));
            }
        }
        DataUtil.writeJSON(json, response);
    }
}
