package com.spring.service.admin;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.admin.AdminLoginLogDAO;

@Service
public class AdminLoginLogServiceImpl implements AdminLoginLogService {

	@Autowired
	private AdminLoginLogDAO loginLogDAO;

	@Override
	public void processLoginSuccess(int memberId, String loginId) throws SQLException {
		loginLogDAO.updateLastLoginAt(memberId);
		loginLogDAO.insertSuccessLoginLog(memberId, loginId);
	}

}
