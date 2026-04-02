package com.spring.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spring.cmd.SupportWriteCommand;
import com.spring.dto.FaqVO;
import com.spring.dto.MemberVO;
import com.spring.dto.SupportAnswerVO;
import com.spring.dto.SupportFileVO;
import com.spring.dto.SupportVO;
import com.spring.security.CustomUser;
import com.spring.service.FaqService;
import com.spring.service.SupportService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/support")
public class SupportController {

    @Autowired
    private SupportService service;

    @Autowired
    private FaqService faqService;

    // 파일 저장 경로
    private static final String UPLOAD_DIR = "C:/upload/support/";

    // ── 로그인 유저 꺼내기 ────────────────────────────────────────────
    private MemberVO getLoginUser(HttpSession session) {
        Object sessionUser = session.getAttribute("loginUser");
        if (sessionUser instanceof MemberVO) return (MemberVO) sessionUser;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUser) return ((CustomUser) principal).getMember();
        if (principal instanceof MemberVO) return (MemberVO) principal;
        return null;
    }

    // ── 역할별 레이아웃 결정 ──────────────────────────────────────────
    private String resolveLayout(HttpSession session) {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) return "common/layout/studentLayout";
        String role = loginUser.getRole_code();
        if ("TEACHER".equals(role)) return "common/layout/teacherLayout";
        if ("STUDENT".equals(role)) return "common/layout/studentLayout";
        return "common/layout/parentLayout";
    }

    // ── 로그인 유저 검증 ──────────────────────────────────────────────
    private MemberVO requireLogin(HttpSession session, ModelAndView mnv) {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) mnv.setViewName("redirect:/auth/login");
        return loginUser;
    }

    // ── 문의 메인 ────────────────────────────────────────────────────
    @GetMapping("/main")
    public String main(HttpSession session, Model model) {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) return "redirect:/auth/login";
        model.addAttribute("contentPage", "/WEB-INF/views/support/main.jsp");
        model.addAttribute("pageTitle", "1:1 문의");
        return resolveLayout(session);
    }

    // ── 문의 작성 폼 ─────────────────────────────────────────────────
    @GetMapping("/submit")
    public String writeForm(HttpSession session, Model model) {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) return "redirect:/auth/login";
        model.addAttribute("contentPage", "/WEB-INF/views/support/submit.jsp");
        model.addAttribute("pageTitle", "문의 작성");
        return resolveLayout(session);
    }

    // ── 문의 작성 처리 (파일 첨부 포함) ─────────────────────────────
    @PostMapping(value = "/submit", produces = "text/plain;charset=utf-8")
    public ModelAndView write(@ModelAttribute SupportWriteCommand writeCommand,
                              @RequestParam(value = "attachment", required = false) MultipartFile file,
                              HttpSession session, ModelAndView mnv) throws Exception {

        MemberVO loginUser = requireLogin(session, mnv);
        if (loginUser == null) return mnv;

        try {
            // ① 문의 저장
            SupportVO support = writeCommand.toSupportVO(loginUser.getMember_id());
            service.write(support);

            // ② 파일 첨부가 있으면 저장
            if (file != null && !file.isEmpty()) {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String oriName  = file.getOriginalFilename();
                String ext      = oriName.substring(oriName.lastIndexOf("."));
                String saveName = UUID.randomUUID().toString() + ext;
                String savePath = UPLOAD_DIR + saveName;

                file.transferTo(new File(savePath));

                SupportFileVO fileVO = SupportFileVO.builder()
                        .supportNo(support.getSupportNo())
                        .fileName(saveName)
                        .fileOriName(oriName)
                        .filePath(savePath)
                        .fileSize(file.getSize())
                        .build();
                service.saveFile(fileVO);
            }

            mnv.addObject("contentPage", "/WEB-INF/views/support/submitSuccess.jsp");
            mnv.addObject("pageTitle", "문의 완료");

        } catch (Exception e) {
            e.printStackTrace();
            mnv.addObject("contentPage", "/WEB-INF/views/error/500.jsp");
            mnv.addObject("pageTitle", "오류");
        }

        mnv.setViewName(resolveLayout(session));
        return mnv;
    }

    // ── 내 문의 목록 ─────────────────────────────────────────────────
    @GetMapping("/list")
    public String list(HttpSession session, Model model) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) return "redirect:/auth/login";
        List<SupportVO> supportList = service.getSupportList(loginUser.getMember_id());
        model.addAttribute("supportList", supportList);
        model.addAttribute("contentPage", "/WEB-INF/views/support/list.jsp");
        model.addAttribute("pageTitle", "내 문의 목록");
        return resolveLayout(session);
    }

    // ── 문의 상세 조회 (파일 목록 포함) ─────────────────────────────
    @GetMapping("/detail")
    public ModelAndView detail(@RequestParam(name = "supportNo") int supportNo,
                               HttpSession session, ModelAndView mnv) {

        MemberVO loginUser = requireLogin(session, mnv);
        if (loginUser == null) return mnv;

        try {
            SupportVO support = service.getSupportById(supportNo);
            SupportAnswerVO answer = service.getAnswerBySupportNo(supportNo);

            if (support == null || support.getWriterId() != loginUser.getMember_id()) {
                mnv.setViewName("error/403");
                return mnv;
            }

            // 첨부 파일 목록
            List<SupportFileVO> fileList = service.getFilesBySupportNo(supportNo);

            mnv.addObject("support",  support);
            mnv.addObject("answer",   answer);
            mnv.addObject("fileList", fileList);   // ← 파일 목록 추가
            mnv.addObject("contentPage", "/WEB-INF/views/support/detail.jsp");
            mnv.addObject("pageTitle", "문의 상세");

        } catch (Exception e) {
            e.printStackTrace();
            mnv.addObject("contentPage", "/WEB-INF/views/error/500.jsp");
            mnv.addObject("pageTitle", "오류");
        }

        mnv.setViewName(resolveLayout(session));
        return mnv;
    }

 // ── 파일 보기 및 다운로드 (수정본) ────────────────────────────────────────────────
    @GetMapping("/file/download")
    @ResponseBody
    public ResponseEntity<byte[]> fileDownload(
            @RequestParam("fileName")    String fileName,
            @RequestParam(value = "fileOriName", required = false) String fileOriName) throws Exception {

        File file = new File(UPLOAD_DIR + fileName);
        if (!file.exists()) return ResponseEntity.notFound().build();

        byte[] fileData = FileCopyUtils.copyToByteArray(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();

        // 1. 확장자 추출 및 소문자 변환
        String formatName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        
        // 2. 이미지 여부 확인 및 MediaType 결정
        MediaType mType = null;
        if (formatName.equals("jpg") || formatName.equals("jpeg")) mType = MediaType.IMAGE_JPEG;
        else if (formatName.equals("png")) mType = MediaType.IMAGE_PNG;
        else if (formatName.equals("gif")) mType = MediaType.IMAGE_GIF;

        if (mType != null) {
            // 이미지인 경우: 브라우저에서 바로 보여줌 (Inline)
            headers.setContentType(mType);
        } else {
            // 이미지가 아닌 경우: 다운로드 실행 (Attachment)
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // 원본 파일명이 없으면 저장된 파일명으로 대체
            String downloadName = (fileOriName != null) ? fileOriName : fileName;
            headers.setContentDispositionFormData("attachment",
                    URLEncoder.encode(downloadName, "UTF-8").replaceAll("\\+", "%20"));
        }

        headers.setContentLength(fileData.length);
        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    // ── 문의 삭제 ────────────────────────────────────────────────────
    @GetMapping("/remove")
    public ModelAndView remove(@RequestParam(name = "supportNo") int supportNo,
                               HttpSession session, ModelAndView mnv) throws Exception {

        MemberVO loginUser = requireLogin(session, mnv);
        if (loginUser == null) return mnv;

        try {
            SupportVO support = service.getSupportById(supportNo);
            if (support == null || support.getWriterId() != loginUser.getMember_id()) {
                mnv.setViewName("error/403");
                return mnv;
            }
            if ("답변완료".equals(support.getStatus())) {
                mnv.addObject("errorMsg", "답변 완료된 문의는 삭제할 수 없습니다.");
                mnv.addObject("supportNo", supportNo);
                mnv.addObject("contentPage", "/WEB-INF/views/support/detail.jsp");
                mnv.addObject("pageTitle", "문의 상세");
                mnv.setViewName(resolveLayout(session));
                return mnv;
            }
            service.removeSupportById(supportNo);
            mnv.addObject("contentPage", "/WEB-INF/views/support/removeSuccess.jsp");
            mnv.addObject("pageTitle", "삭제 완료");

        } catch (SQLException e) {
            e.printStackTrace();
            mnv.addObject("contentPage", "/WEB-INF/views/error/500.jsp");
            mnv.addObject("pageTitle", "오류");
        }

        mnv.setViewName(resolveLayout(session));
        return mnv;
    }

    // ── FAQ ──────────────────────────────────────────────────────────
    @GetMapping("/faq")
    public String faq(@RequestParam(value = "category", defaultValue = "all") String category,
                      HttpSession session, Model model) throws SQLException {
        List<FaqVO> faqList = "all".equals(category)
                ? faqService.getFaqList()
                : faqService.getFaqListByCategory(category);
        List<String> categoryList = faqService.getCategoryList();
        
        // ⭐ 1. 현재 로그인한 유저 정보를 가져와서 관리자인지 확인
        MemberVO loginUser = getLoginUser(session); 
        boolean isAdmin = false;
        // (본인 시스템의 관리자 권한 코드가 "ADMIN"이 맞는지 확인 후 수정해주세요)
        if (loginUser != null && "ADMIN".equals(loginUser.getRole_code())) { 
            isAdmin = true;
        }
        
        // ⭐ 2. 모델에 isAdmin 값 전달
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("faqList",         faqList);
        model.addAttribute("categoryList",    categoryList);
        model.addAttribute("currentCategory", category);
        model.addAttribute("contentPage",     "/WEB-INF/views/support/faq.jsp");
        model.addAttribute("pageTitle",       "자주 묻는 질문");
        
        return resolveLayout(session); //
    }
}