package com.spring.service;

public interface ReportAggregateService {

    String buildPersonalSnapshotJson(int classId,
                                     int studentId,
                                     String startDate,
                                     String endDate) throws Exception;

    String buildClassSnapshotJson(int classId,
                                  String startDate,
                                  String endDate) throws Exception;
}