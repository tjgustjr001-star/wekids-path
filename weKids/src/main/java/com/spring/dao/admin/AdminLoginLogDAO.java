package com.spring.dao.admin;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLoginLogDAO {
	void updateLastLoginAt(@Param("memberId") int memberId) throws SQLException;

	void insertSuccessLoginLog(@Param("memberId") int memberId,
	                           @Param("loginId") String loginId,
	                           @Param("ipAddress") String ipAddress,
	                           @Param("userAgent") String userAgent) throws SQLException;
	
	
}
