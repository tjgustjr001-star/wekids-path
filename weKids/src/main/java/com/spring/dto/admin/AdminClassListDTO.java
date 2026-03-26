package com.spring.dto.admin;

import java.util.Date;

import lombok.Data;

@Data
public class AdminClassListDTO {
	private int classId;
	private String className;
	private String teacherName;
	private int studentCount;
	private String classStatus;
	private String schoolName;
	private String description;
	private String lastActive;
	private Date createdAt;
}