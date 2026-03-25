package com.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.cmd.ParentRegisterCommand;
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

    @PostMapping("/parent-register")
    public String parentRegister(ParentRegisterCommand regCommand,
                                 RedirectAttributes rttr) {
        rttr.addFlashAttribute("msg", "보호자 등록 화면 테스트용 제출 성공");
        return "redirect:/auth/login";
    }
}