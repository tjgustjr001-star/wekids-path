package com.spring.dto.admin;

import java.util.Date;

public class AdminLoginLogDTO {

	 private int loginLogId;
	    private Integer memberId;
	    private String loginId;
	    private String loginResult;
	    private String loginType;
	    private String ipAddress;
	    private String userAgent;
	    private String failReason;
	    private Date createdAt;

	    public int getLoginLogId() {
	        return loginLogId;
	    }

	    public void setLoginLogId(int loginLogId) {
	        this.loginLogId = loginLogId;
	    }

	    public Integer getMemberId() {
	        return memberId;
	    }

	    public void setMemberId(Integer memberId) {
	        this.memberId = memberId;
	    }

	    public String getLoginId() {
	        return loginId;
	    }

	    public void setLoginId(String loginId) {
	        this.loginId = loginId;
	    }

	    public String getLoginResult() {
	        return loginResult;
	    }

	    public void setLoginResult(String loginResult) {
	        this.loginResult = loginResult;
	    }

	    public String getLoginType() {
	        return loginType;
	    }

	    public void setLoginType(String loginType) {
	        this.loginType = loginType;
	    }

	    public String getIpAddress() {
	        return ipAddress;
	    }

	    public void setIpAddress(String ipAddress) {
	        this.ipAddress = ipAddress;
	    }

	    public String getUserAgent() {
	        return userAgent;
	    }

	    public void setUserAgent(String userAgent) {
	        this.userAgent = userAgent;
	    }

	    public String getFailReason() {
	        return failReason;
	    }

	    public void setFailReason(String failReason) {
	        this.failReason = failReason;
	    }

	    public Date getCreatedAt() {
	        return createdAt;
	    }

	    public void setCreatedAt(Date createdAt) {
	        this.createdAt = createdAt;
	    }
}
