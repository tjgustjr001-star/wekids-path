
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
        return sqlSession.selectOne("com.spring.dao.MemberDAO.selectMemberByLoginId", login_id);
    }

    @Override
    public int selectNextMemberId() throws Exception {
        Integer nextId = sqlSession.selectOne("com.spring.dao.MemberDAO.selectNextMemberId");
        return nextId == null ? 1 : nextId;
    }

    @Override
    public void insertMember(MemberVO member) throws Exception {
        sqlSession.insert("com.spring.dao.MemberDAO.insertMember", member);
    }

    @Override
    public void insertParent(MemberVO member) throws Exception {
        sqlSession.insert("com.spring.dao.MemberDAO.insertParent", member);
    }
}
