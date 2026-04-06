package com.spring.service.admin;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.dao.admin.AdminStatsDAO;

@Service
public class AdminStatsServiceImpl implements AdminStatsService {

	@Autowired
	private AdminStatsDAO adminStatsDAO;

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final DecimalFormat numberFormat = new DecimalFormat("#,##0");

	@Override
	public Map<String, Object> getStatsPageData(String period) throws Exception {
		String safePeriod = normalizePeriod(period);

		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> dashboardStat = adminStatsDAO.selectDashboardOverview();
		if (dashboardStat == null) {
			dashboardStat = new HashMap<String, Object>();
		}

		int totalUsers = toInt(dashboardStat.get("totalUsers"));
		int activeClassCount = toInt(dashboardStat.get("activeClassCount"));
		int submittedAssignmentCount = toInt(dashboardStat.get("submittedAssignmentCount"));
		int completedLearnCount = toInt(dashboardStat.get("completedLearnCount"));
		int totalLearnTargetCount = toInt(dashboardStat.get("totalLearnTargetCount"));

		int learnCompleteRate = calcPercent(completedLearnCount, totalLearnTargetCount);

		dashboardStat.put("totalUsers", totalUsers);
		dashboardStat.put("activeClassCount", activeClassCount);
		dashboardStat.put("submittedAssignmentCount", submittedAssignmentCount);
		dashboardStat.put("completedLearnCount", completedLearnCount);
		dashboardStat.put("totalLearnTargetCount", totalLearnTargetCount);
		dashboardStat.put("learnCompleteRate", learnCompleteRate);

		dashboardStat.put("userChangeText", "역할 4종 운영");
		dashboardStat.put("activeClassChangeText", "운영중 " + numberFormat.format(activeClassCount) + "개");
		dashboardStat.put("assignmentSubmitChangeText", "전체 제출 " + numberFormat.format(submittedAssignmentCount) + "건");
		dashboardStat.put("learnCompleteRateText",
				numberFormat.format(completedLearnCount) + " / " + numberFormat.format(totalLearnTargetCount));

		List<Map<String, Object>> userTypeList = adminStatsDAO.selectUserTypeList();
		if (userTypeList == null) {
			userTypeList = new ArrayList<Map<String, Object>>();
		}
		decorateUserTypeList(userTypeList);

		List<Map<String, Object>> userGrowthList = adminStatsDAO.selectUserGrowthList(safePeriod);
		if (userGrowthList == null) {
		    userGrowthList = new ArrayList<Map<String, Object>>();
		}

		if ("daily".equals(safePeriod)) {
		    userGrowthList = normalizeDailyUserGrowth(userGrowthList);
		} else if ("week".equals(safePeriod)) {
		    userGrowthList = normalizeWeeklyUserGrowthStrict(userGrowthList);
		}

		List<Map<String, Object>> classStatsList = adminStatsDAO.selectClassStatsList();
		if (classStatsList == null) {
			classStatsList = new ArrayList<Map<String, Object>>();
		}
		decorateClassStatsList(classStatsList);

		List<Map<String, Object>> assignmentRateTopList = buildAssignmentRateTopList(classStatsList);
		List<Map<String, Object>> learnCompletionTopList = buildLearnCompletionTopList(classStatsList);

		result.put("period", safePeriod);
		result.put("dashboardStat", dashboardStat);
		result.put("userTypeList", userTypeList);
		result.put("classStatsList", classStatsList);
		result.put("classStatsCount", classStatsList.size());

		result.put("userGrowthJson", toJson(userGrowthList));
		result.put("userTypeJson", toJson(userTypeList));
		result.put("assignmentRateJson", toJson(assignmentRateTopList));
		result.put("learnCompletionJson", toJson(learnCompletionTopList));

		return result;
	}

	@Override
	public Map<String, Object> getStatsDetailPageData(int classId, String period) throws Exception {
		String safePeriod = normalizePeriod(period);

		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> detailSummary = adminStatsDAO.selectClassDetailSummary(classId);
		if (detailSummary == null) {
			detailSummary = new HashMap<String, Object>();
			detailSummary.put("classId", classId);
			detailSummary.put("classDisplayName", "알 수 없는 클래스");
			detailSummary.put("classStatus", "BLINDED");
			detailSummary.put("studentCount", 0);
			detailSummary.put("teacherName", "-");
			detailSummary.put("createdDateText", "-");
		}

		List<Map<String, Object>> submissionTrendList = adminStatsDAO.selectSubmissionTrendList(classId);
		if (submissionTrendList == null) {
			submissionTrendList = new ArrayList<Map<String, Object>>();
		}

		List<Map<String, Object>> classLearnStatusList = adminStatsDAO.selectClassLearnStatusList(classId);
		if (classLearnStatusList == null) {
			classLearnStatusList = new ArrayList<Map<String, Object>>();
		}

		List<Map<String, Object>> studentStatsList = adminStatsDAO.selectStudentStatsList(classId);
		if (studentStatsList == null) {
			studentStatsList = new ArrayList<Map<String, Object>>();
		}
		decorateStudentStatsList(studentStatsList);

		result.put("period", safePeriod);
		result.put("detailSummary", detailSummary);
		result.put("studentStatsList", studentStatsList);
		result.put("submissionTrendJson", toJson(submissionTrendList));
		result.put("classLearnStatusJson", toJson(classLearnStatusList));

		return result;
	}

	private void decorateUserTypeList(List<Map<String, Object>> userTypeList) {
		for (Map<String, Object> item : userTypeList) {
			String roleCode = nvl(item.get("roleCode"));
			item.put("name", roleName(roleCode));
			item.put("color", roleColor(roleCode));
			item.put("value", toInt(item.get("value")));
		}
	}

	private void decorateClassStatsList(List<Map<String, Object>> classStatsList) {
		for (Map<String, Object> row : classStatsList) {
			int studentCount = toInt(row.get("studentCount"));
			int totalLearnCount = toInt(row.get("totalLearnCount"));
			int completedLearnCount = toInt(row.get("completedLearnCount"));
			int totalAssignmentCount = toInt(row.get("totalAssignmentCount"));
			int submittedAssignmentCount = toInt(row.get("submittedAssignmentCount"));

			int totalAssignmentTargetCount = studentCount * totalAssignmentCount;
			int assignmentSubmitRate = calcPercent(submittedAssignmentCount, totalAssignmentTargetCount);

			row.put("studentCount", studentCount);
			row.put("totalLearnCount", totalLearnCount);
			row.put("completedLearnCount", completedLearnCount);
			row.put("totalAssignmentCount", totalAssignmentCount);
			row.put("submittedAssignmentCount", submittedAssignmentCount);
			row.put("assignmentSubmitRate", assignmentSubmitRate);

			String classDisplayName = nvl(row.get("classDisplayName"));
			if (classDisplayName.isEmpty()) {
				row.put("classDisplayName", "클래스 " + toInt(row.get("classId")));
			}
		}
	}

	private List<Map<String, Object>> buildAssignmentRateTopList(List<Map<String, Object>> classStatsList) {
		List<Map<String, Object>> copy = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> row : classStatsList) {
			Map<String, Object> item = new HashMap<String, Object>();
			int studentCount = toInt(row.get("studentCount"));
			int totalAssignmentCount = toInt(row.get("totalAssignmentCount"));
			int submittedAssignmentCount = toInt(row.get("submittedAssignmentCount"));

			int totalTarget = studentCount * totalAssignmentCount;
			int unsubmittedCount = Math.max(totalTarget - submittedAssignmentCount, 0);

			item.put("className", row.get("classDisplayName"));
			item.put("submittedCount", submittedAssignmentCount);
			item.put("unsubmittedCount", unsubmittedCount);
			item.put("sortValue", submittedAssignmentCount);

			copy.add(item);
		}

		Collections.sort(copy, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> a, Map<String, Object> b) {
				return Integer.compare(toInt(b.get("sortValue")), toInt(a.get("sortValue")));
			}
		});

		return trimTop(copy, 5);
	}

	private List<Map<String, Object>> buildLearnCompletionTopList(List<Map<String, Object>> classStatsList) {
		List<Map<String, Object>> copy = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> row : classStatsList) {
			Map<String, Object> item = new HashMap<String, Object>();
			int studentCount = toInt(row.get("studentCount"));
			int totalLearnCount = toInt(row.get("totalLearnCount"));
			int completedLearnCount = toInt(row.get("completedLearnCount"));

			int totalTarget = studentCount * totalLearnCount;
			int incompleteCount = Math.max(totalTarget - completedLearnCount, 0);

			item.put("className", row.get("classDisplayName"));
			item.put("completedCount", completedLearnCount);
			item.put("incompleteCount", incompleteCount);
			item.put("sortValue", completedLearnCount);

			copy.add(item);
		}

		Collections.sort(copy, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> a, Map<String, Object> b) {
				return Integer.compare(toInt(b.get("sortValue")), toInt(a.get("sortValue")));
			}
		});

		return trimTop(copy, 5);
	}

	private void decorateStudentStatsList(List<Map<String, Object>> studentStatsList) {
		for (Map<String, Object> row : studentStatsList) {
			int totalLearnCount = toInt(row.get("totalLearnCount"));
			int completedLearnCount = toInt(row.get("completedLearnCount"));
			int totalAssignmentCount = toInt(row.get("totalAssignmentCount"));
			int submittedAssignmentCount = toInt(row.get("submittedAssignmentCount"));

			int learnProgressRate = calcPercent(completedLearnCount, totalLearnCount);
			int assignmentSubmitRate = calcPercent(submittedAssignmentCount, totalAssignmentCount);

			row.put("totalLearnCount", totalLearnCount);
			row.put("completedLearnCount", completedLearnCount);
			row.put("totalAssignmentCount", totalAssignmentCount);
			row.put("submittedAssignmentCount", submittedAssignmentCount);
			row.put("learnProgressRate", learnProgressRate);
			row.put("assignmentSubmitRate", assignmentSubmitRate);

			if (learnProgressRate >= 90 && assignmentSubmitRate >= 90) {
				row.put("statusCode", "GOOD");
				row.put("statusText", "우수");
			} else if (learnProgressRate < 50 || assignmentSubmitRate < 50) {
				row.put("statusCode", "WARN");
				row.put("statusText", "주의");
			} else {
				row.put("statusCode", "NORMAL");
				row.put("statusText", "정상");
			}

			String lastAssignmentStatusText = nvl(row.get("lastAssignmentStatusText"));
			if (lastAssignmentStatusText.isEmpty()) {
				row.put("lastAssignmentStatusText", "-");
			}
		}
	}
	

	private List<Map<String, Object>> trimTop(List<Map<String, Object>> list, int size) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size() && i < size; i++) {
			Map<String, Object> item = new HashMap<String, Object>(list.get(i));
			item.remove("sortValue");
			result.add(item);
		}
		return result;
	}

	private int calcPercent(int numerator, int denominator) {
		if (denominator <= 0) {
			return 0;
		}
		return (int) Math.round((numerator * 100.0) / denominator);
	}

	private int toInt(Object value) {
		if (value == null)
			return 0;
		if (value instanceof Number)
			return ((Number) value).intValue();
		try {
			String text = String.valueOf(value).trim();
			if (text.isEmpty())
				return 0;
			return (int) Math.round(Double.parseDouble(text));
		} catch (Exception e) {
			return 0;
		}
	}

	private String nvl(Object value) {
		return value == null ? "" : String.valueOf(value);
	}

	private String normalizePeriod(String period) {
		if ("daily".equals(period) || "week".equals(period) || "month".equals(period) || "year".equals(period)) {
			return period;
		}

		return "daily";
	}
	
	private List<Map<String, Object>> normalizeDailyUserGrowth(List<Map<String, Object>> rawList) {
	    Map<String, Map<String, Object>> rawMap = new HashMap<String, Map<String, Object>>();

	    for (Map<String, Object> row : rawList) {
	        String label = nvl(row.get("label"));
	        rawMap.put(label, row);
	    }

	    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

	    java.time.LocalDate today = java.time.LocalDate.now();
	    java.time.format.DateTimeFormatter labelFormatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd");

	    for (int i = 6; i >= 0; i--) {
	        java.time.LocalDate targetDate = today.minusDays(i);
	        String label = targetDate.format(labelFormatter);

	        Map<String, Object> source = rawMap.get(label);

	        Map<String, Object> item = new HashMap<String, Object>();
	        item.put("label", label);
	        item.put("studentCount", source != null ? toInt(source.get("studentCount")) : 0);
	        item.put("parentCount", source != null ? toInt(source.get("parentCount")) : 0);
	        item.put("teacherCount", source != null ? toInt(source.get("teacherCount")) : 0);

	        result.add(item);
	    }

	    return result;
	}
	private List<Map<String, Object>> normalizeWeeklyUserGrowthStrict(List<Map<String, Object>> rawList) {
	    Map<String, Map<String, Object>> rawMap = new HashMap<String, Map<String, Object>>();

	    for (Map<String, Object> row : rawList) {
	        rawMap.put(nvl(row.get("label")), row);
	    }

	    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

	    java.time.LocalDate today = java.time.LocalDate.now();
	    java.time.LocalDate currentWeekStart = today.with(java.time.DayOfWeek.MONDAY);
	    java.time.format.DateTimeFormatter keyFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    for (int i = 4; i >= 0; i--) {
	        java.time.LocalDate weekStart = currentWeekStart.minusWeeks(i);
	        String key = weekStart.format(keyFormatter);

	        Map<String, Object> source = rawMap.get(key);

	        Map<String, Object> item = new HashMap<String, Object>();
	        item.put("label", (5 - i) + "주");
	        item.put("studentCount", source != null ? toInt(source.get("studentCount")) : 0);
	        item.put("parentCount", source != null ? toInt(source.get("parentCount")) : 0);
	        item.put("teacherCount", source != null ? toInt(source.get("teacherCount")) : 0);

	        result.add(item);
	    }

	    return result;
	}

	

	private String roleName(String roleCode) {
		if ("STUDENT".equals(roleCode))
			return "학생";
		if ("PARENT".equals(roleCode))
			return "학부모";
		if ("TEACHER".equals(roleCode))
			return "교사";
		if ("ADMIN".equals(roleCode))
			return "관리자";
		return roleCode;
	}

	private String roleColor(String roleCode) {
		if ("STUDENT".equals(roleCode))
			return "#84cc16";
		if ("PARENT".equals(roleCode))
			return "#ff7878";
		if ("TEACHER".equals(roleCode))
			return "#5b86ff";
		if ("ADMIN".equals(roleCode))
			return "#cbd5e1";
		return "#94a3b8";
	}

	private String toJson(Object value) throws SQLException {
		try {
			return objectMapper.writeValueAsString(value);
		} catch (Exception e) {
			throw new SQLException("통계 JSON 변환 중 오류가 발생했습니다.", e);
		}
	}
}