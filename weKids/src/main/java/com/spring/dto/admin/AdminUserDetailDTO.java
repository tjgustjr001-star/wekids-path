package com.spring.dto.admin;

import lombok.Data;

@Data
public class AdminUserDetailDTO {
	private int memberId;
	private String name;
	private String loginId;
	private String email;
	private String roleCode;
	private String roleName;
	private String accountStatus;
	private String lastLoginAt;
	private String createdAt;
	private String classInfo;
	private String connectionStatus;
}