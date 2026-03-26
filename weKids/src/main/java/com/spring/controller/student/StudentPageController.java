package com.spring.controller.student;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.dto.ClassVO;
import com.spring.dto.MemberVO;
import com.spring.dto.NoticeVO;
import com.spring.security.CustomUser;
import com.spring.service.ClassService;
import com.spring.service.NoticeService;
import com.spring.service.StudentLearnProgressService;
import com.spring.service.StudentLearnService;

import jakarta.servlet.http.HttpSession;

@Controller
public class StudentPageController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ClassService classService;

    @Autowired
    private StudentLearnService studentLearnService;
    
    @Autowired
    private StudentLearnProgressService studentLearnProgressService;
    
    @GetMapping("/student")
    public String studentHome(Model model) {
        model.addAttribute("pageTitle", "학생 홈");
        model.addAttribute("currentUri", "/student");
        model.addAttribute("contentPage", "/WEB-INF/views/student/homeContent.jsp");

        setStudentLayoutBase(model);

        model.addAttribute("termLabel", "2026 1학기");
        model.addAttribute("greetingTitle", "안녕하세요, 김학생님!");
        model.addAttribute("greetingMessage", "오늘도 즐거운 학습 시간되세요!");

        Map<String, Object> learningStats = new LinkedHashMap<>();
        learningStats.put("total", 8);
        learningStats.put("inProgress", 3);
        learningStats.put("completed", 5);
        learningStats.put("progressPercent", 63);
        model.addAttribute("learningStats", learningStats);

        List<Map<String, Object>> weeklyStudyHours = new ArrayList<>();
        weeklyStudyHours.add(createStudyDay("월", 2.5, 63));
        weeklyStudyHours.add(createStudyDay("화", 3.2, 80));
        weeklyStudyHours.add(createStudyDay("수", 1.8, 45));
        weeklyStudyHours.add(createStudyDay("목", 4.0, 100));
        weeklyStudyHours.add(createStudyDay("금", 2.1, 53));
        weeklyStudyHours.add(createStudyDay("토", 1.0, 25));
        weeklyStudyHours.add(createStudyDay("일", 0.0, 8));
        model.addAttribute("weeklyStudyHours", weeklyStudyHours);

        List<Map<String, Object>> assignments = new ArrayList<>();
        assignments.add(createAssignment(1, "수학 3단원 문제풀이", "수학", "오늘 23:59", "미진행"));
        assignments.add(createAssignment(2, "과학 실험 관찰 보고서", "과학", "내일 18:00", "진행중"));
        assignments.add(createAssignment(3, "국어 독서록 작성", "국어", "금요일 18:00", "완료"));
        model.addAttribute("assignments", assignments);

        List<Map<String, Object>> recentNews = new ArrayList<>();
        recentNews.add(createNews("yellow", "체험학습 동의서 제출 안내",
                "2026학년도 1학기 현장체험학습 관련 안내문입니다.", "2시간 전"));
        recentNews.add(createNews("green", "수학 단원평가 결과 확인",
                "3단원 평가 결과 및 오답노트 과제가 등록되었습니다.", "어제"));
        model.addAttribute("recentNews", recentNews);

        Map<String, Object> currentClass = new LinkedHashMap<>();
        currentClass.put("classId", 1);
        currentClass.put("className", "2026학년도 3학년 2반");
        currentClass.put("teacherName", "김교사");
        model.addAttribute("currentClass", currentClass);

        return "common/layout/studentLayout";
    }

    @GetMapping("/student/classes")
    public String studentClassList(Model model, HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "클래스 목록");
        model.addAttribute("currentUri", "/student/classes");
        model.addAttribute("contentPage", "/WEB-INF/views/class/listContent.jsp");

        setStudentLayoutBase(model);

        MemberVO loginUser = getLoginUser(session);

        model.addAttribute("roleType", "student");
        model.addAttribute("showJoinButton", true);
        model.addAttribute("showCreateButton", false);

        if (loginUser != null) {
            model.addAttribute("classList", classService.getStudentClassList(loginUser.getMember_id()));
        } else {
            model.addAttribute("classList", List.of());
        }

        return "common/layout/studentLayout";
    }
    
    @GetMapping("/student/classes/join")
    public String studentClassJoinForm(Model model) {
        model.addAttribute("pageTitle", "클래스 가입");
        model.addAttribute("currentUri", "/student/classes/join");
        model.addAttribute("contentPage", "/WEB-INF/views/student/class/joinContent.jsp");

        setStudentLayoutBase(model);

        return "common/layout/studentLayout";
    }

    @PostMapping("/student/classes/join")
    public String studentClassJoinSubmit(@RequestParam("inviteCode") String inviteCode,
                                         HttpSession session,
                                         Model model) throws Exception {
        MemberVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        try {
            classService.joinClass(loginUser.getMember_id(), inviteCode);

            return "redirect:/student/classes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("pageTitle", "클래스 가입");
            model.addAttribute("currentUri", "/student/classes/join");
            model.addAttribute("contentPage", "/WEB-INF/views/student/class/joinContent.jsp");

            setStudentLayoutBase(model);
            model.addAttribute("joinError", e.getMessage());
            model.addAttribute("inviteCode", inviteCode);

            return "common/layout/studentLayout";
        }
    }
    
    @GetMapping("/student/classes/{classId}")
    public String studentClassHome(@PathVariable("classId") int classId,
                                   Model model,
                                   HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "클래스 홈");
        model.addAttribute("currentUri", "/student/classes/" + classId);
        model.addAttribute("contentPage", "/WEB-INF/views/student/class/homeContent.jsp");

        setStudentLayoutBase(model);
        setStudentClassDetailBase(model, classId, session);
        setStudentHomeDummyData(model, classId, session);

        return "common/layout/studentLayout";
    }

    @GetMapping("/student/classes/{classId}/bulletins")
    public String studentBulletinList(@PathVariable("classId") int classId,
                                      Model model,
                                      HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);

        List<NoticeVO> noticeList = noticeService.getNoticeList(classId, loginUser);

        long totalCount = noticeList.size();
        long studentCount = noticeList.stream()
                .filter(notice -> "STUDENT".equals(notice.getTarget()))
                .count();
        long parentCount = noticeList.stream()
                .filter(notice -> "PARENT".equals(notice.getTarget()))
                .count();
        model.addAttribute("classId", classId);
        model.addAttribute("pageTitle", "가정통신문");
        model.addAttribute("currentUri", "/student/classes/" + classId + "/bulletins");
        model.addAttribute("contentPage", "/WEB-INF/views/notice/list.jsp");

        model.addAttribute("isTeacher", false);
        model.addAttribute("isStudentOrParent", true);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("parentCount", parentCount);

        setStudentLayoutBase(model);
        setStudentClassDetailBase(model, classId, session);

        return "common/layout/studentLayout";
    }

    @GetMapping("/student/classes/{classId}/learns")
    public String studentLearnList(@PathVariable("classId") int classId,
                                   Model model,
                                   HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "학습");
        model.addAttribute("currentUri", "/student/classes/" + classId + "/learns");
        model.addAttribute("contentPage", "/WEB-INF/views/student/learn/listContent.jsp");

        setStudentLayoutBase(model);
        setStudentClassDetailBase(model, classId, session);

        MemberVO loginUser = getLoginUser(session);
        if (loginUser != null) {
            model.addAttribute("learnList",
                    studentLearnService.getStudentLearnList(loginUser.getMember_id(), classId));
        } else {
            model.addAttribute("learnList", List.of());
        }

        return "common/layout/studentLayout";
    }

    @PostMapping("/student/classes/{classId}/learns/{learnId}/video-progress")
    @ResponseBody
    public String saveVideoProgress(@PathVariable("classId") int classId,
                                    @PathVariable("learnId") int learnId,
                                    @RequestParam("currentSecond") int currentSecond,
                                    @RequestParam("durationSecond") int durationSecond,
                                    @RequestParam("progressRate") int progressRate,
                                    HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "FAIL";
        }

        studentLearnProgressService.saveVideoProgress(
                loginUser.getMember_id(),
                classId,
                learnId,
                currentSecond,
                durationSecond,
                progressRate
        );

        return "OK";
    }
    @PostMapping("/student/classes/{classId}/learns/{learnId}/text-progress")
    @ResponseBody
    public String saveTextProgress(@PathVariable("classId") int classId,
                                   @PathVariable("learnId") int learnId,
                                   @RequestParam("scrollTop") int scrollTop,
                                   @RequestParam("progressRate") int progressRate,
                                   HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "FAIL";
        }

        studentLearnProgressService.saveTextProgress(
                loginUser.getMember_id(),
                classId,
                learnId,
                scrollTop,
                progressRate
        );

        return "OK";
    }
    @PostMapping("/student/classes/{classId}/learns/{learnId}/start")
    @ResponseBody
    public String startLearning(@PathVariable("classId") int classId,
                                @PathVariable("learnId") int learnId,
                                HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "FAIL";
        }

        studentLearnProgressService.startLearning(loginUser.getMember_id(), classId, learnId);
        return "OK";
    }

    @PostMapping("/student/classes/{classId}/learns/{learnId}/complete")
    @ResponseBody
    public String completeLearning(@PathVariable("classId") int classId,
                                   @PathVariable("learnId") int learnId,
                                   HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "FAIL";
        }

        studentLearnProgressService.completeLearning(loginUser.getMember_id(), classId, learnId);
        return "OK";
    }

    @PostMapping("/student/classes/{classId}/learns/{learnId}/difficulty")
    @ResponseBody
    public String saveDifficulty(@PathVariable("classId") int classId,
                                 @PathVariable("learnId") int learnId,
                                 @RequestParam(value = "feedbackContent", required = false) String feedbackContent,
                                 HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "FAIL";
        }

        studentLearnProgressService.saveDifficultyFeedback(
                loginUser.getMember_id(), classId, learnId, feedbackContent);

        return "OK";
    }
    
    @GetMapping("/student/classes/{classId}/assignments")
    public String studentAssignmentList(@PathVariable("classId") int classId,
                                        Model model,
                                        HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "과제");
        model.addAttribute("currentUri", "/student/classes/" + classId + "/assignments");
        model.addAttribute("contentPage", "/WEB-INF/views/student/assignment/listContent.jsp");

        setStudentLayoutBase(model);
        setStudentClassDetailBase(model, classId, session);

        List<Map<String, Object>> assignmentList = new ArrayList<>();
        assignmentList.add(createAssignmentItem(1, "과학 실험 관찰 보고서", "과학", "진행", "오늘 18:00",
                "파일 제출", false, "", "",
                "오늘 수업시간에 진행한 강낭콩 싹틔우기 관찰 결과를 보고서 양식에 맞추어 작성해 제출하세요.",
                "", ""));
        assignmentList.add(createAssignmentItem(2, "수학 3단원 문제풀이 (p.45-48)", "수학", "마감임박", "내일 09:00",
                "텍스트 작성", false, "", "",
                "교과서 45쪽부터 48쪽까지의 문제를 풀고, 어려운 문제의 번호와 이유를 텍스트로 남겨주세요.",
                "", ""));
        assignmentList.add(createAssignmentItem(3, "국어 독서록 작성", "국어", "제출완료", "어제 18:00",
                "파일 제출", true, "어제 16:30", "",
                "이번 달 추천 도서를 읽고 독서록을 작성하여 제출하세요.",
                "홍길동전 독서록을 작성하여 첨부합니다.", "독서록_김민수.hwp"));
        assignmentList.add(createAssignmentItem(4, "영어 단어 쓰기", "영어", "반려", "2일 전",
                "텍스트 작성", true, "", "글씨를 조금 더 바르게 써주세요. 재제출 바랍니다.",
                "알파벳 A부터 Z까지 5번씩 쓰고 사진을 찍어 제출하세요.",
                "", ""));
        model.addAttribute("assignmentList", assignmentList);

        return "common/layout/studentLayout";
    }

    @GetMapping("/student/classes/{classId}/reports")
    public String studentReportList(@PathVariable("classId") int classId,
                                    Model model,
                                    HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "리포트");
        model.addAttribute("currentUri", "/student/classes/" + classId + "/reports");
        model.addAttribute("contentPage", "/WEB-INF/views/student/report/listContent.jsp");

        setStudentLayoutBase(model);
        setStudentClassDetailBase(model, classId, session);

        model.addAttribute("weeklyReport", createStudentReportData(
                createReportChart(
                        createSubjectItem(1, "국어", 3, 3, "blue"),
                        createSubjectItem(2, "수학", 4, 5, "yellow"),
                        createSubjectItem(3, "사회", 2, 3, "green"),
                        createSubjectItem(4, "과학", 3, 3, "dark"),
                        createSubjectItem(5, "영어", 2, 2, "red")
                ),
                createSummaryMap(12, 5, 8),
                "이번 주",
                "이번 주에는 수학 과목에서의 성장이 특히 돋보였습니다!\n새로운 개념을 배울 때 질문을 많이 하고 끝까지 이해하려는 모습이 훌륭해요.\n다만 사회 과목의 과제 제출이 조금 늦어지는 경향이 있으니, 스케줄 관리에 조금만 더 신경 써볼까요? 항상 응원합니다!"
        ));

        model.addAttribute("monthlyReport", createStudentReportData(
                createReportChart(
                        createSubjectItem(1, "국어", 10, 12, "blue"),
                        createSubjectItem(2, "수학", 18, 20, "yellow"),
                        createSubjectItem(3, "사회", 8, 10, "green"),
                        createSubjectItem(4, "과학", 9, 10, "dark"),
                        createSubjectItem(5, "영어", 7, 8, "red")
                ),
                createSummaryMap(45, 20, 32),
                "이번 달",
                "이번 달은 전반적으로 꾸준한 학습 태도를 보여주었습니다.\n특히 영어 어휘력이 많이 향상되었어요.\n다음 달에는 과학 실험 보고서 작성에 조금 더 시간을 투자해본다면 완벽할 것 같습니다!"
        ));

        model.addAttribute("semesterReport", createStudentReportData(
                createReportChart(
                        createSubjectItem(1, "국어", 28, 30, "blue"),
                        createSubjectItem(2, "수학", 48, 50, "yellow"),
                        createSubjectItem(3, "사회", 22, 25, "green"),
                        createSubjectItem(4, "과학", 24, 25, "dark"),
                        createSubjectItem(5, "영어", 19, 20, "red")
                ),
                createSummaryMap(120, 55, 96),
                "1학기",
                "1학기 동안 정말 고생 많았습니다!\n학기 초반에 비해 모든 과목에서 눈에 띄는 성장을 이뤄냈어요.\n스스로 학습 계획을 세우고 실천하는 능력이 크게 향상된 점을 칭찬합니다."
        ));

        return "common/layout/studentLayout";
    }

    private void setStudentLayoutBase(Model model) {
        model.addAttribute("roleKey", "student");
        model.addAttribute("roleLabel", "학생");
        model.addAttribute("homeUrl", "/student");
        model.addAttribute("classMenuUrl", "/student/classes");
        model.addAttribute("notificationUrl", "/student/notifications");
        model.addAttribute("settingsUrl", "/student/settings");
        model.addAttribute("isClassDetail", false);
        model.addAttribute("classId", null);
        model.addAttribute("className", null);
    }

    private void setStudentClassDetailBase(Model model, int classId, HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        ClassVO classInfo = null;

        if (loginUser != null) {
            classInfo = classService.getStudentClassDetail(loginUser.getMember_id(), classId);
        }

        model.addAttribute("isClassDetail", true);
        model.addAttribute("classId", classId);
        model.addAttribute("className", classInfo != null ? classInfo.getClassName() : "클래스");

        model.addAttribute("classHomeUrl", "/student/classes/" + classId);
        model.addAttribute("bulletinUrl", "/student/classes/" + classId + "/bulletins");
        model.addAttribute("learnUrl", "/student/classes/" + classId + "/learns");
        model.addAttribute("assignmentUrl", "/student/classes/" + classId + "/assignments");
        model.addAttribute("reportUrl", "/student/classes/" + classId + "/reports");
    }

    private void setStudentHomeDummyData(Model model, int classId, HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        ClassVO classInfo = null;

        if (loginUser != null) {
            classInfo = classService.getStudentClassDetail(loginUser.getMember_id(), classId);
        }

        model.addAttribute("termLabel", "2026 1학기");
        model.addAttribute("greetingTitle", "안녕하세요, 김학생님!");
        model.addAttribute("greetingMessage", "오늘도 즐거운 학습 시간되세요!");

        Map<String, Object> learningStats = new LinkedHashMap<>();
        learningStats.put("total", 8);
        learningStats.put("inProgress", 3);
        learningStats.put("completed", 5);
        learningStats.put("progressPercent", 63);
        model.addAttribute("learningStats", learningStats);

        List<Map<String, Object>> weeklyStudyHours = new ArrayList<>();
        weeklyStudyHours.add(createStudyDay("월", 2.5, 63));
        weeklyStudyHours.add(createStudyDay("화", 3.2, 80));
        weeklyStudyHours.add(createStudyDay("수", 1.8, 45));
        weeklyStudyHours.add(createStudyDay("목", 4.0, 100));
        weeklyStudyHours.add(createStudyDay("금", 2.1, 53));
        weeklyStudyHours.add(createStudyDay("토", 1.0, 25));
        weeklyStudyHours.add(createStudyDay("일", 0.0, 8));
        model.addAttribute("weeklyStudyHours", weeklyStudyHours);

        List<Map<String, Object>> assignments = new ArrayList<>();
        assignments.add(createAssignment(1, "수학 3단원 문제풀이", "수학", "오늘 23:59", "미진행"));
        assignments.add(createAssignment(2, "과학 실험 관찰 보고서", "과학", "내일 18:00", "진행중"));
        assignments.add(createAssignment(3, "국어 독서록 작성", "국어", "금요일 18:00", "완료"));
        model.addAttribute("assignments", assignments);

        List<Map<String, Object>> recentNews = new ArrayList<>();
        recentNews.add(createNews("yellow", "체험학습 동의서 제출 안내",
                "2026학년도 1학기 현장체험학습 관련 안내문입니다.", "2시간 전"));
        recentNews.add(createNews("green", "수학 단원평가 결과 확인",
                "3단원 평가 결과 및 오답노트 과제가 등록되었습니다.", "어제"));
        model.addAttribute("recentNews", recentNews);

        Map<String, Object> currentClass = new LinkedHashMap<>();
        currentClass.put("classId", classId);
        currentClass.put("className", classInfo != null ? classInfo.getClassName() : "클래스");
        currentClass.put("teacherName", classInfo != null ? classInfo.getTeacherName() : "");
        model.addAttribute("currentClass", currentClass);
    }



    private Map<String, Object> createStudyDay(String day, double hours, int heightPercent) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("day", day);
        map.put("hours", hours);
        map.put("heightPercent", heightPercent);
        return map;
    }

    private Map<String, Object> createAssignment(int id, String title, String subject, String deadline, String status) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("subject", subject);
        map.put("deadline", deadline);
        map.put("status", status);
        return map;
    }

    private Map<String, Object> createNews(String dotColor, String title, String content, String timeText) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("dotColor", dotColor);
        map.put("title", title);
        map.put("content", content);
        map.put("timeText", timeText);
        return map;
    }

    private Map<String, Object> createAssignmentItem(int id, String title, String subject, String status,
                                                     String deadline, String type, boolean submitted,
                                                     String submittedAt, String feedback, String content,
                                                     String mySubmission, String attachedFile) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("subject", subject);
        map.put("status", status);
        map.put("deadline", deadline);
        map.put("type", type);
        map.put("submitted", submitted);
        map.put("submittedAt", submittedAt);
        map.put("feedback", feedback);
        map.put("content", content);
        map.put("mySubmission", mySubmission);
        map.put("attachedFile", attachedFile);
        return map;
    }

    private Map<String, Object> createStudentReportData(List<Map<String, Object>> chart,
                                                        Map<String, Object> summary,
                                                        String tabLabel,
                                                        String comment) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("chart", chart);
        map.put("summary", summary);
        map.put("tabLabel", tabLabel);
        map.put("comment", comment);

        int totalSubmitted = 0;
        int totalAll = 0;
        for (Map<String, Object> item : chart) {
            totalSubmitted += (Integer) item.get("submitted");
            totalAll += (Integer) item.get("total");
        }
        int overallRate = totalAll > 0 ? Math.round((float) totalSubmitted * 100 / totalAll) : 0;
        map.put("overallRate", overallRate);

        return map;
    }

    private List<Map<String, Object>> createReportChart(Map<String, Object>... items) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> item : items) {
            list.add(item);
        }
        return list;
    }

    private Map<String, Object> createSubjectItem(int id, String subject, int submitted, int total, String colorClass) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("subject", subject);
        map.put("submitted", submitted);
        map.put("total", total);
        map.put("colorClass", colorClass);
        map.put("submittedHeight", total > 0 ? Math.round((float) submitted * 100 / total) : 0);
        map.put("totalHeight", 100);
        return map;
    }

    private Map<String, Object> createSummaryMap(int completed, int submitted, int hours) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("completed", completed);
        map.put("submitted", submitted);
        map.put("hours", hours);
        return map;
    }

    private MemberVO getLoginUser(HttpSession session) {
        Object sessionUser = session.getAttribute("loginUser");
        if (sessionUser instanceof MemberVO) {
            return (MemberVO) sessionUser;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUser) {
            return ((CustomUser) principal).getMember();
        }

        if (principal instanceof MemberVO) {
            return (MemberVO) principal;
        }

        return null;
    }
}