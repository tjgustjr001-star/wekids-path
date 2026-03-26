
package com.spring.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.spring.dto.ChildLinkVO;
import com.spring.dto.MemberVO;
import com.spring.dto.ParentChildVO;

@Repository
public class SettingsDAOImpl implements SettingsDAO {

    private final SqlSession sqlSession;

    public SettingsDAOImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public ChildLinkVO selectStudentLinkInfoByMemberId(int memberId) throws SQLException {
        return sqlSession.selectOne("com.spring.dao.SettingsDAO.selectStudentLinkInfoByMemberId", memberId);
    }

    @Override
    public void updateStudentLinkCode(int studentId, String parentLinkCode) throws SQLException {
        Map<String, Object> param = new HashMap<>();
        param.put("studentId", studentId);
        param.put("parentLinkCode", parentLinkCode);

        sqlSession.update("com.spring.dao.SettingsDAO.updateStudentLinkCode", param);
    }

    @Override
    public ChildLinkVO selectStudentByLinkCode(String parentLinkCode) throws SQLException {
        return sqlSession.selectOne("com.spring.dao.SettingsDAO.selectStudentByLinkCode", parentLinkCode);
    }

    @Override
    public int countActiveLink(int studentId, int parentId) throws SQLException {
        Map<String, Object> param = new HashMap<>();
        param.put("studentId", studentId);
        param.put("parentId", parentId);

        return sqlSession.selectOne("com.spring.dao.SettingsDAO.countActiveLink", param);
    }

    @Override
    public Integer selectLinkStatus(int studentId, int parentId) throws SQLException {
        Map<String, Object> param = new HashMap<>();
        param.put("studentId", studentId);
        param.put("parentId", parentId);

        return sqlSession.selectOne("com.spring.dao.SettingsDAO.selectLinkStatus", param);
    }

    @Override
    public void insertStudentParentLink(int studentId, int parentId, String relationType) throws SQLException {
        Map<String, Object> param = new HashMap<>();
        param.put("studentId", studentId);
        param.put("parentId", parentId);
        param.put("relationType", relationType);

        sqlSession.insert("com.spring.dao.SettingsDAO.insertStudentParentLink", param);
    }

    @Override
    public int reactivateChildLink(int parentId, int studentId) throws SQLException {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", parentId);
        param.put("studentId", studentId);

        return sqlSession.update("com.spring.dao.SettingsDAO.reactivateChildLink", param);
    }

    @Override
    public List<ParentChildVO> selectLinkedChildrenByParentMemberId(int memberId) throws SQLException {
        return sqlSession.selectList("com.spring.dao.SettingsDAO.selectLinkedChildrenByParentMemberId", memberId);
    }

    @Override
    public List<ParentChildVO> selectLinkedParentsByStudentId(int studentId) throws SQLException {
        return sqlSession.selectList("com.spring.dao.SettingsDAO.selectLinkedParentsByStudentId", studentId);
    }

    @Override
    public ParentChildVO selectChildDetail(int parentId, int studentId) throws SQLException {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", parentId);
        param.put("studentId", studentId);

        return sqlSession.selectOne("com.spring.dao.SettingsDAO.selectChildDetail", param);
    }

    @Override
    public int updateUnlinkChild(int parentId, int studentId) throws SQLException {
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", parentId);
        param.put("studentId", studentId);

        return sqlSession.update("com.spring.dao.SettingsDAO.updateUnlinkChild", param);
    }

    @Override
    public MemberVO selectStudentProfile(int memberId) throws SQLException {
        return sqlSession.selectOne("com.spring.dao.SettingsDAO.selectStudentProfile", memberId);
    }

    @Override
    public MemberVO selectParentProfile(int memberId) throws SQLException {
        return sqlSession.selectOne("com.spring.dao.SettingsDAO.selectParentProfile", memberId);
    }

    @Override
    public MemberVO selectTeacherProfile(int memberId) throws SQLException {
        return sqlSession.selectOne("com.spring.dao.SettingsDAO.selectTeacherProfile", memberId);
    }

    @Override
    public int updateStudentProfile(MemberVO member) throws SQLException {
        return sqlSession.update("com.spring.dao.SettingsDAO.updateStudentProfile", member);
    }

    @Override
    public int updateParentProfile(MemberVO member) throws SQLException {
        return sqlSession.update("com.spring.dao.SettingsDAO.updateParentProfile", member);
    }

    @Override
    public int updateTeacherProfile(MemberVO member) throws SQLException {
        return sqlSession.update("com.spring.dao.SettingsDAO.updateTeacherProfile", member);
    }

    @Override
    public MemberVO selectStudentAccountInfo(int memberId) throws SQLException {
        return sqlSession.selectOne("com.spring.dao.SettingsDAO.selectStudentAccountInfo", memberId);
    }

    @Override
    public MemberVO selectParentAccountInfo(int memberId) throws SQLException {
        return sqlSession.selectOne("com.spring.dao.SettingsDAO.selectParentAccountInfo", memberId);
    }

    @Override
    public MemberVO selectTeacherAccountInfo(int memberId) throws SQLException {
        return sqlSession.selectOne("com.spring.dao.SettingsDAO.selectTeacherAccountInfo", memberId);
    }

    @Override
    public int updateStudentAccountInfo(MemberVO member) throws SQLException {
        return sqlSession.update("com.spring.dao.SettingsDAO.updateStudentAccountInfo", member);
    }

    @Override
    public int updateParentAccountInfo(MemberVO member) throws SQLException {
        return sqlSession.update("com.spring.dao.SettingsDAO.updateParentAccountInfo", member);
    }

    @Override
    public int updateTeacherAccountInfo(MemberVO member) throws SQLException {
        return sqlSession.update("com.spring.dao.SettingsDAO.updateTeacherAccountInfo", member);
    }

    @Override
    public int updateMemberEmail(MemberVO member) throws SQLException {
        return sqlSession.update("com.spring.dao.SettingsDAO.updateMemberEmail", member);
    }

    @Override
    public int updateMemberPassword(int memberId, String pwd) throws SQLException {
        Map<String, Object> param = new HashMap<>();
        param.put("memberId", memberId);
        param.put("pwd", pwd);

        return sqlSession.update("com.spring.dao.SettingsDAO.updateMemberPassword", param);
    }

    @Override
    public int updateMemberAccountStatusDeleted(int memberId) throws SQLException {
        return sqlSession.update("com.spring.dao.SettingsDAO.updateMemberAccountStatusDeleted", memberId);
    }

}

