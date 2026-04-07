	package com.spring.service.admin;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.dao.admin.AdminUserDAO;
import com.spring.dto.admin.AdminStudentRegistDTO;
import com.spring.dto.admin.AdminUserDetailDTO;
import com.spring.dto.admin.AdminUserListDTO;
import com.spring.dto.admin.UserRoleLoginTrendDTO;
import com.spring.dto.admin.WeeklyLoginTrendDTO;

@Service
public class AdminUserServiceImpl implements AdminUserService {

	@Autowired
	private AdminUserDAO adminUserDAO;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<AdminUserListDTO> getUserList() throws SQLException {
		return adminUserDAO.selectUserList();
	}

	@Override
	public AdminUserDetailDTO getUserDetailById(int memberId) throws SQLException {
		AdminUserDetailDTO dto = adminUserDAO.selectUserDetailById(memberId);

		if (dto != null) {
		    dto.setDeviceInfo(parseDeviceInfo(dto.getDeviceInfo()));
		    dto.setAccessLocation(parseAccessLocation(dto.getAccessLocation()));
		}
		return dto;
	}

	@Override
	public void modifyUserStatus(int memberId, String accountStatus) throws SQLException {
		adminUserDAO.updateUserStatus(memberId, accountStatus);
	}

	@Override
	public void registStudent(AdminStudentRegistDTO registDTO) throws Exception {
		int memberId = adminUserDAO.selectNextMemberId();
		int authorityId = adminUserDAO.selectNextAuthorityId();

		registDTO.setInitialPassword(passwordEncoder.encode(registDTO.getInitialPassword()));

		adminUserDAO.insertStudentMember(memberId, registDTO);
		adminUserDAO.insertStudentAuthority(authorityId, memberId);
		adminUserDAO.insertStudent(memberId, registDTO);
	}

	@Override
	public int getTotalUserCount() throws SQLException {
		return adminUserDAO.selectTotalUserCount();
	}
	
	@Override
	public List<WeeklyLoginTrendDTO> getWeeklyLoginTrend() throws SQLException {
	    return adminUserDAO.selectWeeklyLoginTrend();
	}
	
	@Override
	public List<WeeklyLoginTrendDTO> getUserWeeklyLoginTrend(int memberId) throws SQLException {
		return adminUserDAO.selectUserWeeklyLoginTrend(memberId);
	}

	private String parseDeviceInfo(String userAgent) {
		if (userAgent == null || userAgent.isBlank()) return "-";

		String os = "기타";
		String browser = "기타";

		if (userAgent.contains("Windows")) os = "Windows";
		else if (userAgent.contains("Mac OS")) os = "macOS";
		else if (userAgent.contains("Android")) os = "Android";
		else if (userAgent.contains("iPhone")) os = "iPhone";

		if (userAgent.contains("Edg")) browser = "Edge";
		else if (userAgent.contains("Chrome")) browser = "Chrome";
		else if (userAgent.contains("Safari")) browser = "Safari";
		else if (userAgent.contains("Firefox")) browser = "Firefox";
		
		

		return os + " / " + browser;
	}
	private String parseAccessLocation(String ipAddress) {
	    if (ipAddress == null || ipAddress.isBlank()) return "-";

	    if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "::1".equals(ipAddress)) {
	        return "localhost";
	    }

	    if ("127.0.0.1".equals(ipAddress)) {
	        return "localhost";
	    }

	    return ipAddress;
	}
	
	@Override
	public List<UserRoleLoginTrendDTO> getUserRoleLoginTrend() throws SQLException {
	    return adminUserDAO.selectUserRoleLoginTrend();
	}
	
	@Override
	public int getInspectionNeedCount() throws SQLException {
	    return adminUserDAO.selectInspectionNeedCount();
	}

	@Override
	public List<AdminUserListDTO> getInspectionNeedAccountList() throws SQLException {
	    return adminUserDAO.selectInspectionNeedAccountList();
	}
}