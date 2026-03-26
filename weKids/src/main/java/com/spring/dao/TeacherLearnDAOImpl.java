package com.spring.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.spring.dto.teacher.TeacherLearnDifficultyDTO;
import com.spring.dto.teacher.TeacherLearnManageDTO;
import com.spring.dto.teacher.TeacherLearnProgressDTO;

public class TeacherLearnDAOImpl implements TeacherLearnDAO {

    private static final String NAMESPACE = "com.spring.dao.TeacherLearnDAO.";

    private SqlSession session;

    public void setSession(SqlSession session) {
        this.session = session;
    }

    @Override
    public List<TeacherLearnManageDTO> selectTeacherLearnList(Map<String, Object> paramMap) throws SQLException {
        return session.selectList(NAMESPACE + "selectTeacherLearnList", paramMap);
    }

    @Override
    public TeacherLearnManageDTO selectTeacherLearnDetail(Map<String, Object> paramMap) throws SQLException {
        return session.selectOne(NAMESPACE + "selectTeacherLearnDetail", paramMap);
    }

    @Override
    public int selectNextLearnListId() throws SQLException {
        Integer value = session.selectOne(NAMESPACE + "selectNextLearnListId");
        return value == null ? 1 : value;
    }

    @Override
    public int selectNextLearnId() throws SQLException {
        Integer value = session.selectOne(NAMESPACE + "selectNextLearnId");
        return value == null ? 1 : value;
    }

    @Override
    public void insertLearnList(Map<String, Object> paramMap) throws SQLException {
        session.insert(NAMESPACE + "insertLearnList", paramMap);
    }

    @Override
    public void insertLearnCont(Map<String, Object> paramMap) throws SQLException {
        session.insert(NAMESPACE + "insertLearnCont", paramMap);
    }

    @Override
    public void updateLearnList(Map<String, Object> paramMap) throws SQLException {
        session.update(NAMESPACE + "updateLearnList", paramMap);
    }

    @Override
    public void updateLearnCont(Map<String, Object> paramMap) throws SQLException {
        session.update(NAMESPACE + "updateLearnCont", paramMap);
    }

    @Override
    public void softDeleteLearn(Map<String, Object> paramMap) throws SQLException {
        session.update(NAMESPACE + "softDeleteLearn", paramMap);
    }

    @Override
    public void restoreLearn(Map<String, Object> paramMap) throws SQLException {
        session.update(NAMESPACE + "restoreLearn", paramMap);
    }

    @Override
    public void deleteLearnFeedbackByLearnId(Map<String, Object> paramMap) throws SQLException {
        session.delete(NAMESPACE + "deleteLearnFeedbackByLearnId", paramMap);
    }

    @Override
    public void deleteLearnProgressByLearnId(Map<String, Object> paramMap) throws SQLException {
        session.delete(NAMESPACE + "deleteLearnProgressByLearnId", paramMap);
    }

    @Override
    public void deleteLearnCont(Map<String, Object> paramMap) throws SQLException {
        session.delete(NAMESPACE + "deleteLearnCont", paramMap);
    }

    @Override
    public void deleteLearnListIfNoChildren(Map<String, Object> paramMap) throws SQLException {
        session.delete(NAMESPACE + "deleteLearnListIfNoChildren", paramMap);
    }

    @Override
    public List<TeacherLearnDifficultyDTO> selectTeacherLearnDifficultyList(Map<String, Object> paramMap)
            throws SQLException {
        return session.selectList(NAMESPACE + "selectTeacherLearnDifficultyList", paramMap);
    }
    
    @Override
    public List<TeacherLearnProgressDTO> selectTeacherLearnProgressList(Map<String, Object> paramMap)
            throws SQLException {
        return session.selectList(NAMESPACE + "selectTeacherLearnProgressList", paramMap);
    }
}