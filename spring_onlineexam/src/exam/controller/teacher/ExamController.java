// 
// 
// 

package exam.controller.teacher;

import java.io.File;
import exam.util.StringUtil;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.List;
import java.net.URLEncoder;
import exam.dto.StudentReport;
import exam.util.ExcelUtil;
import java.io.IOException;
import java.util.Iterator;
import exam.dto.StatisticsData;
import exam.util.json.JSONArray;
import exam.util.JFreechartUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import exam.util.json.JSON;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import exam.model.Exam;
import exam.model.page.PageBean;
import exam.model.role.Teacher;
import exam.util.DataUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;
import exam.service.ExaminationResultService;
import javax.annotation.Resource;
import exam.service.ExamService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller("exam.controller.teacher.ExamController")
@RequestMapping({ "/teacher/exam" })
public class ExamController
{
    @Resource
    private ExamService examService;
    @Resource
    private ExaminationResultService examinationResultService;
    @Value("#{properties['exam.pageSize']}")
    private int pageSize;
    @Value("#{properties['exam.pageNumber']}")
    private int pageNumber;
    
    @RequestMapping({ "/list" })
    public String list(final String pn, final Model model, final HttpServletRequest request) {
        final int pageCode = DataUtil.getPageCode(pn);
        final String tid = ((Teacher)request.getSession().getAttribute("teacher")).getId();
        final PageBean<Exam> pageBean = this.examService.pageSearchByTeacher(pageCode, this.pageSize, this.pageNumber, tid);
        model.addAttribute("pageBean", (Object)pageBean);
        return "teacher/exam_list";
    }
    
    @RequestMapping({ "/add" })
    public String addUI() {
        return "teacher/exam_add";
    }
    
    @RequestMapping({ "/save" })
    @ResponseBody
    public void add(final String exam, final HttpServletRequest request, final HttpServletResponse response) {
        final Teacher teacher = (Teacher)request.getSession().getAttribute("teacher");
        final JSON json = new JSONObject();
        final Exam result = DataUtil.parseExam(exam, teacher);
        if (result.getPoints() == 0) {
            json.addElement("result", "0").addElement("message", "\u8bf7\u4e0d\u8981\u63d0\u4ea4\u7a7a\u8bd5\u5377!");
        }
        else {
            this.examService.saveOrUpdate(result);
            json.addElement("result", "1");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/remove" })
    @ResponseBody
    public void delete(final Integer examId, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (!DataUtil.isValid(examId)) {
            json.addElement("result", "0");
        }
        else {
            this.examService.delete(examId);
            json.addElement("result", "1");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/status" })
    @ResponseBody
    public void switchStatus(final Integer examId, final String status, final Integer days, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (!DataUtil.isValid(examId) || !DataUtil.isValid(status)) {
            json.addElement("result", "0");
        }
        else {
            this.examService.switchStatus(examId, status, days);
            json.addElement("result", "1");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/statistics/{eid}" })
    public String toStatistics(@PathVariable final Integer eid, final Model model) {
        model.addAttribute("eid", (Object)eid);
        return "teacher/statistics";
    }
    
    @RequestMapping({ "/statistics/do/{eid}" })
    @ResponseBody
    public void statistics(@PathVariable final Integer eid, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final JSONObject json = new JSONObject();
        final StatisticsData data = this.examinationResultService.getStatisticsData(eid);
        if (data == null) {
            json.addElement("result", "0");
        }
        else {
            final String realPath = String.valueOf(request.getServletContext().getRealPath("/")) + "/";
            this.checkPath(String.valueOf(realPath) + "charts");
            final String imagePath = "charts/pie_" + eid + ".png";
            JFreechartUtil.generateChart(data, String.valueOf(realPath) + imagePath);
            json.addElement("result", "1").addElement("url", imagePath).addElement("highestPoint", String.valueOf(data.getHighestPoint())).addElement("lowestPoint", String.valueOf(data.getLowestPoint())).addElement("title", data.getTitle()).addElement("count", String.valueOf(data.getPersonCount()));
            final JSONArray highestNames = new JSONArray();
            for (final String name : data.getHighestNames()) {
                highestNames.addElement("name", name);
            }
            json.addElement("highestNames", highestNames);
            final JSONArray lowestNames = new JSONArray();
            for (final String name2 : data.getLowestNames()) {
                lowestNames.addElement("name", name2);
            }
            json.addElement("lowestNames", lowestNames);
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/download/{eid}" })
    public String download(@PathVariable final Integer eid, final Model model) {
        model.addAttribute("eid", (Object)eid);
        return "teacher/download";
    }
    
    @RequestMapping({ "/report/{eid}" })
    @ResponseBody
    public void report(@PathVariable("eid") final Integer eid, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final List<StudentReport> reportData = this.examinationResultService.getReportData(eid);
        final String realPath = String.valueOf(request.getServletContext().getRealPath("/")) + "/reports";
        this.checkPath(realPath);
        final InputStream is = ExcelUtil.generateExcel(reportData, String.valueOf(realPath) + "/report_" + eid + ".xls");
        response.setContentType("application/zip");
        final String fileName = URLEncoder.encode(String.valueOf(reportData.get(0).getTitle()) + "\u6210\u7ee9\u5355.xls", "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        final OutputStream os = (OutputStream)response.getOutputStream();
        final byte[] b = new byte[1024];
        int i = 0;
        while ((i = is.read(b)) > 0) {
            os.write(b, 0, i);
        }
        os.flush();
        is.close();
        os.close();
    }
    
    @RequestMapping({ "/update/{eid}" })
    @ResponseBody
    public void update(@PathVariable final Integer eid, String title, final Integer limit, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        title = StringUtil.htmlEncode(title);
        if (!DataUtil.isValid(eid, limit) || !DataUtil.isValid(title)) {
            json.addElement("result", "0");
        }
        else {
            final Exam exam = new Exam();
            exam.setId(eid);
            exam.setLimit(limit);
            exam.setTitle(title);
            this.examService.saveOrUpdate(exam);
            json.addElement("result", "1");
        }
        DataUtil.writeJSON(json, response);
    }
    
    private void checkPath(final String path) {
        final File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
