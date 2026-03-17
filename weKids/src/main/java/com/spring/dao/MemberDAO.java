package com.spring.dao;

import com.spring.dto.MemberVO;

public interface MemberDAO {

    MemberVO selectMemberByLoginId(String login_id) throws Exception;
}