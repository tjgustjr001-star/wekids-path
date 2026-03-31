package com.spring.service.admin;

import java.util.Map;

public interface AdminStatsService {

    Map<String, Object> getStatsPageData(String period) throws Exception;

    Map<String, Object> getStatsDetailPageData(int classId, String period) throws Exception;
}