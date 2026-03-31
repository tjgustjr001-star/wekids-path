package com.spring.dao.admin;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface AdminStatsDAO {

    Map<String, Object> selectDashboardOverview() throws SQLException;

    List<Map<String, Object>> selectUserTypeList() throws SQLException;

    List<Map<String, Object>> selectUserGrowthList(String period) throws SQLException;

    List<Map<String, Object>> selectClassStatsList() throws SQLException;

    Map<String, Object> selectClassDetailSummary(int classId) throws SQLException;

    List<Map<String, Object>> selectSubmissionTrendList(int classId) throws SQLException;

    List<Map<String, Object>> selectClassLearnStatusList(int classId) throws SQLException;

    List<Map<String, Object>> selectStudentStatsList(int classId) throws SQLException;
}