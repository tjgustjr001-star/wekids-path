
package com.spring.controller.parent;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.dto.ClassVO;
import com.spring.dto.MemberVO;
import com.spring.dto.NoticeVO;
import com.spring.dto.ParentChildVO;
import com.spring.dto.parent.ParentAssignmentChildDTO;
import com.spring.dto.report.ReportDetailDTO;
import com.spring.dto.report.ReportListDTO;
import com.spring.dto.student.StudentAssignmentItemDTO;
import com.spring.security.CustomUser;
import com.spring.service.ClassService;
import com.spring.service.NoticeService;
import com.spring.service.ParentAssignmentService;
import com.spring.service.ReportService;
import com.spring.service.SettingsService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ParentPageController {
	
    @Autowired
    private NoticeService noticeService;
	
    @Autowired
    private ClassService classService;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private ParentAssignmentService parentAssignmentService;
	
    @Autowired
    private ReportService reportService;

    
    @GetMapping("/parent")
    public String parentHome(Model model) {
        model.addAttribute("pageTitle", "학부모 홈");
        model.addAttribute("currentUri", "/parent");
        model.addAttribute("contentPage", "/WEB-INF/views/parent/homeContent.jsp");

        setParentLayoutBase(model);

        model.addAttribute("greetingTitle", "안녕하세요, 김학부모님!");
        model.addAttribute("greetingMessage", "자녀의 학습과 클래스 소식을 확인해보세요.");

        Map<String, Object> childSummary = new LinkedHashMap<>();
        childSummary.put("childName", "김학생");
        childSummary.put("childGrade", "초등 3학년");
        childSummary.put("classCount", 2);
        childSummary.put("pendingAssignments", 1);
        childSummary.put("newBulletins", 2);
        model.addAttribute("childSummary", childSummary);

        List<Map<String, Object>> children = createChildrenData();
        model.addAttribute("childrenData", children);
        model.addAttribute("selectedChildId", "child1");
        model.addAttribute("selectedChild", children.get(0));
        model.addAttribute("progressPercent", 63);
        model.addAttribute("notices", createNoticeList());

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/classes")
    public String parentClassList(Model model, HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "클래스 목록");
        model.addAttribute("currentUri", "/parent/classes");
        model.addAttribute("contentPage", "/WEB-INF/views/class/listContent.jsp");

        setParentLayoutBase(model);

        MemberVO loginUser = getLoginUser(session);

        model.addAttribute("roleType", "parent");
        model.addAttribute("showJoinButton", false);
        model.addAttribute("showCreateButton", false);

        if (loginUser != null) {
            model.addAttribute("classList", classService.getParentClassList(loginUser.getMember_id()));
        } else {
            model.addAttribute("classList", List.of());
        }

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/children")
    public String parentChildrenList(Model model, HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "자녀 목록");
        model.addAttribute("currentUri", "/parent/children");
        model.addAttribute("contentPage", "/WEB-INF/views/settings/childLinkParent.jsp");

        setParentLayoutBase(model);

        MemberVO loginUser = getLoginUser(session);

        if (loginUser != null) {
            List<ParentChildVO> childList = settingsService.getLinkedChildren(loginUser.getMember_id());
            model.addAttribute("childList", childList);
        } else {
            model.addAttribute("childList", List.of());
        }

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/children/{childId}")
    public String parentChildDetail(@PathVariable("childId") int childId,
                                    Model model,
                                    HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "자녀 상세");
        model.addAttribute("currentUri", "/parent/children");
        model.addAttribute("contentPage", "/WEB-INF/views/parent/children/detailRealContent.jsp");

        setParentLayoutBase(model);

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        List<ParentChildVO> childList = settingsService.getLinkedChildren(loginUser.getMember_id());
        ParentChildVO child = settingsService.getChildDetail(loginUser.getMember_id(), childId);

        if (child == null) {
            model.addAttribute("childList", childList);
            model.addAttribute("msg", "not_found_child");
            model.addAttribute("contentPage", "/WEB-INF/views/settings/childLinkParent.jsp");
            return "common/layout/parentLayout";
        }

        model.addAttribute("childList", childList);
        model.addAttribute("child", child);

        int overallProgress = Math.round((child.getLearningProgressRate() + child.getAssignmentRate()) / 2.0f);
        model.addAttribute("overallProgress", overallProgress);

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/classes/{classId}")
    public String parentClassHome(@PathVariable("classId") int classId,
                                  Model model,
                                  HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "클래스 홈");
        model.addAttribute("currentUri", "/parent/classes/" + classId);
        model.addAttribute("contentPage", "/WEB-INF/views/parent/class/homeContent.jsp");

        setParentLayoutBase(model);
        setParentClassDetailBase(model, classId, session);

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/classes/{classId}/bulletins")
    public String parentBulletinList(@PathVariable("classId") int classId,
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
        model.addAttribute("currentUri", "/parent/classes/" + classId + "/bulletins");
        model.addAttribute("contentPage", "/WEB-INF/views/notice/list.jsp");

        model.addAttribute("isTeacher", false);
        model.addAttribute("isStudentOrParent", true);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("parentCount", parentCount);

        setParentLayoutBase(model);
        setParentClassDetailBase(model, classId, session);

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/classes/{classId}/assignments")
    public String parentAssignmentList(@PathVariable("classId") int classId,
                                       @RequestParam(value = "childId", required = false) Integer childId,
                                       Model model,
                                       HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "과제");
        model.addAttribute("currentUri", "/parent/classes/" + classId + "/assignments");
        model.addAttribute("contentPage", "/WEB-INF/views/parent/assignment/listContent.jsp");

        setParentLayoutBase(model);
        setParentClassDetailBase(model, classId, session);

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        List<ParentAssignmentChildDTO> children = parentAssignmentService.getParentAssignmentChildren(loginUser.getMember_id(), classId);
        model.addAttribute("assignmentChildren", children);

        Integer selectedChildId = null;
        ParentAssignmentChildDTO selectedChild = null;

        if (!children.isEmpty()) {
            selectedChild = children.get(0);
            selectedChildId = selectedChild.getStudentId();

            if (childId != null) {
                for (ParentAssignmentChildDTO child : children) {
                    if (child.getStudentId() == childId.intValue()) {
                        selectedChild = child;
                        selectedChildId = childId;
                        break;
                    }
                }
            }
        }

        List<StudentAssignmentItemDTO> assignmentList = List.of();
        if (selectedChildId != null) {
            assignmentList = parentAssignmentService.getParentAssignmentList(loginUser.getMember_id(), classId, selectedChildId);
        }

        model.addAttribute("selectedChildId", selectedChildId);
        model.addAttribute("selectedChild", selectedChild);
        model.addAttribute("assignmentList", assignmentList);

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/classes/{classId}/assignments/{assignmentId}/download")
    public void downloadParentAssignmentFile(@PathVariable("classId") int classId,
                                             @PathVariable("assignmentId") int assignmentId,
                                             @RequestParam("childId") int childId,
                                             HttpSession session,
                                             HttpServletResponse response) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        StudentAssignmentItemDTO detail = parentAssignmentService.getParentAssignmentDetail(
                loginUser.getMember_id(), classId, childId, assignmentId);

        if (!detail.isCanDownload()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File file = new File(detail.getUploadPath());
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/octet-stream");
        String encodedName = URLEncoder.encode(detail.getAttachedFile(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedName);
        response.setContentLengthLong(file.length());

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
            out.flush();
        }
    }

    @GetMapping("/parent/classes/{classId}/reports")
    public String parentReportPage(@PathVariable("classId") int classId,
                                   @RequestParam(value = "studentId", required = false) Integer studentId,
                                   @RequestParam(value = "periodFilter", required = false) String periodFilter,
                                   Model model,
                                   HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        int parentId = loginUser.getMember_id();

        List<ParentChildVO> childList = settingsService.getLinkedChildren(parentId);
        
        List<ReportListDTO> reportList =
                reportService.getParentReportList(parentId, classId, studentId, periodFilter);

        model.addAttribute("classId", classId);
        model.addAttribute("selectedStudentId", studentId);
        model.addAttribute("periodFilter", periodFilter);
        model.addAttribute("childList", childList);
        model.addAttribute("reportList", reportList);

        model.addAttribute("contentPage", "/WEB-INF/views/parent/report/listContent.jsp");
        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/classes/{classId}/reports/{reportId}")
    @ResponseBody
    public ReportDetailDTO getParentReportDetail(@PathVariable("classId") int classId,
                                                 @PathVariable("reportId") int reportId,
                                                 @RequestParam("studentId") int studentId,
                                                 HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        int parentId = loginUser.getMember_id();

        return reportService.getParentReportDetail(parentId, studentId, classId, reportId);
    }

    private void setParentLayoutBase(Model model) {
        model.addAttribute("roleKey", "parent");
        model.addAttribute("roleLabel", "학부모");
        model.addAttribute("homeUrl", "/parent");
        model.addAttribute("classMenuUrl", "/parent/classes");
        model.addAttribute("childrenMenuUrl", "/parent/children");
        model.addAttribute("notificationUrl", "/parent/notifications");
        model.addAttribute("settingsUrl", "/parent/settings");
        model.addAttribute("isClassDetail", false);
        model.addAttribute("classId", null);
        model.addAttribute("className", null);
    }

    private void setParentClassDetailBase(Model model, int classId, HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        ClassVO classInfo = null;

        if (loginUser != null) {
            classInfo = classService.getParentClassDetail(loginUser.getMember_id(), classId);
        }

        model.addAttribute("isClassDetail", true);
        model.addAttribute("classId", classId);
        model.addAttribute("className", classInfo != null ? classInfo.getClassName() : "클래스");

        model.addAttribute("classHomeUrl", "/parent/classes/" + classId);
        model.addAttribute("bulletinUrl", "/parent/classes/" + classId + "/bulletins");
        model.addAttribute("assignmentUrl", "/parent/classes/" + classId + "/assignments");
        model.addAttribute("reportUrl", "/parent/classes/" + classId + "/reports");
    }

    private List<Map<String, Object>> createChildrenData() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> child1 = new LinkedHashMap<>();
        child1.put("id", "child1");
        child1.put("name", "김학생");
        child1.put("grade", "3학년 2반");
        child1.put("school", "서울초등학교");
        child1.put("teacher", "이선생");
        child1.put("phone", "010-1111-2222");
        child1.put("learningStats", createLearningStats(8, 3, 5));
        child1.put("assignments", Arrays.asList(
                createAssignment(1, "수학 3단원 문제풀이", "수학", "오늘 23:59", "미진행"),
                createAssignment(2, "과학 실험 관찰 보고서", "과학", "내일 18:00", "진행중"),
                createAssignment(3, "국어 독서록 작성", "국어", "금요일 18:00", "완료")
        ));
        child1.put("weeklyStudyData", Arrays.asList(
                createStudyDay("월", 2.5, 63),
                createStudyDay("화", 3.2, 80),
                createStudyDay("수", 1.8, 45),
                createStudyDay("목", 4.0, 100),
                createStudyDay("금", 2.1, 53),
                createStudyDay("토", 1.0, 25),
                createStudyDay("일", 0.0, 8)
        ));

        Map<String, Object> child2 = new LinkedHashMap<>();
        child2.put("id", "child2");
        child2.put("name", "김동생");
        child2.put("grade", "1학년 1반");
        child2.put("school", "서울초등학교");
        child2.put("teacher", "박선생");
        child2.put("phone", "없음");
        child2.put("learningStats", createLearningStats(5, 1, 4));
        child2.put("assignments", Arrays.asList(
                createAssignment(4, "그림일기 쓰기", "국어", "오늘 18:00", "완료"),
                createAssignment(5, "가족 얼굴 그리기", "미술", "내일 18:00", "진행중")
        ));
        child2.put("weeklyStudyData", Arrays.asList(
                createStudyDay("월", 1.5, 38),
                createStudyDay("화", 1.0, 25),
                createStudyDay("수", 2.0, 50),
                createStudyDay("목", 1.5, 38),
                createStudyDay("금", 1.0, 25),
                createStudyDay("토", 0.5, 13),
                createStudyDay("일", 0.0, 8)
        ));

        list.add(child1);
        list.add(child2);
        return list;
    }

    private List<Map<String, Object>> createNoticeList() {
        return Arrays.asList(
                createNotice("yellow", "체험학습 동의서 제출 안내", "2026학년도 1학기 현장체험학습 관련 안내문입니다.", "2시간 전"),
                createNotice("blue", "수학 단원평가 결과 확인", "3단원 평가 결과 및 오답노트 과제가 등록되었습니다.", "어제")
        );
    }

    private List<Map<String, Object>> createChildDetailList() {
        return Arrays.asList(
                createChildDetail("1", "김지훈", "2026학년도 3학년 2반", "3학년", 1, "김", "오늘 08:30",
                        "김선생",
                        "지훈이가 최근 수학 시간에 질문도 많이 하고 발표도 잘하고 있습니다. 가정에서도 많은 칭찬 부탁드립니다!",
                        "어제",
                        8, 8, 4, 3,
                        Arrays.asList(
                                createPendingItem(1, "수학 3단원 오답노트 과제 미제출", "assignment", "오늘 23:59까지", true),
                                createPendingItem(2, "가정통신문 동의서 회신 필요", "notice", "내일 18:00까지", false)
                        ),
                        Arrays.asList(
                                createSubjectProgress("국어", 100, "blue"),
                                createSubjectProgress("수학", 85, "green"),
                                createSubjectProgress("과학", 100, "purple"),
                                createSubjectProgress("사회", 60, "amber")
                        )),
                createChildDetail("2", "김서연", "2026학년도 1학년 3반", "1학년", 15, "김", "어제 16:42",
                        "박선생",
                        "서연이가 미술 시간에 창의적인 작품을 많이 만들어서 칭찬합니다. 읽기 연습도 꾸준히 잘 하고 있어요!",
                        "3일 전",
                        6, 5, 3, 3,
                        new ArrayList<>(),
                        Arrays.asList(
                                createSubjectProgress("국어", 80, "blue"),
                                createSubjectProgress("수학", 100, "green"),
                                createSubjectProgress("통합(봄)", 70, "pink")
                        ))
        );
    }

    private Map<String, Object> createChildDetail(String id, String name, String className, String grade, int number,
                                                  String initial, String lastAccess, String teacherName,
                                                  String teacherComment, String teacherCommentDate,
                                                  int learningTotal, int learningDone,
                                                  int assignmentTotal, int assignmentDone,
                                                  List<Map<String, Object>> pendingItems,
                                                  List<Map<String, Object>> weeklySubjects) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("className", className);
        map.put("grade", grade);
        map.put("number", number);
        map.put("initial", initial);
        map.put("lastAccess", lastAccess);
        map.put("teacherName", teacherName);
        map.put("teacherComment", teacherComment);
        map.put("teacherCommentDate", teacherCommentDate);
        map.put("learningTotal", learningTotal);
        map.put("learningDone", learningDone);
        map.put("assignmentTotal", assignmentTotal);
        map.put("assignmentDone", assignmentDone);
        map.put("pendingItems", pendingItems);
        map.put("weeklySubjects", weeklySubjects);
        return map;
    }

    private Map<String, Object> createPendingItem(int id, String title, String type, String deadline, boolean urgent) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("type", type);
        map.put("deadline", deadline);
        map.put("urgent", urgent);
        return map;
    }

    private Map<String, Object> createSubjectProgress(String subject, int progress, String colorClass) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("subject", subject);
        map.put("progress", progress);
        map.put("colorClass", colorClass);
        return map;
    }

    private Map<String, Object> createLearningStats(int total, int inProgress, int completed) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("total", total);
        map.put("inProgress", inProgress);
        map.put("completed", completed);
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

    private Map<String, Object> createStudyDay(String day, double hours, int heightPercent) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("day", day);
        map.put("hours", hours);
        map.put("heightPercent", heightPercent);
        return map;
    }

    private Map<String, Object> createNotice(String dotColor, String title, String content, String timeText) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("dotColor", dotColor);
        map.put("title", title);
        map.put("content", content);
        map.put("timeText", timeText);
        return map;
    }

    private Map<String, Object> createParentAssignmentChild(int id, String name, String grade) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("grade", grade);
        return map;
    }

    private Map<String, Object> createParentAssignmentItem(int id, String title, String subject, String status,
                                                           String deadline, String type, boolean submitted,
                                                           String submittedAt, String feedback,
                                                           String content, String mySubmission) {
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
        return map;
    }

    private Map<String, Object> createParentReportData(List<Map<String, Object>> data,
                                                       int completedLearning,
                                                       int submittedAssignments,
                                                       int learningHours,
                                                       String comment) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("data", data);
        map.put("completedLearning", completedLearning);
        map.put("submittedAssignments", submittedAssignments);
        map.put("learningHours", learningHours);
        map.put("comment", comment);

        int totalSubmitted = 0;
        int totalAll = 0;
        for (Map<String, Object> item : data) {
            totalSubmitted += (Integer) item.get("submitted");
            totalAll += (Integer) item.get("total");
        }
        int overallRate = totalAll > 0 ? Math.round((float) totalSubmitted * 100 / totalAll) : 0;
        map.put("overallRate", overallRate);

        return map;
    }

    private List<Map<String, Object>> createParentReportChart(Map<String, Object>... items) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> item : items) {
            list.add(item);
        }
        return list;
    }

    private Map<String, Object> createParentReportSubject(int id, String subject, int submitted, int total, String colorClass) {
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