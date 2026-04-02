package com.spring.dao.admin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminLoginLogDAOImpl implements AdminLoginLogDAO {

	 @Autowired
	    private SqlSession session;

	    @Override
	    public void updateLastLoginAt(@Param("memberId") int memberId) throws SQLException {
	        session.update("AdminLoginLog-Mapper.updateLastLoginAt", memberId);
	    }

	    @Override
	    public void insertSuccessLoginLog(@Param("memberId") int memberId,
	                                      @Param("loginId") String loginId,
	                                      @Param("ipAddress") String ipAddress,
	                                      @Param("userAgent") String userAgent) throws SQLException {
	        Map<String, Object> paramMap = new HashMap<>();
	        paramMap.put("memberId", memberId);
	        paramMap.put("loginId", loginId);
	        paramMap.put("ipAddress", ipAddress);
	        paramMap.put("userAgent", userAgent);

	        session.insert("AdminLoginLog-Mapper.insertSuccessLoginLog", paramMap);
	    }
}