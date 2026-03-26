package com.spring.service;

import com.spring.dto.MemberVO;

public interface AuthService {

    MemberVO login(String login_id, String pwd) throws Exception;
}