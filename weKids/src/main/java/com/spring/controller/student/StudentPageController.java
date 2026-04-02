package com.spring.controller.student;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.dto.ClassVO;
import com.spring.dto.MemberVO;
import com.spring.dto.NoticeVO;
import com.spring.dto.report.ReportDetailDTO;
import com.spring.dto.report.ReportListDTO;
import com.spring.dto.student.StudentAssignmentItemDTO;
import com.spring.dto.student.StudentLearnItemDTO;
import com.spring.security.CustomUser;
import com.spring.service.ClassService;
import com.spring.service.NoticeService;
import com.spring.service.ReportService;
import com.spring.service.StudentAssignmentService;
import com.spring.service.StudentLearnProgressService;
import com.spring.service.StudentLearnService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
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

    @Autowired
    private StudentAssignmentService studentAssignmentService;

    @Autowired
    private ServletContext servletContext;
    
    @Autowired
    private ReportService reportService;
    
    @GetMapping("/student")
    public String studentHome(Model model,
                              HttpSession session,
                              @RequestParam(value = "classId", required = false) Integer classId) throws Exception {
        model.addAttribute("pageTitle", "학생 홈");
        model.addAttribute("currentUri", "/student");
        model.addAttribute("contentPage", "/WEB-INF/views/student/homeContent.jsp");

        setStudentLayoutBase(model);

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        List<ClassVO> classList = classService.getStudentClassList(loginUser.getMember_id());

        ClassVO selectedClass = null;
        if (!classList.isEmpty()) {
            selectedClass = classList.get(0);

            if (classId != null) {
                for (ClassVO classInfo : classList) {
                    if (classInfo != null && classInfo.getClassId() == classId.intValue()) {
                        selectedClass = classInfo;
                        break;
                    }
                }
            }
        }

        int selectedClassId = selectedClass != null ? selectedClass.getClassId() : 0;

        List<StudentLearnItemDTO> learnList = selectedClassId > 0
                ? studentLearnService.getStudentLearnList(loginUser.getMember_id(), selectedClassId)
                : List.of();

        List<StudentAssignmentItemDTO> assignmentList = selectedClassId > 0
                ? studentAssignmentService.getStudentAssignmentList(loginUser.getMember_id(), selectedClassId)
                : List.of();

        List<NoticeVO> noticeList = selectedClassId > 0
                ? noticeService.getNoticeList(selectedClassId, loginUser)
                : List.of();

        int totalLearn = learnList.size();
        int inProgressLearn = (int) learnList.stream()
                .filter(item -> "진행중".equals(item.getStatus()))
                .count();
        int completedLearn = (int) learnList.stream()
                .filter(item -> "완료".equals(item.getStatus()))
                .count();

        int progressPercent = totalLearn == 0 ? 0
                : Math.round((float) learnList.stream()
                        .mapToInt(StudentLearnItemDTO::getProgress)
                        .sum() / totalLearn);

        Map<String, Object> learningStats = new LinkedHashMap<>();
        learningStats.put("total", totalLearn);
        learningStats.put("inProgress", inProgressLearn);
        learningStats.put("completed", completedLearn);
        learningStats.put("progressPercent", progressPercent);

        Map<String, Object> currentClass = new LinkedHashMap<>();
        currentClass.put("classId", selectedClassId);
        currentClass.put("className", selectedClass != null ? selectedClass.getClassName() : "참여 중인 클래스가 없습니다.");
        currentClass.put("teacherName", selectedClass != null ? safeString(selectedClass.getTeacherName()) : "");

        model.addAttribute("termLabel", buildTermLabel(selectedClass));
        model.addAttribute("greetingTitle", "안녕하세요, " + safeDisplayName(loginUser, "학생") + "님!");
        model.addAttribute("greetingMessage", "오늘도 즐거운 학습 시간되세요!");

        model.addAttribute("studentClassOptions", classList);
        model.addAttribute("selectedClassId", selectedClassId);

        model.addAttribute("learningStats", learningStats);
        model.addAttribute("recentLearnList", learnList.stream().limit(3).toList());
        model.addAttribute("assignmentList", assignmentList.stream().limit(3).toList());
        model.addAttribute("bulletinList", noticeList.stream().limit(3).toList());
        model.addAttribute("currentClass", currentClass);

        model.addAttribute("learnListUrl", selectedClassId > 0 ? "/student/classes/" + selectedClassId + "/learns" : "/student/classes");
        model.addAttribute("assignmentListUrl", selectedClassId > 0 ? "/student/classes/" + selectedClassId + "/assignments" : "/student/classes");
        model.addAttribute("bulletinListUrl", selectedClassId > 0 ? "/student/classes/" + selectedClassId + "/bulletins" : "/student/classes");
        model.addAttribute("currentClassUrl", selectedClassId > 0 ? "/student/classes/" + selectedClassId : "/student/classes");

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

    MemberVO loginUser = getLoginUser(session);
    if (loginUser == null) {
        return "redirect:/auth/login";
    }

    ClassVO classInfo = classService.getStudentClassDetail(loginUser.getMember_id(), classId);
    if (classInfo == null) {
        return "redirect:/student/classes";
    }

    List<StudentLearnItemDTO> learnList =
            studentLearnService.getStudentLearnList(loginUser.getMember_id(), classId);

    List<StudentAssignmentItemDTO> assignmentList =
            studentAssignmentService.getStudentAssignmentList(loginUser.getMember_id(), classId);

    List<NoticeVO> noticeList = noticeService.getNoticeList(classId, loginUser);
    List<ReportListDTO> reportList = reportService.getStudentReportList(loginUser.getMember_id(), classId, null);

    int progressPercent = learnList.isEmpty()
            ? 0
            : Math.round((float) learnList.stream()
                    .mapToInt(StudentLearnItemDTO::getProgress)
                    .sum() / learnList.size());

    int pendingAssignmentCount = (int) assignmentList.stream()
            .filter(item -> !item.isSubmitted())
            .count();

    ReportListDTO latestReport = reportList.isEmpty() ? null : reportList.get(0);

    List<Map<String, Object>> recentBulletins = new ArrayList<>();
    for (NoticeVO notice : noticeList.stream().limit(5).toList()) {
        Map<String, Object> bulletin = new LinkedHashMap<>();
        bulletin.put("id", notice.getNoticeId());
        bulletin.put("title", safeString(notice.getTitle()));
        bulletin.put("date", formatNoticeDate(notice.getCreatedAt()));
        bulletin.put("important", notice.getConfirmYn() == 1);
        recentBulletins.add(bulletin);
    }

    model.addAttribute("className", classInfo.getClassName());
    model.addAttribute("studentName", safeDisplayName(loginUser, "학생"));
    model.addAttribute("teacherName", safeString(classInfo.getTeacherName()));
    model.addAttribute("weeklyProgress", progressPercent);
    model.addAttribute("pendingAssignmentCount", pendingAssignmentCount);
    model.addAttribute("latestAssignmentTitle",
            assignmentList.stream()
                    .filter(item -> !item.isSubmitted())
                    .map(StudentAssignmentItemDTO::getTitle)
                    .findFirst()
                    .orElse(""));
    model.addAttribute("newReportMessage", latestReport != null ? "도착했어요!" : "없어요");
    model.addAttribute("latestReportTitle", latestReport != null ? safeString(latestReport.getTitle()) : "");
    model.addAttribute("hasReport", latestReport != null);
    model.addAttribute("recentBulletins", recentBulletins);

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

        MemberVO loginUser = getLoginUser(session);
        model.addAttribute("assignmentList",
                loginUser == null ? List.of() : studentAssignmentService.getStudentAssignmentList(loginUser.getMember_id(), classId));

        return "common/layout/studentLayout";
    }

    @GetMapping("/student/classes/{classId}/assignments/{assignmentId}")
    @ResponseBody
    public StudentAssignmentItemDTO studentAssignmentDetail(@PathVariable("classId") int classId,
                                                            @PathVariable("assignmentId") int assignmentId,
                                                            HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        return studentAssignmentService.getStudentAssignmentDetail(loginUser.getMember_id(), classId, assignmentId);
    }

    @PostMapping("/student/classes/{classId}/assignments/{assignmentId}/submit")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitStudentAssignment(@PathVariable("classId") int classId,
                                                                       @PathVariable("assignmentId") int assignmentId,
                                                                       @RequestParam(value = "content", required = false) String content,
                                                                       @RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile,
                                                                       HttpSession session) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            MemberVO loginUser = getLoginUser(session);
            if (loginUser == null) {
                throw new IllegalArgumentException("로그인이 필요합니다.");
            }

            String saveDir = servletContext.getRealPath("/resources/upload/assignment");
            StudentAssignmentItemDTO saved = studentAssignmentService.submitStudentAssignment(
                    loginUser.getMember_id(), classId, assignmentId, content, uploadFile, saveDir);

            result.put("success", true);
            result.put("assignment", saved);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/student/classes/{classId}/assignments/{assignmentId}/download")
    public void downloadStudentAssignmentFile(@PathVariable("classId") int classId,
                                              @PathVariable("assignmentId") int assignmentId,
                                              HttpSession session,
                                              HttpServletResponse response) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        StudentAssignmentItemDTO detail = studentAssignmentService.getStudentAssignmentDetail(loginUser.getMember_id(), classId, assignmentId);
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

    @GetMapping("/student/classes/{classId}/reports")
    public String studentReportPage(@PathVariable("classId") int classId,
                                    @RequestParam(value = "periodFilter", required = false) String periodFilter,
                                    Model model,
                                    HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        int studentId = loginUser.getMember_id();

        ClassVO classInfo = classService.getStudentClassDetail(studentId, classId);
        if (classInfo == null) {
            return "redirect:/student/classes";
        }

        List<ReportListDTO> reportList =
                reportService.getStudentReportList(studentId, classId, periodFilter);

        model.addAttribute("pageTitle", "리포트 확인");
        model.addAttribute("currentUri", "/student/classes/" + classId + "/reports");
        setStudentClassDetailBase(model, classId, session);

        model.addAttribute("classId", classId);
        model.addAttribute("classInfo", classInfo);
        model.addAttribute("periodFilter", periodFilter);
        model.addAttribute("reportList", reportList);

        model.addAttribute("contentPage", "/WEB-INF/views/student/report/listContent.jsp");
        return "common/layout/studentLayout";
    }

    @GetMapping("/student/classes/{classId}/reports/{reportId}")
    @ResponseBody
    public ReportDetailDTO getStudentReportDetail(@PathVariable("classId") int classId,
                                                  @PathVariable("reportId") int reportId,
                                                  HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        int studentId = loginUser.getMember_id();

        return reportService.getStudentReportDetail(studentId, classId, reportId);
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

    private String buildTermLabel(ClassVO classInfo) {
        if (classInfo == null) {
            return "2026 1학기";
        }
        if (classInfo.getYear() > 0 && classInfo.getSemester() > 0) {
            return classInfo.getYear() + " " + classInfo.getSemester() + "학기";
        }
        return classInfo.getYearLabel() != null && !classInfo.getYearLabel().isBlank() ? classInfo.getYearLabel() : "2026 1학기";
    }

    private String safeDisplayName(MemberVO loginUser, String fallback) {
        if (loginUser == null) {
            return fallback;
        }
        String name = loginUser.getName();
        return (name == null || name.isBlank()) ? fallback : name.trim();
    }

    private String safeString(String value) {
        return value == null ? "" : value;
    }

    private String formatNoticeDate(java.util.Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy.MM.dd").format(date);
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