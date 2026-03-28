package com.spring.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.cmd.SupportWriteCommand;
import com.spring.dto.FaqVO;
import com.spring.dto.MemberVO;
import com.spring.dto.SupportAnswerVO;
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

    // ── 로그인 유저 꺼내기 (StudentPageController와 동일한 패턴) ──────
    private MemberVO getLoginUser(HttpSession session) {
        // 1순위: 세션에 직접 저장된 경우
        Object sessionUser = session.getAttribute("loginUser");
        if (sessionUser instanceof MemberVO) {
            return (MemberVO) sessionUser;
        }

        // 2순위: Spring Security Context에서 꺼내기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return null;

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUser) {
            return ((CustomUser) principal).getMember();
        }
        if (principal instanceof MemberVO) {
            return (MemberVO) principal;
        }

        return null;
    }

    // ── 역할별 레이아웃 결정 ──────────────────────────────────────────
    private String resolveLayout(HttpSession session) {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) return "common/layout/studentLayout"; // fallback

        String role = loginUser.getRole_code(); // "STUDENT" | "PARENT" | "TEACHER"
        if ("TEACHER".equals(role)) return "common/layout/teacherLayout";
        if ("STUDENT".equals(role)) return "common/layout/studentLayout";
        return "common/layout/parentLayout";
    }

    // ── 로그인 유저 검증 + 미로그인 시 redirect ───────────────────────
    private MemberVO requireLogin(HttpSession session, ModelAndView mnv) {
        MemberVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            mnv.setViewName("redirect:/auth/login");
        }
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

    // ── 문의 작성 처리 ───────────────────────────────────────────────
    @PostMapping(value = "/submit", produces = "text/plain;charset=utf-8")
    public ModelAndView write(@ModelAttribute SupportWriteCommand writeCommand,
                              HttpSession session, ModelAndView mnv) throws Exception {

        MemberVO loginUser = requireLogin(session, mnv);
        if (loginUser == null) return mnv; // redirect:/auth/login

        try {
            SupportVO support = writeCommand.toSupportVO(loginUser.getMember_id()); // ✅ 세션에서 꺼낸 ID
            service.write(support);

            mnv.addObject("contentPage", "/WEB-INF/views/support/submitSuccess.jsp");
            mnv.addObject("pageTitle", "문의 완료");

        } catch (SQLException e) {
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

        List<SupportVO> supportList = service.getSupportList(loginUser.getMember_id()); // ✅ 세션에서 꺼낸 ID
        model.addAttribute("supportList", supportList);
        model.addAttribute("contentPage", "/WEB-INF/views/support/list.jsp");
        model.addAttribute("pageTitle", "내 문의 목록");
        return resolveLayout(session);
    }

    // ── 문의 상세 조회 ───────────────────────────────────────────────
    @GetMapping("/detail")
    public ModelAndView detail(@RequestParam(name = "supportNo") int supportNo,
                               HttpSession session, ModelAndView mnv) {

        MemberVO loginUser = requireLogin(session, mnv);
        if (loginUser == null) return mnv;

        try {
            SupportVO support = service.getSupportById(supportNo);
            SupportAnswerVO answer = service.getAnswerBySupportNo(supportNo);

            // 본인 문의인지 확인
            if (support == null || support.getWriterId() != loginUser.getMember_id()) {
                mnv.setViewName("error/403");
                return mnv;
            }

            mnv.addObject("support", support);
            mnv.addObject("answer", answer);
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

    // ── 문의 삭제 ────────────────────────────────────────────────────
    @GetMapping("/remove")
    public ModelAndView remove(@RequestParam(name = "supportNo") int supportNo,
                               HttpSession session, ModelAndView mnv) throws Exception {

        MemberVO loginUser = requireLogin(session, mnv);
        if (loginUser == null) return mnv;

        try {
            SupportVO support = service.getSupportById(supportNo);

            // 본인 문의인지 확인
            if (support == null || support.getWriterId() != loginUser.getMember_id()) { // ✅ 세션 ID로 비교
                mnv.setViewName("error/403");
                return mnv;
            }

            // 답변 완료된 문의는 삭제 불가
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

        // FAQ는 로그인 없이도 볼 수 있으면 loginUser 체크 생략 가능
        List<FaqVO> faqList = "all".equals(category)
                ? faqService.getFaqList()
                : faqService.getFaqListByCategory(category);

        List<String> categoryList = faqService.getCategoryList();

        model.addAttribute("faqList",         faqList);
        model.addAttribute("categoryList",    categoryList);
        model.addAttribute("currentCategory", category);
        model.addAttribute("contentPage",     "/WEB-INF/views/support/faq.jsp");
        model.addAttribute("pageTitle",       "자주 묻는 질문");
        return resolveLayout(session);
    }
}
