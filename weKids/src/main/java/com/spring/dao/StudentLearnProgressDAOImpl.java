package com.spring.dao;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

public class StudentLearnProgressDAOImpl implements StudentLearnProgressDAO {

    private static final String NAMESPACE = "com.spring.dao.StudentLearnProgressDAO.";

    private SqlSession session;

    public void setSession(SqlSession session) {
        this.session = session;
    }

    @Override
    public Integer selectProgressId(Map<String, Object> paramMap) throws SQLException {
        return session.selectOne(NAMESPACE + "selectProgressId", paramMap);
    }

    @Override
    public Integer selectNextProgressId() throws SQLException {
        return session.selectOne(NAMESPACE + "selectNextProgressId");
    }

    @Override
    public Integer selectNextFeedbackId() throws SQLException {
        return session.selectOne(NAMESPACE + "selectNextFeedbackId");
    }

    @Override
    public void insertLearnProgress(Map<String, Object> paramMap) throws SQLException {
        session.insert(NAMESPACE + "insertLearnProgress", paramMap);
    }

    @Override
    public void updateLearnProgressToInProgress(Map<String, Object> paramMap) throws SQLException {
        session.update(NAMESPACE + "updateLearnProgressToInProgress", paramMap);
    }

    @Override
    public void updateLearnProgressToCompleted(Map<String, Object> paramMap) throws SQLException {
        session.update(NAMESPACE + "updateLearnProgressToCompleted", paramMap);
    }

    @Override
    public Integer selectProgressIdByLearnAndStudent(Map<String, Object> paramMap) throws SQLException {
        return session.selectOne(NAMESPACE + "selectProgressIdByLearnAndStudent", paramMap);
    }

    @Override
    public void insertLearnDifficultyFeedback(Map<String, Object> paramMap) throws SQLException {
        session.insert(NAMESPACE + "insertLearnDifficultyFeedback", paramMap);
    }

    @Override
    public void updateVideoProgress(Map<String, Object> paramMap) throws SQLException {
        session.update(NAMESPACE + "updateVideoProgress", paramMap);
    }

    @Override
    public void updateTextProgress(Map<String, Object> paramMap) throws SQLException {
        session.update(NAMESPACE + "updateTextProgress", paramMap);
    }

    @Override
    public Integer selectDifficultyFeedbackIdByProgressId(Map<String, Object> paramMap) throws SQLException {
        return session.selectOne(NAMESPACE + "selectDifficultyFeedbackIdByProgressId", paramMap);
    }

    @Override
    public void updateLearnDifficultyFeedback(Map<String, Object> paramMap) throws SQLException {
        session.update(NAMESPACE + "updateLearnDifficultyFeedback", paramMap);
    }

    @Override
    public String selectStudentLearnOpenStatus(Map<String, Object> paramMap) throws SQLException {
        return session.selectOne(NAMESPACE + "selectStudentLearnOpenStatus", paramMap);
    }

	@Override
	public String selectProgressStatus(Map<String, Object> paramMap) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}