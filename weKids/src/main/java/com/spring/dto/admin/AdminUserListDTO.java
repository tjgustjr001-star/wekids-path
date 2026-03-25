package com.spring.dto.admin;

import lombok.Data;

@Data
public class AdminUserListDTO {
	private int memberId;
	private String name;
	private String loginId;
	private String email;
	private String roleCode;
	private String roleName;
	private String connectionStatus;
	private String lastLoginAt;
	private String accountStatus;
}