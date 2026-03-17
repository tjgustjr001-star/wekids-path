package com.spring.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.cmd.ParentRegisterCommand;
import com.spring.dto.MemberVO;
import com.spring.service.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/parent-register")
    public String parentRegister() {
        return "auth/parent-register";
    }

    @GetMapping(value = "/id-check", produces = "text/plain; charset=UTF-8")
    @ResponseBody
    public String idCheck(@RequestParam("login_id") String loginId) {
        return "admin".equalsIgnoreCase(loginId) ? "duplicated" : "available";
    }

    @GetMapping(value = "/parent-link-code-check", produces = "text/plain; charset=UTF-8")
    @ResponseBody
    public String parentLinkCodeCheck(@RequestParam("parent_link_code") String parentLinkCode) {
        return "ABCD1234".equalsIgnoreCase(parentLinkCode) ? "valid" : "invalid";
    }

    @PostMapping("/login_success")
    public String loginSuccess(@RequestParam("login_id") String login_id,
                               @RequestParam("pwd") String pwd,
                               HttpSession session,
                               RedirectAttributes rttr) throws Exception {

        MemberVO member = authService.login(login_id, pwd);

        if (member == null) {
            return "redirect:/auth/login?error=1";
        }

        session.setAttribute("loginUser", member);

        String roleCode = member.getRole_code();

        if ("ADMIN".equals(roleCode)) {
            return "redirect:/admin";
        } else if ("STUDENT".equals(roleCode)) {
            return "redirect:/student";
        } else if ("PARENT".equals(roleCode)) {
            return "redirect:/parent";
        } else if ("TEACHER".equals(roleCode)) {
            return "redirect:/teacher";
        }

        session.invalidate();
        rttr.addFlashAttribute("msg", "권한 정보가 올바르지 않습니다.");
        return "redirect:/auth/login?error=1";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login?logout=1";
    }

    @PostMapping("/parent-register")
    public String parentRegister(ParentRegisterCommand regCommand,
                                 RedirectAttributes rttr) {
        rttr.addFlashAttribute("msg", "보호자 등록 화면 테스트용 제출 성공");
        return "redirect:/auth/login";
    }
}