
package com.spring.dao;

import com.spring.dto.MemberVO;

public interface MemberDAO {

    MemberVO selectMemberByLoginId(String login_id) throws Exception;

    int selectNextMemberId() throws Exception;

    void insertMember(MemberVO member) throws Exception;

    void insertParent(MemberVO member) throws Exception;
}
