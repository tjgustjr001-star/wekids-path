
package com.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String idCheck(@RequestParam("login_id") String loginId) throws Exception {
        return authService.isLoginIdDuplicated(loginId) ? "duplicated" : "available";
    }

    @GetMapping(value = "/parent-link-code-check", produces = "text/plain; charset=UTF-8")
    @ResponseBody
    public String parentLinkCodeCheck(@RequestParam("parent_link_code") String parentLinkCode) throws Exception {
        return authService.isValidParentLinkCode(parentLinkCode) ? "valid" : "invalid";
    }

    @PostMapping("/parent-register")
    public String parentRegister(ParentRegisterCommand regCommand,
                                 RedirectAttributes rttr,
                                 Model model) {
        try {
            authService.registerParent(regCommand);
            rttr.addFlashAttribute("msg", "parent_register_success");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/parent-register";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "보호자 등록 중 오류가 발생했습니다.");
            return "auth/parent-register";
        }
    }
}
