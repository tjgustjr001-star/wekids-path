package com.spring.dto.admin;

import java.util.Date;

import lombok.Data;

@Data
public class AdminTeacherDetailDTO {
	private int teacherId;
	private String teacherName;
	private String email;
	private String phone;
	private String intro;
	private Date birth;
	private Date joinDate;
	private Date lastLoginAt;
	private String accountStatus;
}