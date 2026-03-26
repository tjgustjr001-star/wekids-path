package com.spring.dto.admin;

import java.util.Date;

import lombok.Data;

@Data
public class AdminTeacherListDTO {

	private int teacherId;
	private String teacherName;
	private String email;
	private String schoolName;
	private int classCount;
	private Date joinDate;
	private String accountStatus;
}