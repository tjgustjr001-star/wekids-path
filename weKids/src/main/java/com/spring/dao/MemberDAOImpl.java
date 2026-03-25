package com.spring.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.dto.MemberVO;

@Repository
public class MemberDAOImpl implements MemberDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public MemberVO selectMemberByLoginId(String login_id) throws Exception {
        return sqlSession.selectOne("Member-Mapper.selectMemberByLoginId", login_id);
    }
}