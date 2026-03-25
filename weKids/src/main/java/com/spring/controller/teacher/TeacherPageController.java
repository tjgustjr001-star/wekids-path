package com.spring.controller.teacher;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.dto.ClassVO;
import com.spring.dto.MemberVO;
import com.spring.dto.NoticeVO;
import com.spring.dto.TeacherClassCreateDTO;
import com.spring.dto.TeacherClassManageDTO;
import com.spring.security.CustomUser;
import com.spring.service.ClassService;
import com.spring.service.NoticeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class TeacherPageController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ClassService classService;

    @GetMapping("/teacher")
    public String teacherHome(Model model, HttpServletRequest request) {
        model.addAttribute("pageTitle", "교사 홈");
        model.addAttribute("currentUri", "/teacher");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/homeContent.jsp");

        setTeacherLayoutBase(model, request);

        model.addAttribute("greetingTitle", "안녕하세요, 김교사님!");
        model.addAttribute("greetingMessage", "오늘 수업과 클래스 현황을 확인해보세요.");

        Map<String, Object> classSummary = new LinkedHashMap<>();
        classSummary.put("managedClassCount", 2);
        classSummary.put("studentCount", 44);
        classSummary.put("pendingFeedbackCount", 5);
        classSummary.put("newBulletinNeedCount", 1);
        model.addAttribute("classSummary", classSummary);

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
                                   HttpSession session) {
        model.addAttribute("pageTitle", "클래스 홈");
        model.addAttribute("currentUri", "/teacher/classes/" + classId);
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/class/homeContent.jsp");

        setTeacherClassDetailLayout(model, request, classId, getTeacherClassName(classId, session));

        return "common/layout/teacherLayout";
    }

    @GetMapping("/teacher/classes/{classId}/manage")
    public String teacherClassManage(@PathVariable("classId") int classId,
                                     Model model,
                                     HttpServletRequest request,
                                     HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);

        model.addAttribute("pageTitle", "클래스 관리");
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
                                     HttpSession session) {
        model.addAttribute("pageTitle", "학생 관리");
        model.addAttribute("currentUri", "/teacher/classes/" + classId + "/students");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/student/listContent.jsp");

        setTeacherClassDetailLayout(model, request, classId, getTeacherClassName(classId, session));

        return "common/layout/teacherLayout";
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
                                   HttpSession session) {
        model.addAttribute("pageTitle", "학습 관리");
        model.addAttribute("currentUri", "/teacher/classes/" + classId + "/learns");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/learn/listContent.jsp");

        setTeacherClassDetailLayout(model, request, classId, getTeacherClassName(classId, session));

        return "common/layout/teacherLayout";
    }

    @GetMapping("/teacher/classes/{classId}/assignments")
    public String teacherAssignmentList(@PathVariable("classId") int classId,
                                        Model model,
                                        HttpServletRequest request,
                                        HttpSession session) {
        model.addAttribute("pageTitle", "과제 관리");
        model.addAttribute("currentUri", "/teacher/classes/" + classId + "/assignments");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/assignment/listContent.jsp");

        setTeacherClassDetailLayout(model, request, classId, getTeacherClassName(classId, session));

        return "common/layout/teacherLayout";
    }

    @GetMapping("/teacher/classes/{classId}/reports")
    public String teacherReportList(@PathVariable("classId") int classId,
                                    Model model,
                                    HttpServletRequest request,
                                    HttpSession session) {
        model.addAttribute("pageTitle", "리포트 관리");
        model.addAttribute("currentUri", "/teacher/classes/" + classId + "/reports");
        model.addAttribute("contentPage", "/WEB-INF/views/teacher/report/listContent.jsp");

        setTeacherClassDetailLayout(model, request, classId, getTeacherClassName(classId, session));

        return "common/layout/teacherLayout";
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
}