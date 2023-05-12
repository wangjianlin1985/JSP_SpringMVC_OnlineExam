// 
// 
// 

package exam.controller.teacher;

import exam.model.page.PageBean;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import exam.util.json.JSONArray;
import org.springframework.web.bind.annotation.ResponseBody;
import exam.util.json.JSON;
import exam.model.role.Teacher;
import exam.model.Question;
import exam.util.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import exam.model.QuestionType;
import exam.util.DataUtil;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.IOException;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.Resource;
import exam.service.QuestionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller("exam.controller.teacher.QuestionController")
@RequestMapping({ "/teacher/question" })
public class QuestionController
{
    @Resource
    private QuestionService questionService;
    @Value("#{properties['question.pageSize']}")
    private int pageSize;
    @Value("#{properties['question.pageNumber']}")
    private int pageNumber;
    
    @RequestMapping({ "/singles" })
    public String singlesHelper(final HttpServletRequest request, final Model model) throws IOException {
        return this.singles("1", null, request, model);
    }
    
    @RequestMapping({ "/singles/{pn}" })
    public String singles(@PathVariable final String pn, final String search, final HttpServletRequest request, final Model model) {
        return this.questions(DataUtil.getPageCode(pn), search, QuestionType.SINGLE, request, model);
    }
    
    @RequestMapping({ "/multis" })
    public String multisHelper(final HttpServletRequest request, final Model model) {
        return this.multis("1", null, request, model);
    }
    
    @RequestMapping({ "/multis/{pn}" })
    public String multis(@PathVariable final String pn, final String search, final HttpServletRequest request, final Model model) {
        return this.questions(DataUtil.getPageCode(pn), search, QuestionType.MULTI, request, model);
    }
    
    @RequestMapping({ "/judges" })
    public String judgesHelper(final HttpServletRequest request, final Model model) {
        return this.judges("1", null, request, model);
    }
    
    @RequestMapping({ "/judges/{pn}" })
    public String judges(@PathVariable final String pn, final String search, final HttpServletRequest request, final Model model) {
        return this.questions(DataUtil.getPageCode(pn), search, QuestionType.JUDGE, request, model);
    }
    
    @RequestMapping({ "/save" })
    @ResponseBody
    public void save(final Integer id, final String title, final String optionA, final String optionB, final String optionC, final String optionD, final String answer, final Integer point, final String type, final HttpServletRequest request, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (!DataUtil.isValid(point) || !DataUtil.isValid(title, answer, type) || (QuestionType.valueOf(type) != QuestionType.JUDGE && !DataUtil.isValid(optionA, optionB, optionC, optionD))) {
            json.addElement("result", "0");
        }
        else {
            final Question question = new Question();
            question.setType(QuestionType.valueOf(type));
            question.setAnswer(answer);
            question.setId(id);
            question.setOptionA(optionA);
            question.setOptionB(optionB);
            question.setOptionC(optionC);
            question.setOptionD(optionD);
            question.setPoint(point);
            question.setTitle(title);
            question.setTeacher((Teacher)request.getSession().getAttribute("teacher"));
            this.questionService.saveOrUpdate(question);
            json.addElement("result", "1");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/delete/{id}" })
    @ResponseBody
    public void delete(@PathVariable("id") final Integer id, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (this.questionService.isUsedByExam(id)) {
            json.addElement("result", "0").addElement("message", "\u6b64\u9898\u76ee\u88ab\u8bd5\u5377\u5f15\u7528\uff0c\u65e0\u6cd5\u5220\u9664");
        }
        else {
            this.questionService.delete(id);
            json.addElement("result", "1").addElement("message", "\u5220\u9664\u6210\u529f");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping({ "/ajax" })
    @ResponseBody
    public void list(final String type, final HttpServletRequest request, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (!DataUtil.isValid(type)) {
            json.addElement("result", "0");
        }
        else {
            final QuestionType qt = QuestionType.valueOf(type);
            final String tid = ((Teacher)request.getSession().getAttribute("teacher")).getId();
            final List<Question> questions = (qt == QuestionType.SINGLE) ? this.questionService.getSingles(tid) : ((qt == QuestionType.MULTI) ? this.questionService.getMultis(tid) : this.questionService.getJudges(tid));
            final JSONArray array = new JSONArray();
            for (final Question q : questions) {
                array.addObject(q.getJSON());
            }
            json.addElement("result", "1").addElement("data", array);
        }
        DataUtil.writeJSON(json, response);
    }
    
    private String questions(final int pageCode, final String search, final QuestionType type, final HttpServletRequest request, final Model model) {
        final Teacher teacher = (Teacher)request.getSession().getAttribute("teacher");
        String where = "where tid = '" + teacher.getId() + "' and type = '" + type.name() + "'";
        if (DataUtil.isValid(search)) {
            where = String.valueOf(where) + " and title like '%" + search + "%'";
        }
        final PageBean<Question> pageBean = this.questionService.pageSearch(pageCode, this.pageSize, this.pageNumber, where, null, null);
        model.addAttribute("pageBean", (Object)pageBean);
        model.addAttribute("search", (Object)search);
        model.addAttribute("type", (Object)type.name());
        return "teacher/question_list";
    }
    
    @RequestMapping({ "/rate/{qid}" })
    @ResponseBody
    public void rate(@PathVariable final Integer qid, final HttpServletResponse response) {
        final JSONObject json = new JSONObject();
        if (!DataUtil.isValid(qid)) {
            json.addElement("result", "0").addElement("messgae", "\u53c2\u6570\u975e\u6cd5");
        }
        else {
            final Double rate = this.questionService.articulationScore(qid);
            json.addElement("result", "1").addElement("rate", (rate == null) ? "\u65e0\u8003\u8bd5\u8bb0\u5f55!" : (String.valueOf(rate * 100.0) + "%"));
        }
        DataUtil.writeJSON(json, response);
    }
}
