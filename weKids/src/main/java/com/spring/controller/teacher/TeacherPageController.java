package com.spring.controller.teacher;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
import com.spring.dto.teacher.TeacherClassCreateDTO;
import com.spring.dto.teacher.TeacherClassManageDTO;
import com.spring.dto.teacher.TeacherAssignmentDetailDTO;
import com.spring.dto.teacher.TeacherAssignmentSaveDTO;
import com.spring.dto.teacher.TeacherAssignmentStudentWorkDTO;
import com.spring.dto.teacher.TeacherLearnDifficultyDTO;
import com.spring.dto.teacher.TeacherLearnProgressDTO;
import com.spring.dto.teacher.TeacherLearnSaveDTO;
import com.spring.dto.teacher.TeacherStudentManageDTO;
import com.spring.dto.teacher.TeacherStudentObservationSaveDTO;
import com.spring.security.CustomUser;
import com.spring.service.ClassService;
import com.spring.service.NoticeService;
import com.spring.service.TeacherAssignmentService;
import com.spring.service.TeacherLearnService;

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