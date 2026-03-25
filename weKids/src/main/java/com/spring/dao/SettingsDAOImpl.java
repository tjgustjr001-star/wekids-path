package com.spring.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.spring.dto.ChildLinkVO;
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
    public void insertStudentParentLink(int studentId, int parentId, String relationType) throws SQLException {
        Map<String, Object> param = new HashMap<>();
        param.put("studentId", studentId);
        param.put("parentId", parentId);
        param.put("relationType", relationType);

        sqlSession.insert("com.spring.dao.SettingsDAO.insertStudentParentLink", param);
    }

    @Override
    public List<ParentChildVO> selectLinkedChildrenByParentMemberId(int memberId) throws SQLException {
        return sqlSession.selectList("com.spring.dao.SettingsDAO.selectLinkedChildrenByParentMemberId", memberId);
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
}