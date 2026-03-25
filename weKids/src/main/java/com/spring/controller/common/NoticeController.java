package com.spring.controller.common;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.dto.AttachVO;
import com.spring.dto.MemberVO;
import com.spring.dto.NoticeVO;
import com.spring.security.CustomUser;
import com.spring.service.NoticeService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/detail-modal")
    public String detailModal(@RequestParam("classId") int classId,
                              @RequestParam("noticeId") int noticeId,
                              @RequestParam(value = "returnUrl", required = false) String returnUrl,
                              HttpSession session,
                              Model model) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        NoticeVO notice = noticeService.getNoticeDetail(classId, noticeId, loginUser);

        model.addAttribute("classId", classId);
        model.addAttribute("notice", notice);
        model.addAttribute("returnUrl", returnUrl);
        model.addAttribute("roleCode", loginUser.getRole_code());
        setNoticeRoleFlags(loginUser, model);

        return "notice/detailModal";
    }

    @GetMapping("/write-modal")
    public String writeModal(HttpSession session,
                             @RequestParam("classId") int classId,
                             @RequestParam(value = "returnUrl", required = false) String returnUrl,
                             Model model) {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        if (!isTeacher(loginUser)) {
            return "redirect:/";
        }

        NoticeVO notice = new NoticeVO();
        notice.setClassId(classId);

        model.addAttribute("mode", "write");
        model.addAttribute("classId", classId);
        model.addAttribute("notice", notice);
        model.addAttribute("returnUrl", returnUrl);
        model.addAttribute("roleCode", loginUser.getRole_code());
        setNoticeRoleFlags(loginUser, model);

        return "notice/writeModal";
    }

    @GetMapping("/edit-modal")
    public String editModal(@RequestParam("classId") int classId,
                            @RequestParam("noticeId") int noticeId,
                            @RequestParam(value = "returnUrl", required = false) String returnUrl,
                            HttpSession session,
                            Model model) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        if (!isTeacher(loginUser)) {
            return "redirect:/";
        }

        NoticeVO notice = noticeService.getNoticeDetail(classId, noticeId, loginUser);

        model.addAttribute("mode", "edit");
        model.addAttribute("classId", classId);
        model.addAttribute("notice", notice);
        model.addAttribute("returnUrl", returnUrl);
        model.addAttribute("roleCode", loginUser.getRole_code());
        setNoticeRoleFlags(loginUser, model);

        return "notice/writeModal";
    }

    @PostMapping("/regist")
    public String regist(@RequestParam("classId") int classId,
                         NoticeVO notice,
                         @RequestParam(name = "uploadFile", required = false) MultipartFile[] uploadFile,
                         @RequestParam(value = "returnUrl", required = false) String returnUrl,
                         HttpSession session,
                         RedirectAttributes rttr) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        if (!isTeacher(loginUser)) {
            return "redirect:/";
        }

        String saveDir = servletContext.getRealPath("/resources/upload/notice");
        noticeService.registNotice(classId, notice, uploadFile, loginUser, saveDir);

        rttr.addFlashAttribute("msg", "가정통신문이 등록되었습니다.");
        return redirectToReturnUrl(returnUrl, loginUser, classId);
    }

    @PostMapping("/modify")
    public String modify(@RequestParam("classId") int classId,
                         NoticeVO notice,
                         @RequestParam(name = "uploadFile", required = false) MultipartFile[] uploadFile,
                         @RequestParam(value = "returnUrl", required = false) String returnUrl,
                         HttpSession session,
                         RedirectAttributes rttr) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        if (!isTeacher(loginUser)) {
            return "redirect:/";
        }

        String saveDir = servletContext.getRealPath("/resources/upload/notice");
        noticeService.modifyNotice(classId, notice, uploadFile, loginUser, saveDir);

        rttr.addFlashAttribute("msg", "가정통신문이 수정되었습니다.");
        return redirectToReturnUrl(returnUrl, loginUser, classId);
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("noticeId") int noticeId,
                         @RequestParam("classId") int classId,
                         @RequestParam(value = "returnUrl", required = false) String returnUrl,
                         HttpSession session,
                         RedirectAttributes rttr) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        if (!isTeacher(loginUser)) {
            return "redirect:/";
        }

        noticeService.removeNotice(classId, noticeId, loginUser);

        rttr.addFlashAttribute("msg", "가정통신문이 삭제되었습니다.");
        return redirectToReturnUrl(returnUrl, loginUser, classId);
    }

    @PostMapping("/confirm")
    public String confirm(@RequestParam("noticeId") int noticeId,
                          @RequestParam("classId") int classId,
                          @RequestParam(value = "returnUrl", required = false) String returnUrl,
                          HttpSession session,
                          RedirectAttributes rttr) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        noticeService.confirmNotice(classId, noticeId, loginUser);

        rttr.addFlashAttribute("msg", "읽음 확인이 완료되었습니다.");
        return redirectToReturnUrl(returnUrl, loginUser, classId);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("ano") int ano) throws Exception {
        AttachVO attach = noticeService.getAttach(ano);
        if (attach == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(attach.getUploadPath(), attach.getFileName());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        String originalFileName = attach.getFileName();
        int index = originalFileName.indexOf("$$");
        if (index > -1) {
            originalFileName = originalFileName.substring(index + 2);
        }

        String encodedName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + encodedName)
                .body(resource);
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

    private void setNoticeRoleFlags(MemberVO loginUser, Model model) {
        String roleCode = loginUser.getRole_code();

        model.addAttribute("isTeacher", "TEACHER".equals(roleCode));
        model.addAttribute("isStudentOrParent",
                "STUDENT".equals(roleCode) || "PARENT".equals(roleCode));
    }

    private boolean isTeacher(MemberVO loginUser) {
        return loginUser != null && "TEACHER".equals(loginUser.getRole_code());
    }

    private String redirectToReturnUrl(String returnUrl, MemberVO loginUser, Integer classId) {
        if (returnUrl != null && !returnUrl.isBlank()) {
            return "redirect:" + returnUrl;
        }

        if (classId == null) {
            return "redirect:/";
        }

        String roleCode = loginUser.getRole_code();

        if ("TEACHER".equals(roleCode)) {
            return "redirect:/teacher/classes/" + classId + "/bulletins";
        }
        if ("STUDENT".equals(roleCode)) {
            return "redirect:/student/classes/" + classId + "/bulletins";
        }
        if ("PARENT".equals(roleCode)) {
            return "redirect:/parent/classes/" + classId + "/bulletins";
        }

        return "redirect:/";
    }
}