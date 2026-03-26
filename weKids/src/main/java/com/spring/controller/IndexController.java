//package com.spring.controller;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import com.spring.dto.MemberVO;
//import com.spring.security.CustomUser;
//
//@Controller
//public class IndexController {
//
//    @GetMapping("/index")
//    public String index(Authentication authentication) {
//        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUser)) {
//            return "redirect:/auth/login";
//        }
//
//        CustomUser user = (CustomUser) authentication.getPrincipal();
//        MemberVO member = user.getMember();
//        if (member == null || member.getRole_code() == null) {
//            return "redirect:/auth/login";
//        }
//
//        String roleCode = member.getRole_code().toUpperCase();
//        if (roleCode.startsWith("ROLE_")) {
//            roleCode = roleCode.substring(5);
//        }
//
//        switch (roleCode) {
//            case "STUDENT":
//                return "redirect:/student";
//            case "PARENT":
//                return "redirect:/parent";
//            case "TEACHER":
//                return "redirect:/teacher";
//            case "ADMIN":
//                return "redirect:/admin";
//            default:
//                return "redirect:/auth/login";
//        }
//    }
//}


/*
 * sadsd sa sasadsadsasdada
 */
