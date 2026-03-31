package com.spring.dao.admin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.dto.admin.AdminTeacherClassDTO;
import com.spring.dto.admin.AdminTeacherDetailDTO;
import com.spring.dto.admin.AdminTeacherListDTO;
import com.spring.dto.admin.AdminTeacherRegistDTO;
import com.spring.dto.admin.MonthlyJoinCountDTO;

@Repository
public class AdminTeacherDAOImpl implements AdminTeacherDAO {

	@Autowired
	private SqlSession session;

	@Override
	public List<AdminTeacherListDTO> selectTeacherList() throws SQLException {
		return session.selectList("AdminTeacher-Mapper.selectTeacherList");
	}

	@Override
	public AdminTeacherDetailDTO selectTeacherDetailById(int teacherId) throws SQLException {
		return session.selectOne("AdminTeacher-Mapper.selectTeacherDetailById", teacherId);
	}

	@Override
	public List<AdminTeacherClassDTO> selectTeacherClassListById(int teacherId) throws SQLException {
		return session.selectList("AdminTeacher-Mapper.selectTeacherClassListById", teacherId);
	}
	
	@Override
	public void updateTeacherStatus(int teacherId, String accountStatus) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("teacherId", teacherId);
		paramMap.put("accountStatus", accountStatus);

		session.update("AdminTeacher-Mapper.updateTeacherStatus", paramMap);
	}
	@Override
	public int selectNextMemberId() throws SQLException {
		Integer maxId = session.selectOne("AdminTeacher-Mapper.selectNextMemberId");
		return maxId == null ? 1001 : maxId;
	}

	@Override
	public int selectNextAuthorityId() throws SQLException {
		Integer maxId = session.selectOne("AdminTeacher-Mapper.selectNextAuthorityId");
		return maxId == null ? 2001 : maxId;
	}

	@Override
	public void insertTeacherMember(int memberId, AdminTeacherRegistDTO registDTO) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberId", memberId);
		paramMap.put("registDTO", registDTO);

		session.insert("AdminTeacher-Mapper.insertTeacherMember", paramMap);
	}

	@Override
	public void insertTeacherAuthority(int authorityId, int memberId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("authorityId", authorityId);
		paramMap.put("memberId", memberId);

		session.insert("AdminTeacher-Mapper.insertTeacherAuthority", paramMap);
	}

	@Override
	public void insertTeacher(int memberId, AdminTeacherRegistDTO registDTO) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberId", memberId);
		paramMap.put("registDTO", registDTO);

		session.insert("AdminTeacher-Mapper.insertTeacher", paramMap);
	}

	@Override
	public List<MonthlyJoinCountDTO> selectTeacherJoinTrend() throws SQLException {
		return session.selectList("AdminTeacher-Mapper.selectTeacherJoinTrend");
	}
	
	@Override
	public int selectNewTeacherCount() throws SQLException {
	    Integer count = session.selectOne("AdminTeacher-Mapper.selectNewTeacherCount");
	    return count == null ? 0 : count;
	}
}