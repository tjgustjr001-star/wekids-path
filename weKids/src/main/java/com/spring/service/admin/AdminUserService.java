package com.spring.service.admin;

import java.sql.SQLException;
import java.util.List;

import com.spring.dto.admin.AdminStudentRegistDTO;
import com.spring.dto.admin.AdminUserDetailDTO;
import com.spring.dto.admin.AdminUserListDTO;
import com.spring.dto.admin.WeeklyLoginTrendDTO;

public interface AdminUserService {

	List<AdminUserListDTO> getUserList() throws SQLException;

	AdminUserDetailDTO getUserDetailById(int memberId) throws SQLException;

	void modifyUserStatus(int memberId, String accountStatus) throws SQLException;
	
	void registStudent(AdminStudentRegistDTO registDTO) throws Exception;
	
	int getTotalUserCount() throws SQLException;
	
	List<WeeklyLoginTrendDTO> getWeeklyLoginTrend() throws SQLException;
	
	List<WeeklyLoginTrendDTO> getUserWeeklyLoginTrend(int memberId) throws SQLException;
	
}