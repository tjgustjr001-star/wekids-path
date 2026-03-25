package com.spring.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.spring.dto.teacher.TeacherLearnDifficultyDTO;
import com.spring.dto.teacher.TeacherLearnManageDTO;

public class TeacherLearnDAOImpl implements TeacherLearnDAO {

    private SqlSession session;

    public void setSession(SqlSession session) {
        this.session = session;
    }

    @Override
    public List<TeacherLearnManageDTO> selectTeacherLearnList(Map<String, Object> paramMap) throws SQLException {
        return session.selectList("TeacherLearn-Mapper.selectTeacherLearnList", paramMap);
    }

    @Override
    public TeacherLearnManageDTO selectTeacherLearnDetail(Map<String, Object> paramMap) throws SQLException {
        return session.selectOne("TeacherLearn-Mapper.selectTeacherLearnDetail", paramMap);
    }

    @Override
    public int selectNextLearnListId() throws SQLException {
        Integer value = session.selectOne("TeacherLearn-Mapper.selectNextLearnListId");
        return value == null ? 1 : value;
    }

    @Override
    public int selectNextLearnId() throws SQLException {
        Integer value = session.selectOne("TeacherLearn-Mapper.selectNextLearnId");
        return value == null ? 1 : value;
    }

    @Override
    public void insertLearnList(Map<String, Object> paramMap) throws SQLException {
        session.insert("TeacherLearn-Mapper.insertLearnList", paramMap);
    }

    @Override
    public void insertLearnCont(Map<String, Object> paramMap) throws SQLException {
        session.insert("TeacherLearn-Mapper.insertLearnCont", paramMap);
    }

    @Override
    public void updateLearnList(Map<String, Object> paramMap) throws SQLException {
        session.update("TeacherLearn-Mapper.updateLearnList", paramMap);
    }

    @Override
    public void updateLearnCont(Map<String, Object> paramMap) throws SQLException {
        session.update("TeacherLearn-Mapper.updateLearnCont", paramMap);
    }

    @Override
    public void softDeleteLearn(Map<String, Object> paramMap) throws SQLException {
        session.update("TeacherLearn-Mapper.softDeleteLearn", paramMap);
    }

    @Override
    public void restoreLearn(Map<String, Object> paramMap) throws SQLException {
        session.update("TeacherLearn-Mapper.restoreLearn", paramMap);
    }

    @Override
    public void deleteLearnFeedbackByLearnId(Map<String, Object> paramMap) throws SQLException {
        session.delete("TeacherLearn-Mapper.deleteLearnFeedbackByLearnId", paramMap);
    }

    @Override
    public void deleteLearnProgressByLearnId(Map<String, Object> paramMap) throws SQLException {
        session.delete("TeacherLearn-Mapper.deleteLearnProgressByLearnId", paramMap);
    }

    @Override
    public void deleteLearnCont(Map<String, Object> paramMap) throws SQLException {
        session.delete("TeacherLearn-Mapper.deleteLearnCont", paramMap);
    }

    @Override
    public void deleteLearnListIfNoChildren(Map<String, Object> paramMap) throws SQLException {
        session.delete("TeacherLearn-Mapper.deleteLearnListIfNoChildren", paramMap);
    }

	@Override
	public List<TeacherLearnDifficultyDTO> selectTeacherLearnDifficultyList(Map<String, Object> paramMap)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
    
    
}