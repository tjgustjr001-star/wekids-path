package com.spring.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ReportAggregateDAO {

    int countTotalLearning(@Param("classId") int classId,
                           @Param("studentId") int studentId,
                           @Param("startDate") String startDate,
                           @Param("endDate") String endDate) throws Exception;

    int countCompletedLearning(@Param("classId") int classId,
                               @Param("studentId") int studentId,
                               @Param("startDate") String startDate,
                               @Param("endDate") String endDate) throws Exception;

    List<Map<String, Object>> selectLearningFeedbacks(@Param("classId") int classId,
                                                      @Param("studentId") int studentId,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate) throws Exception;

    int countTotalAssignment(@Param("classId") int classId,
                             @Param("studentId") int studentId,
                             @Param("startDate") String startDate,
                             @Param("endDate") String endDate) throws Exception;

    int countSubmittedAssignment(@Param("classId") int classId,
                                 @Param("studentId") int studentId,
                                 @Param("startDate") String startDate,
                                 @Param("endDate") String endDate) throws Exception;

    List<Map<String, Object>> selectMissingAssignments(@Param("classId") int classId,
                                                       @Param("studentId") int studentId,
                                                       @Param("startDate") String startDate,
                                                       @Param("endDate") String endDate) throws Exception;

    List<Map<String, Object>> selectAssignmentFeedbacks(@Param("classId") int classId,
                                                        @Param("studentId") int studentId,
                                                        @Param("startDate") String startDate,
                                                        @Param("endDate") String endDate) throws Exception;

    List<Integer> selectClassStudentIds(@Param("classId") int classId) throws Exception;
}