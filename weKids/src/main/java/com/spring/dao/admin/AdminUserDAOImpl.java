package com.spring.dao.admin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.dto.admin.AdminStudentRegistDTO;
import com.spring.dto.admin.AdminUserDetailDTO;
import com.spring.dto.admin.AdminUserListDTO;
import com.spring.dto.admin.WeeklyLoginTrendDTO;

@Repository
public class AdminUserDAOImpl implements AdminUserDAO {

	@Autowired
	private SqlSession session;

	@Override
	public List<AdminUserListDTO> selectUserList() throws SQLException {
		return session.selectList("AdminUser-Mapper.selectUserList");
	}

	@Override
	public AdminUserDetailDTO selectUserDetailById(int memberId) throws SQLException {
		return session.selectOne("AdminUser-Mapper.selectUserDetailById", memberId);
	}

	@Override
	public void updateUserStatus(int memberId, String accountStatus) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberId", memberId);
		paramMap.put("accountStatus", accountStatus);

		session.update("AdminUser-Mapper.updateUserStatus", paramMap);
	}
	@Override
	public int selectNextMemberId() throws Exception {
		return session.selectOne("AdminUser-Mapper.selectNextMemberId");
	}

	@Override
	public int selectNextAuthorityId() throws Exception {
		return session.selectOne("AdminUser-Mapper.selectNextAuthorityId");
	}

	@Override
	public void insertStudentMember(int memberId, AdminStudentRegistDTO registDTO) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberId", memberId);
		paramMap.put("registDTO", registDTO);

		session.insert("AdminUser-Mapper.insertStudentMember", paramMap);
	}

	@Override
	public void insertStudentAuthority(int authorityId, int memberId) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("authorityId", authorityId);
		paramMap.put("memberId", memberId);

		session.insert("AdminUser-Mapper.insertStudentAuthority", paramMap);
	}

	@Override
	public void insertStudent(int memberId, AdminStudentRegistDTO registDTO) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberId", memberId);
		paramMap.put("registDTO", registDTO);

		session.insert("AdminUser-Mapper.insertStudent", paramMap);
	}
	
	@Override
	public int selectTotalUserCount() throws SQLException {
	    Integer count = session.selectOne("AdminUser-Mapper.selectTotalUserCount");
	    return count == null ? 0 : count;
	}
	
	@Override
	public List<WeeklyLoginTrendDTO> selectWeeklyLoginTrend() throws SQLException {
	    return session.selectList("AdminUser-Mapper.selectWeeklyLoginTrend");
	}
}