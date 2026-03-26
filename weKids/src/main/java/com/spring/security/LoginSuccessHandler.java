
package com.spring.security;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.spring.dto.MemberVO;
import com.spring.service.SettingsService;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final SettingsService settingsService;

    public LoginSuccessHandler(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String targetUrl = request.getContextPath() + "/";
        String roleCode = null;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if ("ROLE_ADMIN".equals(role)) {
                targetUrl = request.getContextPath() + "/admin";
                roleCode = "ADMIN";
                break;
            } else if ("ROLE_TEACHER".equals(role)) {
                targetUrl = request.getContextPath() + "/teacher";
                roleCode = "TEACHER";
                break;
            } else if ("ROLE_PARENT".equals(role)) {
                targetUrl = request.getContextPath() + "/parent";
                roleCode = "PARENT";
                break;
            } else if ("ROLE_STUDENT".equals(role)) {
                targetUrl = request.getContextPath() + "/student";
                roleCode = "STUDENT";
                break;
            }
        }

        refreshLoginUserSession(request.getSession(), authentication, roleCode);
        response.sendRedirect(targetUrl);
    }

    private void refreshLoginUserSession(HttpSession session, Authentication authentication, String roleCode) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUser)) {
            return;
        }

        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        MemberVO baseMember = customUser.getMember();

        if (baseMember == null) {
            return;
        }

        if (roleCode == null || roleCode.isBlank()) {
            roleCode = normalizeRoleCode(baseMember.getRole_code());
        }

        if (roleCode == null || roleCode.isBlank() || "ADMIN".equalsIgnoreCase(roleCode)) {
            session.setAttribute("loginUser", baseMember);
            return;
        }

        try {
            MemberVO profileMember = settingsService.getMyProfile(baseMember.getMember_id(), roleCode);
            MemberVO mergedMember = mergeMemberForSession(baseMember, profileMember, roleCode);
            customUser.setMember(mergedMember);
            session.setAttribute("loginUser", mergedMember);
        } catch (SQLException e) {
            session.setAttribute("loginUser", baseMember);
        }
    }

    private MemberVO mergeMemberForSession(MemberVO baseUser, MemberVO savedProfile, String roleCode) {
        MemberVO merged = new MemberVO();

        if (baseUser != null) {
            merged.setMember_id(baseUser.getMember_id());
            merged.setLogin_id(baseUser.getLogin_id());
            merged.setPwd(baseUser.getPwd());
            merged.setEmail(baseUser.getEmail());
            merged.setRole_code(baseUser.getRole_code());
            merged.setRole_name(baseUser.getRole_name());
            merged.setAccount_status(baseUser.getAccount_status());
            merged.setLogin_fail_count(baseUser.getLogin_fail_count());
            merged.setLast_login_at(baseUser.getLast_login_at());
            merged.setLocked_at(baseUser.getLocked_at());
            merged.setPwd_changed_at(baseUser.getPwd_changed_at());
            merged.setCreated_at(baseUser.getCreated_at());
            merged.setUpdated_at(baseUser.getUpdated_at());
            merged.setAuthorities(baseUser.getAuthorities());
        }

        if (savedProfile != null) {
            merged.setName(savedProfile.getName());
            merged.setBirth(savedProfile.getBirth());
            merged.setGender(savedProfile.getGender());
            merged.setIntro(savedProfile.getIntro());
            merged.setProfile_image(savedProfile.getProfile_image());

            if (savedProfile.getRole_code() != null && !savedProfile.getRole_code().isBlank()) {
                merged.setRole_code(savedProfile.getRole_code());
            }
        }

        if (merged.getRole_code() == null || merged.getRole_code().isBlank()) {
            merged.setRole_code(roleCode);
        }

        return merged;
    }

    private String normalizeRoleCode(String roleCode) {
        if (roleCode == null) {
            return "";
        }
        if (roleCode.startsWith("ROLE_")) {
            return roleCode.substring(5);
        }
        return roleCode;
    }
}

