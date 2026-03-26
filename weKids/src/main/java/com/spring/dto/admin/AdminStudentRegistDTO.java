package com.spring.dto.admin;

import lombok.Data;

@Data
public class AdminStudentRegistDTO {
	private String studentName;
	private String loginId;
	private String email;
	private String initialPassword;
}