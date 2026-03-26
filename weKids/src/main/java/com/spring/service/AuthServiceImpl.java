package com.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.MemberDAO;
import com.spring.dto.MemberVO;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private MemberDAO memberDAO;

    @Override
    public MemberVO login(String login_id, String pwd) throws Exception {
        System.out.println("service login_id = " + login_id);
        System.out.println("service pwd = " + pwd);

        MemberVO member = memberDAO.selectMemberByLoginId(login_id);
        System.out.println("dao member = " + member);

        if (member == null) {
            return null;
        }

        System.out.println("db pwd = " + member.getPwd());
        System.out.println("db status = " + member.getAccount_status());

        if (!"ACTIVE".equals(member.getAccount_status())) {
            return null;
        }

        if (!member.getPwd().equals(pwd)) {
            return null;
        }

        return member;
    }
}