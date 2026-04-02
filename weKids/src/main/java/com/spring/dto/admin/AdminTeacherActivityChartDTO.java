package com.spring.dto.admin;

import java.util.List;

import lombok.Data;
@Data
public class AdminTeacherActivityChartDTO {
	
	private List<String> labels;
	private List<Integer> assignmentCounts;
	private List<Integer> feedbackCounts;
	}
