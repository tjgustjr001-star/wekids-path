package com.spring.dto.admin;

import lombok.Data;

@Data
public class AdminTeacherRegistDTO {
	private String teacherName;
	private String loginId;
	private String email;
	private String initialPassword;
}

