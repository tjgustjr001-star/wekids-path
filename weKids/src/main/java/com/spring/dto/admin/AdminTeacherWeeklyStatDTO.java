package com.spring.dto.admin;

import lombok.Data;

@Data
public class AdminTeacherWeeklyStatDTO {
	private String weekLabel;
	private int assignmentCount;
	private int feedbackCount;
}
