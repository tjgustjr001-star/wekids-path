package com.spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.report.ReportDetailDTO;
import com.spring.dto.report.ReportListDTO;
import com.spring.dto.report.ReportVO;

public interface ReportDAO {

    int selectNextReportId() throws Exception;

    int selectNextReportViewId() throws Exception;

    void insertReport(ReportVO vo) throws Exception;

    List<ReportListDTO> selectTeacherReportList(@Param("teacherId") int teacherId,
                                                @Param("classId") int classId) throws Exception;

    List<ReportListDTO> selectStudentReportList(@Param("studentId") int studentId,
                                                @Param("classId") int classId,
                                                @Param("periodFilter") String periodFilter) throws Exception;

    List<ReportListDTO> selectParentReportList(@Param("parentId") int parentId,
                                               @Param("classId") int classId,
                                               @Param("studentId") Integer studentId,
                                               @Param("periodFilter") String periodFilter) throws Exception;

    ReportDetailDTO selectReportDetail(@Param("reportId") int reportId) throws Exception;

    int countParentReportView(@Param("reportId") int reportId,
                              @Param("viewerId") int viewerId) throws Exception;

    void insertReportView(@Param("reportViewId") int reportViewId,
                          @Param("reportId") int reportId,
                          @Param("viewerId") int viewerId) throws Exception;

    int countTeacherClass(@Param("teacherId") int teacherId,
                          @Param("classId") int classId) throws Exception;

    int countStudentClass(@Param("studentId") int studentId,
                          @Param("classId") int classId) throws Exception;

    int countParentStudentClass(@Param("parentId") int parentId,
                                @Param("studentId") int studentId,
                                @Param("classId") int classId) throws Exception;
}