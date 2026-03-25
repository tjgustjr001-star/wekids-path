package com.spring.dto.admin;

import java.util.Date;

import lombok.Data;

@Data
public class AdminTeacherClassDTO {
	private int classId;
	private String className;
	private String classStatus;
	private int studentCount;
	private Date startDate;
	private Date endDate;
}