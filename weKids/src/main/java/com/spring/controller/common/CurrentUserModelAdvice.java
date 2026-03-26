package com.spring.controller.common;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.spring.dto.MemberVO;
import com.spring.security.CustomUser;

@ControllerAdvice(annotations = Controller.class)
public class CurrentUserModelAdvice {

    @ModelAttribute("userName")
    public String userName(Authentication authentication) {
        MemberVO member = getMember(authentication);
        if (member == null || member.getName() == null || member.getName().trim().isEmpty()) {
            return null;
        }
        return member.getName().trim();
    }

    @ModelAttribute("roleName")
    public String roleName(Authentication authentication) {
        MemberVO member = getMember(authentication);
        if (member == null) {
            return null;
        }

        String roleCode = member.getRole_code();
        if (roleCode == null) {
            return member.getRole_name();
        }

        switch (roleCode.toUpperCase()) {
            case "STUDENT": return "학생 계정";
            case "PARENT": return "학부모 계정";
            case "TEACHER": return "교사 계정";
            default: return member.getRole_name();
        }
    }

    @ModelAttribute("currentUserProfileImageUrl")
    public String currentUserProfileImageUrl(Authentication authentication) {
        MemberVO member = getMember(authentication);
        if (member == null || member.getProfile_image() == null || member.getProfile_image().trim().isEmpty()) {
            return null;
        }
        return "/resources/upload/profile/" + member.getProfile_image().trim();
    }

    private MemberVO getMember(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUser) {
            return ((CustomUser) principal).getMember();
        }
        return null;
    }
}
