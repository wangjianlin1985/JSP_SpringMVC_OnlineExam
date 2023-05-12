// 
// 
// 

package exam.util;

import exam.dto.MarkedQuestion;
import java.util.Date;
import exam.model.ExaminationResult;
import exam.dto.ExaminationAnswer;
import java.util.Iterator;
import exam.model.Question;
import exam.model.Clazz;
import exam.model.Major;
import exam.model.Grade;
import exam.model.ExamStatus;
import java.util.Calendar;
import exam.model.QuestionType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import exam.model.Exam;
import exam.model.role.Teacher;
import java.util.regex.Matcher;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import exam.util.json.JSON;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.List;

public abstract class DataUtil
{
    private static List<String> extensions;
    private static List<String> contentTypes;
    private static Pattern pattern;
    
    static {
        DataUtil.pattern = Pattern.compile("^[1-9][0-9]*$");
    }
    
    public static boolean isValid(final String str) {
        return str != null && !str.trim().equals("");
    }
    
    public static boolean isValid(final String... strs) {
        for (final String str : strs) {
            if (!isValid(str)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isValid(final List<?> list) {
        return list != null && list.size() != 0;
    }
    
    public static boolean isValid(final Map<?, ?> map) {
        return map != null && map.size() != 0;
    }
    
    public static boolean isValid(final Integer... nums) {
        for (final Integer i : nums) {
            if (i == null || i < 1) {
                return false;
            }
        }
        return true;
    }
    
    public static String getExtend(final String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }
    
    private static void init() {
        DataUtil.extensions = new ArrayList<String>(Arrays.asList("jpg", "jpeg", "png", "gif", "bmp"));
        DataUtil.contentTypes = new ArrayList<String>(Arrays.asList("image/jpeg", "image/png", "image/gif", "image/bmp"));
    }
    
    public static boolean isImage(final String extension, final String contentType) {
        if (DataUtil.extensions == null || DataUtil.contentTypes == null) {
            init();
        }
        return extension.contains(extension) && DataUtil.contentTypes.contains(contentType);
    }
    
    public static void writeJSON(final JSON json, final HttpServletResponse response, final boolean useJSONStyle) {
        if (response == null) {
            throw new NullPointerException("response\u4e3a\u7a7a");
        }
        PrintWriter out = null;
        try {
            response.setContentType(useJSONStyle ? "application/json;charset=utf-8" : "text/html;charset=utf-8");
            out = response.getWriter();
            out.write(json.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
        if (out != null) {
            out.close();
        }
    }
    
    public static void writeJSON(final JSON json, final HttpServletResponse response) {
        writeJSON(json, response, true);
    }
    
    public static boolean checkVerify(final String verify, final HttpSession session) {
        final String rand = (String)session.getAttribute("rand");
        return rand.equals(verify);
    }
    
    public static boolean isNumber(final String num) {
        if (!isValid(num)) {
            return false;
        }
        final Matcher matcher = DataUtil.pattern.matcher(num);
        return matcher.matches();
    }
    
    public static boolean isNumber(final String... nums) {
        for (final String num : nums) {
            if (!isNumber(num)) {
                return false;
            }
        }
        return true;
    }
    
    public static int getPageCode(final String str) {
        if (isNumber(str)) {
            return Integer.parseInt(str);
        }
        return 1;
    }
    
    public static Exam parseExam(final String json, final Teacher teacher) {
        if (!isValid(json) || teacher == null) {
            return null;
        }
        final JSONObject node = JSONObject.fromObject((Object)json);
        final Exam exam = new Exam();
        int singlePoints = 0;
        int multiPoints = 0;
        int judgePoints = 0;
        int sumPoints = 0;
        sumPoints += (singlePoints = parseQuestion(exam, JSONArray.fromObject(node.get("singles")), QuestionType.SINGLE, teacher));
        sumPoints += (multiPoints = parseQuestion(exam, JSONArray.fromObject(node.get("multis")), QuestionType.MULTI, teacher));
        sumPoints += (judgePoints = parseQuestion(exam, JSONArray.fromObject(node.get("judges")), QuestionType.JUDGE, teacher));
        exam.setSinglePoints(singlePoints);
        exam.setMultiPoints(multiPoints);
        exam.setJudgePoints(judgePoints);
        exam.setPoints(sumPoints);
        final JSONObject setting = JSONObject.fromObject(node.get("setting"));
        exam.setLimit(setting.getInt("timeLimit"));
        final int status = setting.getInt("status");
        if (status == 1) {
            final Calendar calendar = Calendar.getInstance();
            calendar.add(5, setting.getInt("runTime"));
            exam.setEndTime(calendar.getTime());
            exam.setStatus(ExamStatus.RUNNING);
        }
        else {
            exam.setStatus(ExamStatus.NOTRUN);
        }
        final String cids = setting.getString("cid");
        final Grade grade = new Grade(setting.getInt("gid"));
        final Major major = new Major(setting.getInt("mid"));
        String[] split;
        for (int length = (split = cids.split(",")).length, i = 0; i < length; ++i) {
            final String cid = split[i];
            final Clazz clazz = new Clazz();
            clazz.setGrade(grade);
            clazz.setMajor(major);
            clazz.setId(Integer.parseInt(cid));
            exam.addClazz(clazz);
        }
        exam.setTitle(StringUtil.htmlEncode(setting.getString("title")));
        exam.setTeacher(teacher);
        return exam;
    }
    
    private static int parseQuestion(final Exam exam, final JSONArray nodes, final QuestionType type, final Teacher teacher) {
        Question question = new Question();
        JSONObject jsonQuestion = null;
        int points = 0;
        int point = 0;
        String idStr = null;
        for (final Object o : nodes) {
            question = new Question();
            jsonQuestion = JSONObject.fromObject(o);
            question.setTitle(jsonQuestion.getString("title"));
            if (isValid(idStr = jsonQuestion.getString("id"))) {
                question.setId(Integer.parseInt(idStr));
            }
            if (type != QuestionType.JUDGE) {
                question.setOptionA(jsonQuestion.getString("optionA"));
                question.setOptionB(jsonQuestion.getString("optionB"));
                question.setOptionC(jsonQuestion.getString("optionC"));
                question.setOptionD(jsonQuestion.getString("optionD"));
            }
            point = jsonQuestion.getInt("point");
            question.setPoint(point);
            points += point;
            question.setType(type);
            question.setAnswer(jsonQuestion.getString("answer"));
            question.setTeacher(teacher);
            if (type == QuestionType.SINGLE) {
                exam.addSingleQuestion(question);
            }
            else if (type == QuestionType.MULTI) {
                exam.addMultiQuestion(question);
            }
            else {
                exam.addJudgeQuestion(question);
            }
        }
        return points;
    }
    
    public static ExaminationAnswer parseAnswers(final String result) {
        final JSONObject json = JSONObject.fromObject((Object)result);
        final ExaminationAnswer er = new ExaminationAnswer();
        er.setExamId(json.getInt("eid"));
        final JSONArray questions = json.getJSONArray("questions");
        JSONObject question = null;
        for (final Object o : questions) {
            question = JSONObject.fromObject(o);
            er.addQuestion(question.getInt("id"), question.getString("answer"));
        }
        return er;
    }
    
    public static ExaminationResult markExam(final ExaminationAnswer ea, final Exam exam, final String sid) {
        final ExaminationResult er = new ExaminationResult();
        er.setExamId(exam.getId());
        er.setStudentId(sid);
        er.setExamTitle(exam.getTitle());
        er.setTime(new Date());
        int sum = 0;
        final Map<Integer, String> answers = ea.getAnswers();
        sum += markHelper(exam.getSingleQuestions(), answers, er);
        sum += markHelper(exam.getMultiQuestions(), answers, er);
        sum += markHelper(exam.getJudgeQuestions(), answers, er);
        er.setPoint(sum);
        return er;
    }
    
    private static int markHelper(final List<Question> questions, final Map<Integer, String> answers, final ExaminationResult er) {
        int point = 0;
        MarkedQuestion mq = null;
        String wa = null;
        for (final Question q : questions) {
            mq = new MarkedQuestion();
            mq.setQuestionId(q.getId());
            wa = answers.get(q.getId());
            if (q.getAnswer().equals(wa)) {
                mq.setRight(true);
                point += q.getPoint();
            }
            else {
                mq.setRight(false);
            }
            mq.setRight(q.getAnswer().equals(wa));
            mq.setWrongAnswer(wa);
        }
        er.addMarkedQuestion(mq);
        return point;
    }
}
