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
		return adminUserDAO.selectUserDetailById(memberId);
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
}