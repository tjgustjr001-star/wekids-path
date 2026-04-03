package com.spring.controller.teacher;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.dto.ClassVO;
import com.spring.dto.MemberVO;
import com.spring.dto.NoticeVO;
import com.spring.dto.report.ReportDetailDTO;
import com.spring.dto.report.ReportGenerateRequestDTO;
import com.spring.dto.report.ReportListDTO;
import com.spring.dto.teacher.TeacherAssignmentDetailDTO;
import com.spring.dto.teacher.TeacherAssignmentManageDTO;
import com.spring.dto.teacher.TeacherAssignmentSaveDTO;
import com.spring.dto.teacher.TeacherAssignmentStudentWorkDTO;
import com.spring.dto.teacher.TeacherClassCreateDTO;
import com.spring.dto.teacher.TeacherClassManageDTO;
import com.spring.dto.teacher.TeacherLearnDifficultyDTO;
import com.spring.dto.teacher.TeacherLearnManageDTO;
import com.spring.dto.teacher.TeacherLearnProgressDTO;
import com.spring.dto.teacher.TeacherLearnSaveDTO;
import com.spring.dto.teacher.TeacherStudentManageDTO;
import com.spring.dto.teacher.TeacherStudentObservationSaveDTO;
import com.spring.security.CustomUser;
import com.spring.service.ClassService;
import com.spring.service.NoticeService;
import com.spring.service.ReportService;
import com.spring.service.TeacherAssignmentService;
import com.spring.service.TeacherLearnService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
public class TeacherPageController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ClassService classService;

    @Autowired
    private TeacherLearnService teacherLearnService;

    @Autowired
    private TeacherAssignmentService teacherAssignmentService;
    
    @Autowired
    private ReportService reportService;

    @Autowired
    private ServletContext servletContext;
    
    @GetMapping("/teacher")
    public String teacherHome(Model model,
                              HttpServletRequest request,
                              HttpSession session,
                              @RequestParam(value = "classId", required = false) Integer classId) throws Exception {
        model.addAttribute("pageTitle", "교사 홈");
        model.addAttribute("currentUri", "/teacher");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/homeContent.jsp");

        setTeacherLayoutBase(model, request);

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        List<ClassVO> teacherClassList = classService.getTeacherClassList(loginUser.getMember_id());
        ClassVO selectedClass = teacherClassList.isEmpty() ? null : teacherClassList.get(0);
        if (classId != null) {
            for (ClassVO classInfo : teacherClassList) {
                if (classInfo != null && classInfo.getClassId() == classId.intValue()) {
                    selectedClass = classInfo;
                    break;
                }
            }
        }

        int selectedClassId = selectedClass != null ? selectedClass.getClassId() : 0;
        List<TeacherAssignmentManageDTO> assignmentList = selectedClassId > 0
                ? teacherAssignmentService.getTeacherAssignmentList(loginUser.getMember_id(), selectedClassId, 0)
                : List.of();
        List<NoticeVO> noticeList = selectedClassId > 0
                ? noticeService.getNoticeList(selectedClassId, loginUser)
                : List.of();

        int unsubmittedCount = assignmentList.stream().mapToInt(item -> Math.max(0, item.getTotalCount() - item.getSubmitCount())).sum();
        int approachingCount = (int) assignmentList.stream().filter(item -> !"마감".equals(item.getStatus())).limit(3).count();
        int feedbackNeededCount = assignmentList.stream().mapToInt(TeacherAssignmentManageDTO::getNeedFeedback).sum();

        Map<String, Object> taskSummary = new LinkedHashMap<>();
        taskSummary.put("unsubmittedCount", unsubmittedCount);
        taskSummary.put("approachingCount", approachingCount);
        taskSummary.put("feedbackNeededCount", feedbackNeededCount);

        List<Map<String, Object>> deadlineAssignmentList = new ArrayList<>();
        for (TeacherAssignmentManageDTO item : assignmentList.stream().limit(3).toList()) {
            Map<String, Object> map = new LinkedHashMap<>();
            int pendingCount = Math.max(0, item.getTotalCount() - item.getSubmitCount());
            map.put("subject", item.getSubject());
            map.put("title", item.getTitle());
            map.put("deadlineLabel", item.getDeadline());
            map.put("unsubmittedCount", pendingCount);
            map.put("submittedCount", item.getSubmitCount());
            map.put("totalCount", item.getTotalCount());
            map.put("progressPercent", item.getProgressPercent());
            deadlineAssignmentList.add(map);
        }

        model.addAttribute("currentSemesterLabel", buildTermLabel(selectedClass));
        model.addAttribute("userName", safeDisplayName(loginUser, "교사"));
        model.addAttribute("teacherClassOptions", teacherClassList);
        model.addAttribute("selectedClassId", selectedClassId);
        model.addAttribute("selectedClassName", selectedClass != null ? selectedClass.getClassName() : "운영 중인 클래스가 없습니다.");
        model.addAttribute("taskSummary", taskSummary);
        model.addAttribute("deadlineAssignmentList", deadlineAssignmentList);
        model.addAttribute("bulletinList", noticeList.stream().limit(3).toList());
        model.addAttribute("uncheckedParentCount", noticeList.stream().filter(NoticeVO::isRequiredUnread).count());
        model.addAttribute("teacherAssignmentUrl", selectedClassId > 0 ? "/teacher/classes/" + selectedClassId + "/assignments" : "/teacher/classes");
        model.addAttribute("teacherBulletinUrl", selectedClassId > 0 ? "/teacher/classes/" + selectedClassId + "/bulletins" : "/teacher/classes");
        model.addAttribute("teacherClassEnterUrl", selectedClassId > 0 ? "/teacher/classes/" + selectedClassId : "/teacher/classes");

        return "common/layout/teacherLayout";
    }

    @GetMapping("/teacher/classes")
    public String teacherClassList(Model model, HttpServletRequest request, HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "클래스 목록");
        model.addAttribute("currentUri", "/teacher/classes");
        model.addAttribute("contentPage", "/WEB-INF/views/class/listContent.jsp");

        setTeacherLayoutBase(model, request);

        MemberVO loginUser = getLoginUser(session);

        model.addAttribute("roleType", "teacher");
        model.addAttribute("showJoinButton", false);
        model.addAttribute("showCreateButton", true);

        if (loginUser != null) {
            model.addAttribute("classList", classService.getTeacherClassList(loginUser.getMember_id()));
        } else {
            model.addAttribute("classList", List.of());
        }

        return "common/layout/teacherLayout";
    }

    @GetMapping("/teacher/classes/new")
    public String teacherClassCreateForm(Model model, HttpServletRequest request) {
        model.addAttribute("pageTitle", "클래스 생성");
        model.addAttribute("currentUri", "/teacher/classes/new");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/class/createContent.jsp");

        setTeacherLayoutBase(model, request);

        return "common/layout/teacherLayout";
    }

    @PostMapping("/teacher/classes/new")
    public String teacherClassCreateSubmit(@ModelAttribute TeacherClassCreateDTO dto,
                                           HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        classService.createClass(loginUser.getMember_id(), dto);
        return "redirect:/teacher/classes";
    }

    
@GetMapping("/teacher/classes/{classId}")
public String teacherClassHome(@PathVariable("classId") int classId,
                               Model model,
                               HttpServletRequest request,
                               HttpSession session) throws Exception {
    model.addAttribute("pageTitle", "클래스 홈");
    model.addAttribute("currentUri", "/teacher/classes/" + classId);
    model.addAttribute("contentPage", "/WEB-INF/views/teacher/class/homeContent.jsp");

    MemberVO loginUser = getLoginUser(session);
    if (loginUser == null) {
        return "redirect:/auth/login";
    }

    ClassVO classInfo = classService.getTeacherClassDetail(loginUser.getMember_id(), classId);
    if (classInfo == null) {
        return "redirect:/teacher/classes";
    }

    setTeacherClassDetailLayout(model, request, classId, classInfo.getClassName());

    List<TeacherStudentManageDTO> studentList =
            classService.getTeacherStudentManageList(loginUser.getMember_id(), classId);

    List<TeacherLearnManageDTO> learnList =
            teacherLearnService.getTeacherLearnList(loginUser.getMember_id(), classId, 0);

    List<TeacherAssignmentManageDTO> assignmentList =
            teacherAssignmentService.getTeacherAssignmentList(loginUser.getMember_id(), classId, 0);

    List<NoticeVO> noticeList = noticeService.getNoticeList(classId, loginUser);
    List<ReportListDTO> reportList = reportService.getTeacherReportList(loginUser.getMember_id(), classId);

    int studentCount = studentList.size();

    int avgProgressPercent = learnList.isEmpty()
            ? 0
            : Math.round((float) learnList.stream()
                    .mapToInt(TeacherLearnManageDTO::getCompletionRate)
                    .sum() / learnList.size());

    int missingAssignmentStudentCount = assignmentList.stream()
            .mapToInt(item -> Math.max(0, item.getTotalCount() - item.getSubmitCount()))
            .sum();

    int ungradedAssignmentCount = assignmentList.stream()
            .mapToInt(TeacherAssignmentManageDTO::getNeedFeedback)
            .sum();

    List<Map<String, Object>> recentUpdates = new ArrayList<>();
    for (ReportListDTO report : reportList.stream().limit(3).toList()) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("writerInitial", "선");
        item.put("writerName", safeDisplayName(loginUser, "교사") + " 선생님 (나)");
        item.put("timeText", report.getCreatedAt());
        item.put("content", "[리포트] " + safeString(report.getTitle()));
        recentUpdates.add(item);
    }
    if (recentUpdates.isEmpty()) {
        for (NoticeVO notice : noticeList.stream().limit(3).toList()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("writerInitial", "선");
            item.put("writerName", safeDisplayName(loginUser, "교사") + " 선생님 (나)");
            item.put("timeText", formatNoticeDateTime(notice.getCreatedAt()));
            item.put("content", safeString(notice.getContent()));
            recentUpdates.add(item);
        }
    }

    List<Map<String, Object>> classBulletins = new ArrayList<>();
    for (NoticeVO notice : noticeList.stream().limit(5).toList()) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", notice.getNoticeId());
        item.put("title", safeString(notice.getTitle()));
        item.put("date", formatNoticeDate(notice.getCreatedAt()));
        item.put("important", notice.getConfirmYn() == 1);
        classBulletins.add(item);
    }

    model.addAttribute("className", classInfo.getClassName());
    model.addAttribute("teacherName", safeDisplayName(loginUser, "교사"));
    model.addAttribute("studentCount", studentCount);
    model.addAttribute("avgProgressPercent", avgProgressPercent);
    model.addAttribute("missingAssignmentStudentCount", missingAssignmentStudentCount);
    model.addAttribute("ungradedAssignmentCount", ungradedAssignmentCount);
    model.addAttribute("recentUpdates", recentUpdates);
    model.addAttribute("classBulletins", classBulletins);

    return "common/layout/teacherLayout";
}

    @GetMapping("/teacher/classes/{classId}/manage")
    public String teacherClassManage(@PathVariable("classId") int classId,
                                     Model model,
                                     HttpServletRequest request,
                                     HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);

        model.addAttribute("pageTitle", "클래스 설정");
        model.addAttribute("currentUri", "/teacher/classes/" + classId + "/manage");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/class/manageContent.jsp");

        String className = getTeacherClassName(classId, session);
        setTeacherClassDetailLayout(model, request, classId, className);

        if (loginUser != null) {
            ClassVO classInfo = classService.getTeacherClassDetail(loginUser.getMember_id(), classId);

            if (classInfo != null) {
                model.addAttribute("className", classInfo.getClassName());
                model.addAttribute("classStatus", classInfo.getClassStatus());
                model.addAttribute("grade", classInfo.getGrade());
                model.addAttribute("classNo", classInfo.getClassNo());
                model.addAttribute("description", classInfo.getDescription());
                model.addAttribute("inviteCode", classInfo.getInviteCode());
                model.addAttribute("defaultDueTime", classInfo.getDefaultDueTime());
                model.addAttribute("allowSubmissionModifyYn", classInfo.getAllowSubmissionModifyYn());
            }

            model.addAttribute("studentCount",
                    classService.getTeacherClassStudentCount(loginUser.getMember_id(), classId));
        }

        return "common/layout/teacherLayout";
    }
    
    @PostMapping("/teacher/classes/{classId}/manage")
    public String teacherClassManageSubmit(@PathVariable("classId") int classId,
                                           @ModelAttribute TeacherClassManageDTO dto,
                                           HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        classService.updateTeacherClassBasicSettings(loginUser.getMember_id(), classId, dto);
        return "redirect:/teacher/classes/" + classId + "/manage";
    }
    
    @PostMapping("/teacher/classes/{classId}/assignment-settings")
    public String teacherAssignmentSettingsSubmit(@PathVariable("classId") int classId,
                                                  @ModelAttribute TeacherClassManageDTO dto,
                                                  HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        classService.updateTeacherAssignmentSettings(loginUser.getMember_id(), classId, dto);
        return "redirect:/teacher/classes/" + classId + "/manage";
    }
    
    @PostMapping("/teacher/classes/{classId}/invite-code")
    public String teacherInviteCodeRegenerate(@PathVariable("classId") int classId,
                                              HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        classService.regenerateInviteCode(loginUser.getMember_id(), classId);
        return "redirect:/teacher/classes/" + classId + "/manage";
    }
    
    @PostMapping("/teacher/classes/{classId}/delete")
    public String teacherClassDelete(@PathVariable("classId") int classId,
                                     HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        classService.archiveTeacherClass(loginUser.getMember_id(), classId);
        return "redirect:/teacher/classes";
    }
    
    @GetMapping("/teacher/classes/{classId}/students")
    public String teacherStudentList(@PathVariable("classId") int classId,
                                     Model model,
                                     HttpServletRequest request,
                                     HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        int teacherId = loginUser.getMember_id();

        ClassVO classInfo = classService.getTeacherClassDetail(teacherId, classId);
        if (classInfo == null) {
            return "redirect:/teacher/classes";
        }

        List<TeacherStudentManageDTO> studentList =
                classService.getTeacherStudentManageList(teacherId, classId);

        int totalStudentCount = studentList.size();

        long linkedParentCount = studentList.stream()
                .filter(student -> student.getParentLinked() == 1)
                .count();

        int linkedParentRate = totalStudentCount == 0
                ? 0
                : (int) Math.round((linkedParentCount * 100.0) / totalStudentCount);

        model.addAttribute("pageTitle", "학생 관리");
        model.addAttribute("currentUri", "/teacher/classes/" + classId + "/students");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/student/listContent.jsp");

        setTeacherClassDetailLayout(model, request, classId, classInfo.getClassName());

        model.addAttribute("classInfo", classInfo);
        model.addAttribute("studentList", studentList);

        model.addAttribute("totalStudentCount", totalStudentCount);
        model.addAttribute("linkedParentCount", linkedParentCount);
        model.addAttribute("linkedParentRate", linkedParentRate);

        model.addAttribute("inviteCode", classInfo.getInviteCode());
        model.addAttribute("inviteLink",
                request.getScheme() + "://" +
                request.getServerName() +
                ((request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort()) +
                request.getContextPath() +
                "/student/classes/join?inviteCode=" + classInfo.getInviteCode());

        return "common/layout/teacherLayout";
    }


    @PostMapping("/teacher/classes/students/observation")
    @ResponseBody
    public Map<String, Object> saveStudentObservation(@RequestBody TeacherStudentObservationSaveDTO dto,
                                                      HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        classService.saveTeacherStudentObservation(loginUser.getMember_id(), dto);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("message", "지도 메모 및 관찰 태그가 저장되었습니다.");
        return result;
    }

    @PostMapping("/teacher/classes/students/remove")
    @ResponseBody
    public Map<String, Object> removeStudentFromClass(@RequestBody Map<String, Integer> requestBody,
                                                      HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Integer studentClassId = requestBody.get("studentClassId");
        if (studentClassId == null) {
            throw new IllegalArgumentException("studentClassId가 필요합니다.");
        }

        classService.removeStudentFromClass(loginUser.getMember_id(), studentClassId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("message", "학생이 클래스에서 제외되었습니다.");
        return result;
    }
    
    @GetMapping("/teacher/classes/{classId}/bulletins")
    public String teacherBulletinList(@PathVariable("classId") int classId,
                                      Model model,
                                      HttpServletRequest request,
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
        model.addAttribute("currentUri", "/teacher/classes/" + classId + "/bulletins");
        model.addAttribute("contentPage", "/WEB-INF/views/notice/list.jsp");

        model.addAttribute("isTeacher", true);
        model.addAttribute("isStudentOrParent", false);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("parentCount", parentCount);

        setTeacherClassDetailLayout(model, request, classId, getTeacherClassName(classId, session));

        return "common/layout/teacherLayout";
    }

    @GetMapping("/teacher/classes/{classId}/learns")
    public String teacherLearnList(@PathVariable("classId") int classId,
                                   Model model,
                                   HttpServletRequest request,
                                   HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("pageTitle", "학습 관리");
        model.addAttribute("currentUri", "/teacher/classes/" + classId + "/learns");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/learn/listContent.jsp");
        model.addAttribute("classId", classId);
        model.addAttribute("trashMode", false);
        model.addAttribute("learnList", teacherLearnService.getTeacherLearnList(loginUser.getMember_id(), classId, 0));

        setTeacherClassDetailLayout(model, request, classId, getTeacherClassName(classId, session));
        return "common/layout/teacherLayout";
    }

    @GetMapping("/teacher/classes/{classId}/learns/trash")
    public String teacherLearnTrash(@PathVariable("classId") int classId,
                                    Model model,
                                    HttpServletRequest request,
                                    HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("pageTitle", "학습 휴지통");
        model.addAttribute("currentUri", "/teacher/classes/" + classId + "/learns/trash");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/learn/listContent.jsp");
        model.addAttribute("classId", classId);
        model.addAttribute("trashMode", true);
        model.addAttribute("learnList", teacherLearnService.getTeacherLearnList(loginUser.getMember_id(), classId, 1));

        setTeacherClassDetailLayout(model, request, classId, getTeacherClassName(classId, session));
        return "common/layout/teacherLayout";
    }
    
    @PostMapping("/teacher/classes/{classId}/learns/new")
    public String registTeacherLearn(@PathVariable("classId") int classId,
                                     @ModelAttribute TeacherLearnSaveDTO dto,
                                     HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        teacherLearnService.registTeacherLearn(loginUser.getMember_id(), classId, dto);
        return "redirect:/teacher/classes/" + classId + "/learns";
    }
    
    @PostMapping("/teacher/classes/{classId}/learns/{learnId}/edit")
    public String modifyTeacherLearn(@PathVariable("classId") int classId,
                                     @PathVariable("learnId") int learnId,
                                     @ModelAttribute TeacherLearnSaveDTO dto,
                                     HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        teacherLearnService.modifyTeacherLearn(loginUser.getMember_id(), classId, learnId, dto);
        return "redirect:/teacher/classes/" + classId + "/learns";
    }
    
    @PostMapping("/teacher/classes/{classId}/learns/{learnId}/difficulties")
    @ResponseBody
    public List<TeacherLearnDifficultyDTO> teacherLearnDifficultyList(@PathVariable("classId") int classId,
                                                                      @PathVariable("learnId") int learnId,
                                                                      HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        return teacherLearnService.getTeacherLearnDifficultyList(loginUser.getMember_id(), classId, learnId);
    }
    
    @PostMapping("/teacher/classes/{classId}/learns/{learnId}/progress")
    @ResponseBody
    public List<TeacherLearnProgressDTO> teacherLearnProgressList(@PathVariable("classId") int classId,
                                                                  @PathVariable("learnId") int learnId,
                                                                  HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        return teacherLearnService.getTeacherLearnProgressList(loginUser.getMember_id(), classId, learnId);
    }
    
	/* 휴지통으로 */
    @PostMapping("/teacher/classes/{classId}/learns/{learnId}/delete")
    public String deleteTeacherLearn(@PathVariable("classId") int classId,
                                     @PathVariable("learnId") int learnId,
                                     HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        teacherLearnService.deleteTeacherLearn(loginUser.getMember_id(), classId, learnId);
        return "redirect:/teacher/classes/" + classId + "/learns";
    }
    
    @PostMapping("/teacher/classes/{classId}/learns/{learnId}/restore")
    public String restoreTeacherLearn(@PathVariable("classId") int classId,
                                      @PathVariable("learnId") int learnId,
                                      HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        teacherLearnService.restoreTeacherLearn(loginUser.getMember_id(), classId, learnId);
        return "redirect:/teacher/classes/" + classId + "/learns/trash";
    }
    
	/* 영구삭제 */
    @PostMapping("/teacher/classes/{classId}/learns/{learnId}/remove")
    public String removeTeacherLearn(@PathVariable("classId") int classId,
                                     @PathVariable("learnId") int learnId,
                                     HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        teacherLearnService.removeTeacherLearn(loginUser.getMember_id(), classId, learnId);
        return "redirect:/teacher/classes/" + classId + "/learns/trash";
    }
    
    @GetMapping("/teacher/classes/{classId}/assignments")
    public String teacherAssignmentList(@PathVariable("classId") int classId,
                                        @RequestParam(value = "trash", defaultValue = "0") int trash,
                                        Model model,
                                        HttpServletRequest request,
                                        HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("pageTitle", trash == 1 ? "휴지통 (과제 관리)" : "과제 관리");
        model.addAttribute("currentUri", "/teacher/classes/" + classId + "/assignments");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/assignment/listContent.jsp");

        setTeacherClassDetailLayout(model, request, classId, getTeacherClassName(classId, session));
        model.addAttribute("trashMode", trash == 1);
        model.addAttribute("assignmentList", teacherAssignmentService.getTeacherAssignmentList(loginUser.getMember_id(), classId, trash));

        return "common/layout/teacherLayout";
    }

    @PostMapping("/teacher/classes/{classId}/assignments")
    public String registTeacherAssignment(@PathVariable("classId") int classId,
                                          @ModelAttribute TeacherAssignmentSaveDTO dto,
                                          HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }
        teacherAssignmentService.registTeacherAssignment(loginUser.getMember_id(), classId, dto);
        return "redirect:/teacher/classes/" + classId + "/assignments";
    }

    @PostMapping("/teacher/classes/{classId}/assignments/{assignmentId}/update")
    public String modifyTeacherAssignment(@PathVariable("classId") int classId,
                                          @PathVariable("assignmentId") int assignmentId,
                                          @ModelAttribute TeacherAssignmentSaveDTO dto,
                                          HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }
        teacherAssignmentService.modifyTeacherAssignment(loginUser.getMember_id(), classId, assignmentId, dto);
        return "redirect:/teacher/classes/" + classId + "/assignments";
    }

    @PostMapping("/teacher/classes/{classId}/assignments/{assignmentId}/delete")
    public String deleteTeacherAssignment(@PathVariable("classId") int classId,
                                          @PathVariable("assignmentId") int assignmentId,
                                          HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }
        teacherAssignmentService.deleteTeacherAssignment(loginUser.getMember_id(), classId, assignmentId);
        return "redirect:/teacher/classes/" + classId + "/assignments";
    }

    @PostMapping("/teacher/classes/{classId}/assignments/{assignmentId}/restore")
    public String restoreTeacherAssignment(@PathVariable("classId") int classId,
                                           @PathVariable("assignmentId") int assignmentId,
                                           HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }
        teacherAssignmentService.restoreTeacherAssignment(loginUser.getMember_id(), classId, assignmentId);
        return "redirect:/teacher/classes/" + classId + "/assignments?trash=1";
    }

    @PostMapping("/teacher/classes/{classId}/assignments/{assignmentId}/remove")
    public String removeTeacherAssignment(@PathVariable("classId") int classId,
                                          @PathVariable("assignmentId") int assignmentId,
                                          HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }
        teacherAssignmentService.removeTeacherAssignment(loginUser.getMember_id(), classId, assignmentId);
        return "redirect:/teacher/classes/" + classId + "/assignments?trash=1";
    }

    @PostMapping("/teacher/classes/{classId}/assignments/{assignmentId}/toggle-status")
    public String toggleTeacherAssignmentStatus(@PathVariable("classId") int classId,
                                                @PathVariable("assignmentId") int assignmentId,
                                                HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }
        teacherAssignmentService.toggleTeacherAssignmentStatus(loginUser.getMember_id(), classId, assignmentId);
        return "redirect:/teacher/classes/" + classId + "/assignments";
    }

    @GetMapping("/teacher/classes/{classId}/assignments/{assignmentId}/detail")
    @ResponseBody
    public TeacherAssignmentDetailDTO teacherAssignmentDetail(@PathVariable("classId") int classId,
                                                              @PathVariable("assignmentId") int assignmentId,
                                                              HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        TeacherAssignmentDetailDTO detail = teacherAssignmentService.getTeacherAssignmentDetail(loginUser.getMember_id(), classId, assignmentId);
        if (detail == null) {
            throw new IllegalArgumentException("과제를 찾을 수 없습니다.");
        }
        return detail;
    }


    @GetMapping("/teacher/classes/{classId}/assignments/{assignmentId}/students/{studentId}")
    @ResponseBody
    public TeacherAssignmentStudentWorkDTO teacherStudentSubmissionDetail(@PathVariable("classId") int classId,
                                                                          @PathVariable("assignmentId") int assignmentId,
                                                                          @PathVariable("studentId") int studentId,
                                                                          HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        TeacherAssignmentStudentWorkDTO detail =
                teacherAssignmentService.getTeacherStudentSubmissionDetail(loginUser.getMember_id(), classId, assignmentId, studentId);

        if (detail == null) {
            throw new IllegalArgumentException("학생 제출 내역을 찾을 수 없습니다.");
        }
        return detail;
    }

    @PostMapping("/teacher/classes/{classId}/assignments/{assignmentId}/students/{studentId}/reject")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rejectTeacherStudentSubmission(@PathVariable("classId") int classId,
                                                                              @PathVariable("assignmentId") int assignmentId,
                                                                              @PathVariable("studentId") int studentId,
                                                                              @RequestParam("returnReason") String returnReason,
                                                                              HttpSession session) {
        Map<String, Object> result = new LinkedHashMap<>();
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(result);
        }

        try {
            teacherAssignmentService.rejectTeacherStudentSubmission(loginUser.getMember_id(), classId, assignmentId, studentId, returnReason);
            TeacherAssignmentStudentWorkDTO detail =
                    teacherAssignmentService.getTeacherStudentSubmissionDetail(loginUser.getMember_id(), classId, assignmentId, studentId);
            result.put("success", true);
            result.put("message", "과제를 반려했습니다.");
            result.put("submission", detail);
            result.put("assignmentDetail", teacherAssignmentService.getTeacherAssignmentDetail(loginUser.getMember_id(), classId, assignmentId));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/teacher/classes/{classId}/assignments/{assignmentId}/students/{studentId}/complete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> completeTeacherStudentSubmission(@PathVariable("classId") int classId,
                                                                                @PathVariable("assignmentId") int assignmentId,
                                                                                @PathVariable("studentId") int studentId,
                                                                                HttpSession session) {
        Map<String, Object> result = new LinkedHashMap<>();
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(result);
        }

        try {
            teacherAssignmentService.completeTeacherStudentSubmission(loginUser.getMember_id(), classId, assignmentId, studentId);
            TeacherAssignmentStudentWorkDTO detail =
                    teacherAssignmentService.getTeacherStudentSubmissionDetail(loginUser.getMember_id(), classId, assignmentId, studentId);
            result.put("success", true);
            result.put("message", "제출완료 상태로 변경했습니다.");
            result.put("submission", detail);
            result.put("assignmentDetail", teacherAssignmentService.getTeacherAssignmentDetail(loginUser.getMember_id(), classId, assignmentId));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }


    @PostMapping("/teacher/classes/{classId}/assignments/{assignmentId}/students/{studentId}/feedback-complete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> completeTeacherStudentSubmissionWithFeedback(@PathVariable("classId") int classId,
                                                                                            @PathVariable("assignmentId") int assignmentId,
                                                                                            @PathVariable("studentId") int studentId,
                                                                                            @RequestParam("feedbackContent") String feedbackContent,
                                                                                            HttpSession session) {
        Map<String, Object> result = new LinkedHashMap<>();
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(result);
        }

        try {
            teacherAssignmentService.completeTeacherStudentSubmissionWithFeedback(loginUser.getMember_id(), classId, assignmentId, studentId, feedbackContent);
            TeacherAssignmentStudentWorkDTO detail =
                    teacherAssignmentService.getTeacherStudentSubmissionDetail(loginUser.getMember_id(), classId, assignmentId, studentId);
            result.put("success", true);
            result.put("message", "피드백을 등록하고 확인완료로 변경했습니다.");
            result.put("submission", detail);
            result.put("assignmentDetail", teacherAssignmentService.getTeacherAssignmentDetail(loginUser.getMember_id(), classId, assignmentId));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/teacher/classes/{classId}/assignments/{assignmentId}/students/{studentId}/download")
    public void downloadTeacherStudentSubmissionFile(@PathVariable("classId") int classId,
                                                     @PathVariable("assignmentId") int assignmentId,
                                                     @PathVariable("studentId") int studentId,
                                                     HttpSession session,
                                                     HttpServletResponse response) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        TeacherAssignmentStudentWorkDTO detail =
                teacherAssignmentService.getTeacherStudentSubmissionDetail(loginUser.getMember_id(), classId, assignmentId, studentId);

        if (detail == null || detail.getAttachedFileName() == null || detail.getAttachedFileName().isBlank()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File file = resolveAssignmentFile(detail.getUploadPath(), detail.getAttachedFileName());
        if (file == null || !file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/octet-stream");
        String encodedName = URLEncoder.encode(detail.getAttachedFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedName);
        response.setContentLengthLong(file.length());

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
            out.flush();
        }
    }

    @GetMapping("/teacher/classes/{classId}/reports")
    public String teacherReportPage(@PathVariable("classId") int classId,
                                    Model model,
                                    HttpServletRequest request,
                                    HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        int teacherId = loginUser.getMember_id();

        ClassVO classInfo = classService.getTeacherClassDetail(teacherId, classId);
        if (classInfo == null) {
            return "redirect:/teacher/classes";
        }

        List<ReportListDTO> reportList = reportService.getTeacherReportList(teacherId, classId);

        List<TeacherStudentManageDTO> studentList =
                classService.getTeacherStudentManageList(teacherId, classId);

        model.addAttribute("pageTitle", "리포트 관리");
        model.addAttribute("currentUri", "/teacher/classes/" + classId + "/reports");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/report/listContent.jsp");

        setTeacherClassDetailLayout(model, request, classId, classInfo.getClassName());

        model.addAttribute("classId", classId);
        model.addAttribute("classInfo", classInfo);
        model.addAttribute("reportList", reportList);
        model.addAttribute("studentList", studentList);
        model.addAttribute("reportGenerateRequestDTO", new ReportGenerateRequestDTO());

        return "common/layout/teacherLayout";
    }

    @PostMapping("/teacher/classes/{classId}/reports/generate")
    public String generateTeacherReports(@PathVariable("classId") int classId,
                                         ReportGenerateRequestDTO dto,
                                         HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        int teacherId = loginUser.getMember_id();

        reportService.generateReports(teacherId, classId, dto);

        return "redirect:/teacher/classes/" + classId + "/reports";
    }

    @GetMapping("/teacher/classes/{classId}/reports/{reportId}")
    @ResponseBody
    public ReportDetailDTO getTeacherReportDetail(@PathVariable("classId") int classId,
                                                  @PathVariable("reportId") int reportId,
                                                  HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        int teacherId = loginUser.getMember_id();

        return reportService.getTeacherReportDetail(teacherId, classId, reportId);
    }


    private String buildTermLabel(ClassVO classInfo) {
        if (classInfo == null) {
            return "2026 1학기";
        }
        if (classInfo.getYear() > 0 && classInfo.getSemester() > 0) {
            return classInfo.getYear() + " " + classInfo.getSemester() + "학기";
        }
        return (classInfo.getYearLabel() != null && !classInfo.getYearLabel().isBlank()) ? classInfo.getYearLabel() : "2026 1학기";
    }

    private String safeDisplayName(MemberVO loginUser, String fallback) {
        if (loginUser == null || loginUser.getName() == null || loginUser.getName().isBlank()) {
            return fallback;
        }
        return loginUser.getName().trim();
    }

    private void setTeacherLayoutBase(Model model, HttpServletRequest request) {
        model.addAttribute("roleKey", "teacher");
        model.addAttribute("roleLabel", "교사");
        model.addAttribute("homeUrl", "/teacher");
        model.addAttribute("classMenuUrl", "/teacher/classes");
        model.addAttribute("notificationUrl", "/teacher/notifications");
        model.addAttribute("settingsUrl", "/teacher/settings");
        model.addAttribute("isClassDetail", false);
        model.addAttribute("classId", null);
        model.addAttribute("className", null);
    }

    private void setTeacherClassDetailLayout(Model model, HttpServletRequest request, int classId, String className) {
        setTeacherLayoutBase(model, request);

        model.addAttribute("isClassDetail", true);
        model.addAttribute("classId", classId);
        model.addAttribute("className", className);

        model.addAttribute("classHomeUrl", "/teacher/classes/" + classId);
        model.addAttribute("manageUrl", "/teacher/classes/" + classId + "/manage");
        model.addAttribute("studentUrl", "/teacher/classes/" + classId + "/students");
        model.addAttribute("bulletinUrl", "/teacher/classes/" + classId + "/bulletins");
        model.addAttribute("learnUrl", "/teacher/classes/" + classId + "/learns");
        model.addAttribute("assignmentUrl", "/teacher/classes/" + classId + "/assignments");
        model.addAttribute("reportUrl", "/teacher/classes/" + classId + "/reports");
    }

    private String safeString(String value) {
        return value == null ? "" : value;
    }

    private String formatNoticeDate(java.util.Date date) {
        if (date == null) {
            return "";
        }
        return new java.text.SimpleDateFormat("yyyy.MM.dd").format(date);
    }

    private String formatNoticeDateTime(java.util.Date date) {
        if (date == null) {
            return "";
        }
        return new java.text.SimpleDateFormat("yyyy.MM.dd HH:mm").format(date);
    }

    private String getTeacherClassName(int classId, HttpSession session) {
        try {
            MemberVO loginUser = getLoginUser(session);
            if (loginUser == null) {
                return "클래스";
            }

            ClassVO classInfo = classService.getTeacherClassDetail(loginUser.getMember_id(), classId);
            if (classInfo != null && classInfo.getClassName() != null) {
                return classInfo.getClassName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "클래스";
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
