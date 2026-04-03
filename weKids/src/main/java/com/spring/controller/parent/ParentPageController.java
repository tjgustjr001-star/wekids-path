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

import jakarta.servlet.ServletContext;
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

    @Autowired
    private ServletContext servletContext;

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
                                    @RequestParam(value = "classId", required = false) Integer classId,
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
        List<ClassVO> childClassList = settingsService.getChildClassList(loginUser.getMember_id(), childId);

        Integer selectedClassId = classId;
        if ((selectedClassId == null || selectedClassId.intValue() <= 0) && childClassList != null && !childClassList.isEmpty()) {
            selectedClassId = childClassList.get(0).getClassId();
        }

        ParentChildVO child = settingsService.getChildDetail(loginUser.getMember_id(), childId, selectedClassId);

        if (child == null) {
            model.addAttribute("childList", childList);
            model.addAttribute("msg", "not_found_child");
            model.addAttribute("contentPage", "/WEB-INF/views/settings/childLinkParent.jsp");
            return "common/layout/parentLayout";
        }

        model.addAttribute("childList", childList);
        model.addAttribute("childClassList", childClassList);
        model.addAttribute("selectedClassId", selectedClassId);
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

    @GetMapping("/parent/classes/{classId}/assignments/{assignmentId}")
    @ResponseBody
    public StudentAssignmentItemDTO parentAssignmentDetail(@PathVariable("classId") int classId,
                                                            @PathVariable("assignmentId") int assignmentId,
                                                            @RequestParam("childId") int childId,
                                                            HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        StudentAssignmentItemDTO detail = parentAssignmentService.getParentAssignmentDetail(
                loginUser.getMember_id(), classId, childId, assignmentId);

        if (detail == null) {
            throw new IllegalArgumentException("과제를 찾을 수 없습니다.");
        }
        return detail;
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

        if (detail == null || detail.getAttachedFile() == null || detail.getAttachedFile().isBlank()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File file = resolveAssignmentFile(detail.getUploadPath(), detail.getAttachedFile());
        if (file == null || !file.exists() || !file.isFile()) {
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
        setParentLayoutBase(model);
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

    private File resolveAssignmentFile(String uploadPath, String originalFileName) {
        if (uploadPath != null && !uploadPath.isBlank()) {
            File direct = new File(uploadPath);
            if (direct.exists() && direct.isFile()) {
                return direct;
            }

            String normalized = uploadPath.replace("\\", File.separator).replace("/", File.separator);
            File normalizedFile = new File(normalized);
            if (normalizedFile.exists() && normalizedFile.isFile()) {
                return normalizedFile;
            }

            String baseName = new File(normalized).getName();
            String uploadDir = servletContext.getRealPath("/resources/upload/assignment");
            if (uploadDir != null && baseName != null && !baseName.isBlank()) {
                File candidate = new File(uploadDir, baseName);
                if (candidate.exists() && candidate.isFile()) {
                    return candidate;
                }
            }
        }
        return null;
    }
}
