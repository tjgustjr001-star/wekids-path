package com.spring.service.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.dao.admin.AdminTeacherDAO;
import com.spring.dto.admin.AdminTeacherActivityChartDTO;
import com.spring.dto.admin.AdminTeacherClassDTO;
import com.spring.dto.admin.AdminTeacherDetailDTO;
import com.spring.dto.admin.AdminTeacherListDTO;
import com.spring.dto.admin.AdminTeacherRegistDTO;
import com.spring.dto.admin.AdminTeacherWeeklyStatDTO;
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

	@Override
	public AdminTeacherActivityChartDTO getTeacherActivityChart(int teacherId) throws SQLException {
		List<AdminTeacherWeeklyStatDTO> statList =
				adminTeacherDAO.selectTeacherWeeklyAssignmentStats(teacherId);

		List<String> labels = new ArrayList<String>();
		List<Integer> assignmentCounts = new ArrayList<Integer>();
		List<Integer> feedbackCounts = new ArrayList<Integer>();

		if (statList != null) {
			for (AdminTeacherWeeklyStatDTO stat : statList) {
				labels.add(stat.getWeekLabel());
				assignmentCounts.add(stat.getAssignmentCount());
				feedbackCounts.add(stat.getFeedbackCount());
			}
		}

		AdminTeacherActivityChartDTO dto = new AdminTeacherActivityChartDTO();
		dto.setLabels(labels);
		dto.setAssignmentCounts(assignmentCounts);
		dto.setFeedbackCounts(feedbackCounts);

		return dto;
	}
}