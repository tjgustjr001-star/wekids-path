package com.spring.service.admin;

import java.sql.SQLException;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.dao.admin.AdminTeacherDAO;
import com.spring.dto.admin.AdminTeacherClassDTO;
import com.spring.dto.admin.AdminTeacherDetailDTO;
import com.spring.dto.admin.AdminTeacherListDTO;
import com.spring.dto.admin.AdminTeacherRegistDTO;
import com.spring.dto.admin.MonthlyJoinCountDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminTeacherServiceImpl implements AdminTeacherService {

	@Autowired
	private AdminTeacherDAO adminTeacherDAO;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<AdminTeacherListDTO> getTeacherList() throws SQLException {
		return adminTeacherDAO.selectTeacherList();
	}

	@Override
	public AdminTeacherDetailDTO getTeacherDetailById(int teacherId) throws SQLException {
		return adminTeacherDAO.selectTeacherDetailById(teacherId);
	}

	@Override
	public List<AdminTeacherClassDTO> getTeacherClassListById(int teacherId) throws SQLException {
		return adminTeacherDAO.selectTeacherClassListById(teacherId);
	}

	@Override
	public void modifyTeacherStatus(int teacherId, String accountStatus) throws SQLException {
		adminTeacherDAO.updateTeacherStatus(teacherId, accountStatus);
	}

	@Override
	public void registTeacher(AdminTeacherRegistDTO registDTO) throws SQLException {
		int memberId = adminTeacherDAO.selectNextMemberId();
		int authorityId = adminTeacherDAO.selectNextAuthorityId();

		registDTO.setInitialPassword(passwordEncoder.encode(registDTO.getInitialPassword()));

		adminTeacherDAO.insertTeacherMember(memberId, registDTO);
		adminTeacherDAO.insertTeacherAuthority(authorityId, memberId);
		adminTeacherDAO.insertTeacher(memberId, registDTO);
	}

	@Override
	public List<MonthlyJoinCountDTO> getTeacherJoinTrend() throws SQLException {
		return adminTeacherDAO.selectTeacherJoinTrend();
	}

	@Override
	public int getNewTeacherCount() throws SQLException {
		return adminTeacherDAO.selectNewTeacherCount();
	}
}