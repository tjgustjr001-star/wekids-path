package com.spring.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.spring.dto.AuthorityVO;
import com.spring.dto.MemberVO;

public class CustomUser extends User {

    private static final long serialVersionUID = 1L;

    private MemberVO member;

    public CustomUser(MemberVO member) {
        super(
            member.getLogin_id(),
            member.getPwd(),
            isEnabled(member),
            true,
            true,
            !isLocked(member),
            toGrantedAuthorities(member.getAuthorities())
        );
        this.member = member;
    }

    private static boolean isEnabled(MemberVO member) {
        return "ACTIVE".equalsIgnoreCase(member.getAccount_status());
    }

    private static boolean isLocked(MemberVO member) {
        return "LOCKED".equalsIgnoreCase(member.getAccount_status());
    }

    private static Collection<? extends GrantedAuthority> toGrantedAuthorities(List<AuthorityVO> authorities) {
        if (authorities == null || authorities.isEmpty()) {
            return new ArrayList<>();
        }

        return authorities.stream()
                .filter(auth -> auth != null && auth.getRoleCode() != null)
                .map(auth -> new SimpleGrantedAuthority("ROLE_" + auth.getRoleCode()))
                .collect(Collectors.toList());
    }

    public MemberVO getMember() {
        return member;
    }

    public void setMember(MemberVO member) {
        this.member = member;
    }
}