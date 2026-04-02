package com.spring.service.admin;

import java.sql.SQLException;

public interface AdminLoginLogService {
	void processLoginSuccess(int memberId, String loginId, String ipAddress, String userAgent) throws SQLException;
}
