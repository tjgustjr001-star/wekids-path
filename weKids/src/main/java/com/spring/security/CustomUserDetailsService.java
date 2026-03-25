package com.spring.security;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.dao.AuthorityDAO;
import com.spring.dao.MemberDAO;
import com.spring.dto.AuthorityVO;
import com.spring.dto.MemberVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberDAO memberDAO;
    private final AuthorityDAO authorityDAO;

    @Override
    public UserDetails loadUserByUsername(String login_id) throws UsernameNotFoundException {
        try {
            MemberVO member = memberDAO.selectMemberByLoginId(login_id);

            if (member == null) {
                throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
            }

            List<AuthorityVO> authorities = authorityDAO.selectAuthoritiesByMemberId(member.getMember_id());
            
            if (authorities == null || authorities.isEmpty()) {
                throw new UsernameNotFoundException("권한이 없는 사용자입니다.");
            }

            member.setAuthorities(authorities);

            AuthorityVO firstAuthority = authorities.get(0);
            member.setRole_code(firstAuthority.getRoleCode());
            member.setRole_name(firstAuthority.getRoleName());

            return new CustomUser(member);

        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UsernameNotFoundException("사용자 조회 중 오류가 발생했습니다.", e);
        }
    }
}