package com.spring.service;

import java.util.List;

import com.spring.dto.ParentChildVO;
import com.spring.dto.report.ReportDetailDTO;
import com.spring.dto.report.ReportGenerateRequestDTO;
import com.spring.dto.report.ReportListDTO;

public interface ReportService {

    void generateReports(int teacherId,
                         int classId,
                         ReportGenerateRequestDTO dto) throws Exception;

    List<ReportListDTO> getTeacherReportList(int teacherId,
                                             int classId) throws Exception;

    List<ReportListDTO> getStudentReportList(int studentId,
                                             int classId,
                                             String periodFilter) throws Exception;

    List<ReportListDTO> getParentReportList(int parentId,
                                            int classId,
                                            Integer studentId,
                                            String periodFilter) throws Exception;

    List<ParentChildVO> getParentReportChildren(int parentId,
                                                int classId) throws Exception;

    ReportDetailDTO getTeacherReportDetail(int teacherId,
                                           int classId,
                                           int reportId) throws Exception;

    ReportDetailDTO getStudentReportDetail(int studentId,
                                           int classId,
                                           int reportId) throws Exception;

    ReportDetailDTO getParentReportDetail(int parentId,
                                          Integer studentId,
                                          int classId,
                                          int reportId) throws Exception;
}