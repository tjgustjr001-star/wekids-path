package com.spring.controller.parent;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.spring.dto.parent.ParentChildClassOptionDTO;
import com.spring.dto.report.ReportDetailDTO;
import com.spring.dto.report.ReportListDTO;
import com.spring.dto.student.StudentAssignmentItemDTO;
import com.spring.dto.student.StudentLearnItemDTO;
import com.spring.service.ClassService;
import com.spring.service.NoticeService;
import com.spring.service.ParentAssignmentService;
import com.spring.service.ReportService;
import com.spring.service.SettingsService;
import com.spring.service.StudentLearnService;

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
    private StudentLearnService studentLearnService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/parent")
    public String parentHome(Model model,
                             @RequestParam(value = "selection", required = false) String selection,
                             HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "학부모 홈");
        model.addAttribute("currentUri", "/parent");
        model.addAttribute("contentPage", "/WEB-INF/views/parent/homeContent.jsp");

        setParentLayoutBase(model);

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        List<ParentChildVO> children = settingsService.getLinkedChildren(loginUser.getMember_id());
        List<ClassVO> parentClassList = classService.getParentClassList(loginUser.getMember_id());

        List<ParentChildClassOptionDTO> childOptions =
                classService.getParentChildClassOptions(loginUser.getMember_id());

        ParentChildClassOptionDTO selectedOption =
                findSelectedParentChildOption(childOptions, selection);

        int selectedChildId = selectedOption != null ? selectedOption.getStudentId() : 0;
        int selectedClassId = selectedOption != null ? selectedOption.getClassId() : 0;
        String selectedLabel = selectedOption != null ? selectedOption.getLabel() : "";

        ParentChildVO selectedChild = findParentChild(children, selectedChildId);
        ClassVO selectedClass = findClassById(parentClassList, selectedClassId);

        List<StudentAssignmentItemDTO> assignmentList = (selectedChildId > 0 && selectedClassId > 0)
                ? parentAssignmentService.getParentAssignmentList(
                        loginUser.getMember_id(), selectedClassId, selectedChildId)
                : List.of();

        List<NoticeVO> noticeList = selectedClassId > 0
                ? noticeService.getNoticeList(selectedClassId, loginUser)
                : List.of();

        int assignmentTotal = assignmentList.size();
        int assignmentSubmitted = (int) assignmentList.stream()
                .filter(this::isParentSubmittedAssignment)
                .count();
        int assignmentPending = Math.max(0, assignmentTotal - assignmentSubmitted);
        int assignmentRate = assignmentTotal == 0
                ? 0
                : Math.round((float) assignmentSubmitted * 100 / assignmentTotal);

        model.addAttribute("termLabel", buildTermLabel(selectedClass));
        model.addAttribute("parentName", safeDisplayName(loginUser, "학부모"));
        model.addAttribute("children", children);
        model.addAttribute("childOptions", childOptions);
        model.addAttribute("selectedChildId", selectedChildId);
        model.addAttribute("assignmentTotal", assignmentTotal);
        model.addAttribute("assignmentSubmitted", assignmentSubmitted);
        model.addAttribute("assignmentPending", assignmentPending);
        model.addAttribute("assignmentRate", assignmentRate);
        model.addAttribute("recentAssignmentList", assignmentList.stream().limit(3).toList());
        model.addAttribute("parentNotices", noticeList.stream().limit(3).toList());
        model.addAttribute("selectedClassId", selectedClassId);
        model.addAttribute("selectedSelection", selectedOption != null ? selectedOption.getValue() : "");
        model.addAttribute("selectedChildClassLabel", selectedLabel);
        model.addAttribute("selectedClassName",
                selectedClass != null ? selectedClass.getClassName() : "참여 중인 클래스가 없습니다.");
        model.addAttribute("selectedChildDetailUrl",
                selectedChildId > 0 ? "/parent/children/" + selectedChildId : "/parent/children");
        model.addAttribute("assignmentListUrl",
                selectedClassId > 0
                        ? "/parent/classes/" + selectedClassId + "/assignments?childId=" + selectedChildId
                        : "/parent/classes");
        model.addAttribute("bulletinListUrl",
                selectedClassId > 0 ? "/parent/classes/" + selectedClassId + "/bulletins" : "/parent/classes");
        model.addAttribute("currentClassUrl",
                selectedClassId > 0 ? "/parent/classes/" + selectedClassId : "/parent/classes");

        if (selectedChild != null) {
            model.addAttribute("selectedChild", selectedChild);
        }

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
                              @RequestParam(value = "childId", required = false) Integer childId,
                              Model model,
                              HttpSession session) throws Exception {
    model.addAttribute("pageTitle", "클래스 홈");
    model.addAttribute("currentUri", "/parent/classes/" + classId);
    model.addAttribute("contentPage", "/WEB-INF/views/parent/class/homeContent.jsp");

    setParentLayoutBase(model);
    setParentClassDetailBase(model, classId, session);

    MemberVO loginUser = getLoginUser(session);
    if (loginUser == null) {
        return "redirect:/auth/login";
    }

    ClassVO classInfo = classService.getParentClassDetail(loginUser.getMember_id(), classId);
    if (classInfo == null) {
        return "redirect:/parent/classes";
    }

    List<ParentChildVO> childList = reportService.getParentReportChildren(loginUser.getMember_id(), classId);

    ParentChildVO selectedChild = null;
    if (childId != null) {
        for (ParentChildVO child : childList) {
            if (child != null && child.getStudentId() == childId.intValue()) {
                selectedChild = child;
                break;
            }
        }
    }
    if (selectedChild == null && !childList.isEmpty()) {
        selectedChild = childList.get(0);
    }

    Integer selectedChildId = selectedChild != null ? selectedChild.getStudentId() : null;

    List<StudentLearnItemDTO> learnList = selectedChildId != null
            ? studentLearnService.getStudentLearnList(selectedChildId, classId)
            : List.of();

    List<StudentAssignmentItemDTO> assignmentList = selectedChildId != null
            ? parentAssignmentService.getParentAssignmentList(loginUser.getMember_id(), classId, selectedChildId)
            : List.of();

    List<ReportListDTO> reportList =
            reportService.getParentReportList(loginUser.getMember_id(), classId, selectedChildId, null);

    List<NoticeVO> noticeList = noticeService.getNoticeList(classId, loginUser);

    int progressPercent = learnList.isEmpty()
            ? 0
            : Math.round((float) learnList.stream()
                    .mapToInt(StudentLearnItemDTO::getProgress)
                    .sum() / learnList.size());

    int pendingAssignmentCount = (int) assignmentList.stream()
            .filter(item -> !item.isSubmitted())
            .count();

    StudentAssignmentItemDTO pendingAssignment = assignmentList.stream()
            .filter(item -> !item.isSubmitted())
            .findFirst()
            .orElse(null);

    ReportListDTO latestReport = reportList.isEmpty() ? null : reportList.get(0);
    boolean hasUnreadReport = latestReport != null && latestReport.getParentViewCount() != null
            && latestReport.getParentViewCount().intValue() == 0;

    NoticeVO recentNotice = noticeList.isEmpty() ? null : noticeList.get(0);

    List<Map<String, Object>> notices = new ArrayList<>();
    for (NoticeVO notice : noticeList.stream().limit(3).toList()) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("title", safeString(notice.getTitle()));
        item.put("date", formatNoticeDate(notice.getCreatedAt()));
        item.put("important", notice.getConfirmYn() == 1);
        notices.add(item);
    }

    model.addAttribute("className", classInfo.getClassName());
    model.addAttribute("loginUserName", safeDisplayName(loginUser, "학부모"));
    model.addAttribute("teacherName", safeString(classInfo.getTeacherName()));
    model.addAttribute("childName", selectedChild != null ? safeString(selectedChild.getStudentName()) : "");
    model.addAttribute("selectedChildId", selectedChildId);
    model.addAttribute("progressPercent", progressPercent);
    model.addAttribute("remainPercent", Math.max(0, 100 - progressPercent));
    model.addAttribute("pendingAssignmentCount", pendingAssignmentCount);
    model.addAttribute("pendingAssignmentTitle", pendingAssignment != null ? safeString(pendingAssignment.getTitle()) : "미제출 과제가 없습니다.");
    model.addAttribute("pendingAssignmentDeadline", pendingAssignment != null ? safeString(pendingAssignment.getDeadline()) : "");
    model.addAttribute("hasUnreadReport", hasUnreadReport);
    model.addAttribute("recentMessageTime", recentNotice != null ? formatNoticeDate(recentNotice.getCreatedAt()) : "");
    model.addAttribute("recentMessage", recentNotice != null ? safeString(recentNotice.getContent()) : "등록된 최근 메시지가 없습니다.");
    model.addAttribute("isNoticeRead", recentNotice != null && !recentNotice.isRequiredUnread());
    model.addAttribute("notices", notices);

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
                                       @RequestParam(value = "selection", required = false) String selection,
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

        List<ParentAssignmentChildDTO> children =
                parentAssignmentService.getParentAssignmentChildren(loginUser.getMember_id(), classId);
        model.addAttribute("assignmentChildren", children);

        Integer selectedChildId = null;
        ParentAssignmentChildDTO selectedChild = null;

        if (childId != null) {
            for (ParentAssignmentChildDTO child : children) {
                if (child != null && child.getStudentId() == childId.intValue()) {
                    selectedChildId = childId;
                    selectedChild = child;
                    break;
                }
            }
        }

        if (selectedChild == null && selection != null && !selection.isBlank()) {
            int parsedChildId = parseStudentIdFromSelection(selection);
            if (parsedChildId > 0) {
                for (ParentAssignmentChildDTO child : children) {
                    if (child != null && child.getStudentId() == parsedChildId) {
                        selectedChildId = parsedChildId;
                        selectedChild = child;
                        break;
                    }
                }
            }
        }

        if (selectedChild == null && !children.isEmpty()) {
            selectedChild = children.get(0);
            selectedChildId = selectedChild.getStudentId();
        }

        List<StudentAssignmentItemDTO> assignmentList = List.of();
        if (selectedChildId != null) {
            assignmentList = parentAssignmentService.getParentAssignmentList(
                    loginUser.getMember_id(), classId, selectedChildId);
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

        if (detail == null || !detail.isCanDownload()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File file = new File(detail.getUploadPath());
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/octet-stream");
        String encodedName = URLEncoder.encode(detail.getAttachedFile(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
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

        List<ParentChildVO> childList = reportService.getParentReportChildren(parentId, classId);

        Integer safeStudentId = studentId;
        if (safeStudentId != null) {
            boolean matched = false;
            for (ParentChildVO child : childList) {
                if (child != null && child.getStudentId() == safeStudentId.intValue()) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                safeStudentId = null;
            }
        }

        List<ReportListDTO> reportList =
                reportService.getParentReportList(parentId, classId, safeStudentId, periodFilter);

        model.addAttribute("pageTitle", "자녀 리포트 확인");
        model.addAttribute("currentUri", "/parent/classes/" + classId + "/reports");
        setParentClassDetailBase(model, classId, session);

        model.addAttribute("classId", classId);
        model.addAttribute("selectedStudentId", safeStudentId);
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
                                                 @RequestParam(value = "studentId", required = false) Integer studentId,
                                                 HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        int parentId = loginUser.getMember_id();

        return reportService.getParentReportDetail(parentId, studentId, classId, reportId);
    }

    private MemberVO getLoginUser(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser instanceof MemberVO) {
            return (MemberVO) loginUser;
        }

        Object principal = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication() != null
                        ? org.springframework.security.core.context.SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal()
                        : null;

        if (principal instanceof com.spring.security.CustomUser) {
            return ((com.spring.security.CustomUser) principal).getMember();
        }

        return null;
    }

    private ParentChildVO findParentChild(List<ParentChildVO> children, int studentId) {
        if (children == null || studentId <= 0) {
            return null;
        }

        for (ParentChildVO child : children) {
            if (child != null && child.getStudentId() == studentId) {
                return child;
            }
        }
        return null;
    }

    private ClassVO findClassById(List<ClassVO> classList, int classId) {
        if (classList == null || classId <= 0) {
            return null;
        }

        for (ClassVO classInfo : classList) {
            if (classInfo != null && classInfo.getClassId() == classId) {
                return classInfo;
            }
        }
        return null;
    }

    private ParentChildClassOptionDTO findSelectedParentChildOption(
            List<ParentChildClassOptionDTO> options, String selection) {

        if (options == null || options.isEmpty()) {
            return null;
        }

        if (selection != null && !selection.isBlank()) {
            for (ParentChildClassOptionDTO option : options) {
                if (option != null && selection.equals(option.getValue())) {
                    return option;
                }
            }
        }

        return options.get(0);
    }

    private int parseStudentIdFromSelection(String selection) {
        if (selection == null || selection.isBlank()) {
            return 0;
        }

        String[] tokens = selection.split(":");
        if (tokens.length == 0) {
            return 0;
        }

        try {
            return Integer.parseInt(tokens[0]);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private int parseInt(Object value) {
        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private boolean isParentSubmittedAssignment(StudentAssignmentItemDTO assignment) {
        if (assignment == null || assignment.getStatus() == null) {
            return false;
        }
        String status = assignment.getStatus().trim();
        return "제출완료".equals(status)
                || "확인완료".equals(status)
                || "늦은제출".equals(status);
    }

    private String buildTermLabel(ClassVO classInfo) {
        if (classInfo == null) {
            return "2026 1학기";
        }
        if (classInfo.getYear() > 0 && classInfo.getSemester() > 0) {
            return classInfo.getYear() + " " + classInfo.getSemester() + "학기";
        }
        return classInfo.getYearLabel() != null && !classInfo.getYearLabel().isBlank()
                ? classInfo.getYearLabel()
                : "2026 1학기";
    }

    private String safeDisplayName(MemberVO loginUser, String fallback) {
        if (loginUser == null || loginUser.getName() == null || loginUser.getName().isBlank()) {
            return fallback;
        }
        return loginUser.getName().trim();
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
}