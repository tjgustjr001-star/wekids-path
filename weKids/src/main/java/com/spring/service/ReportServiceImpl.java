package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.ReportDAO;
import com.spring.dto.ParentChildVO;
import com.spring.dto.report.ReportDetailDTO;
import com.spring.dto.report.ReportGenerateRequestDTO;
import com.spring.dto.report.ReportListDTO;
import com.spring.dto.report.ReportVO;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDAO reportDAO;

    @Autowired
    private ReportAggregateService reportAggregateService;

    @Override
    public void generateReports(int teacherId,
                                int classId,
                                ReportGenerateRequestDTO dto) throws Exception {

        validateTeacherClassAccess(teacherId, classId);

        if (dto == null) {
            throw new IllegalArgumentException("리포트 생성 요청이 없습니다.");
        }

        if (isBlank(dto.getReportType())) {
            throw new IllegalArgumentException("리포트 유형이 없습니다.");
        }

        if (isBlank(dto.getPeriodType())) {
            throw new IllegalArgumentException("기간 유형이 없습니다.");
        }

        if (isBlank(dto.getStartDate()) || isBlank(dto.getEndDate())) {
            throw new IllegalArgumentException("리포트 기간이 없습니다.");
        }

        String reportType = dto.getReportType().trim().toUpperCase();

        if ("PERSONAL".equals(reportType)) {
            generatePersonalReports(teacherId, classId, dto);
            return;
        }

        if ("CLASS".equals(reportType)) {
            generateClassReport(teacherId, classId, dto);
            return;
        }

        throw new IllegalArgumentException("지원하지 않는 리포트 유형입니다.");
    }

    @Override
    public List<ReportListDTO> getTeacherReportList(int teacherId,
                                                    int classId) throws Exception {
        validateTeacherClassAccess(teacherId, classId);
        List<ReportListDTO> list = reportDAO.selectTeacherReportList(teacherId, classId);
        return list == null ? new ArrayList<ReportListDTO>() : list;
    }

    @Override
    public List<ReportListDTO> getStudentReportList(int studentId,
                                                    int classId,
                                                    String periodFilter) throws Exception {
        validateStudentClassAccess(studentId, classId);
        List<ReportListDTO> list = reportDAO.selectStudentReportList(studentId, classId, periodFilter);
        return list == null ? new ArrayList<ReportListDTO>() : list;
    }

    @Override
    public List<ReportListDTO> getParentReportList(int parentId,
                                                   int classId,
                                                   Integer studentId,
                                                   String periodFilter) throws Exception {

        validateParentClassAccess(parentId, classId);

        if (studentId != null) {
            validateParentClassAccess(parentId, studentId, classId);
        }

        List<ReportListDTO> list = reportDAO.selectParentReportList(parentId, classId, studentId, periodFilter);
        return list == null ? new ArrayList<ReportListDTO>() : list;
    }

    @Override
    public List<ParentChildVO> getParentReportChildren(int parentId,
                                                       int classId) throws Exception {
        validateParentClassAccess(parentId, classId);
        List<ParentChildVO> list = reportDAO.selectParentClassChildren(parentId, classId);
        return list == null ? new ArrayList<ParentChildVO>() : list;
    }

    @Override
    public ReportDetailDTO getTeacherReportDetail(int teacherId,
                                                  int classId,
                                                  int reportId) throws Exception {

        validateTeacherClassAccess(teacherId, classId);

        ReportDetailDTO detail = reportDAO.selectReportDetail(reportId);
        validateDetailExists(detail);
        validateDetailClass(detail, classId);

        return detail;
    }

    @Override
    public ReportDetailDTO getStudentReportDetail(int studentId,
                                                  int classId,
                                                  int reportId) throws Exception {

        validateStudentClassAccess(studentId, classId);

        ReportDetailDTO detail = reportDAO.selectReportDetail(reportId);
        validateDetailExists(detail);
        validateDetailClass(detail, classId);

        if (!"CLASS".equalsIgnoreCase(detail.getReportType())) {
            if (detail.getStudentId() == null || detail.getStudentId().intValue() != studentId) {
                throw new IllegalArgumentException("해당 학생의 리포트가 아닙니다.");
            }
        }

        return detail;
    }

    @Override
    public ReportDetailDTO getParentReportDetail(int parentId,
                                                 Integer studentId,
                                                 int classId,
                                                 int reportId) throws Exception {

        validateParentClassAccess(parentId, classId);

        ReportDetailDTO detail = reportDAO.selectReportDetail(reportId);
        validateDetailExists(detail);
        validateDetailClass(detail, classId);

        if ("CLASS".equalsIgnoreCase(detail.getReportType())) {
            // 학급 리포트는 해당 클래스에 연결된 학부모면 열람 가능
        } else {
            if (studentId == null) {
                throw new IllegalArgumentException("자녀 정보가 필요합니다.");
            }

            validateParentClassAccess(parentId, studentId, classId);

            if (detail.getStudentId() == null || detail.getStudentId().intValue() != studentId.intValue()) {
                throw new IllegalArgumentException("선택한 자녀의 리포트가 아닙니다.");
            }
        }

        int exists = reportDAO.countParentReportView(reportId, parentId);
        if (exists == 0) {
            int reportViewId = reportDAO.selectNextReportViewId();
            reportDAO.insertReportView(reportViewId, reportId, parentId);
        }

        return detail;
    }

    private void generatePersonalReports(int teacherId,
                                         int classId,
                                         ReportGenerateRequestDTO dto) throws Exception {

        List<Integer> studentIds = dto.getStudentIds();

        if (studentIds == null || studentIds.isEmpty()) {
            throw new IllegalArgumentException("개인 리포트 대상 학생이 없습니다.");
        }

        for (Integer studentId : studentIds) {
            if (studentId == null) {
                continue;
            }

            validateStudentClassAccess(studentId.intValue(), classId);

            String snapshotJson = reportAggregateService.buildPersonalSnapshotJson(
                    classId,
                    studentId.intValue(),
                    dto.getStartDate(),
                    dto.getEndDate()
            );

            ReportVO vo = new ReportVO();
            vo.setReportId(reportDAO.selectNextReportId());
            vo.setClassId(classId);
            vo.setStudentId(studentId);
            vo.setTeacherId(teacherId);
            vo.setReportType("PERSONAL");
            vo.setPeriodType(dto.getPeriodType());
            vo.setStartDate(dto.getStartDate());
            vo.setEndDate(dto.getEndDate());
            vo.setTitle(buildPersonalTitle(dto.getPeriodType(), dto.getStartDate(), dto.getEndDate()));
            vo.setReportContent(snapshotJson);
            vo.setComent(dto.getComent());
            vo.setAttachUrl(null);

            reportDAO.insertReport(vo);
        }
    }

    private void generateClassReport(int teacherId,
                                     int classId,
                                     ReportGenerateRequestDTO dto) throws Exception {

        String snapshotJson = reportAggregateService.buildClassSnapshotJson(
                classId,
                dto.getStartDate(),
                dto.getEndDate()
        );

        ReportVO vo = new ReportVO();
        vo.setReportId(reportDAO.selectNextReportId());
        vo.setClassId(classId);
        vo.setStudentId(null);
        vo.setTeacherId(teacherId);
        vo.setReportType("CLASS");
        vo.setPeriodType(dto.getPeriodType());
        vo.setStartDate(dto.getStartDate());
        vo.setEndDate(dto.getEndDate());
        vo.setTitle(buildClassTitle(dto.getPeriodType(), dto.getStartDate(), dto.getEndDate()));
        vo.setReportContent(snapshotJson);
        vo.setComent(dto.getComent());
        vo.setAttachUrl(null);

        reportDAO.insertReport(vo);
    }

    private String buildPersonalTitle(String periodType, String startDate, String endDate) {
        if ("MONTHLY".equalsIgnoreCase(periodType)) {
            return "월간 개인 리포트";
        }
        return "주간 개인 리포트";
    }

    private String buildClassTitle(String periodType, String startDate, String endDate) {
        if ("MONTHLY".equalsIgnoreCase(periodType)) {
            return "월간 학급 요약 리포트";
        }
        return "주간 학급 요약 리포트";
    }

    private void validateTeacherClassAccess(int teacherId, int classId) throws Exception {
        int count = reportDAO.countTeacherClass(teacherId, classId);
        if (count <= 0) {
            throw new IllegalArgumentException("해당 클래스에 접근할 수 없는 교사입니다.");
        }
    }

    private void validateStudentClassAccess(int studentId, int classId) throws Exception {
        int count = reportDAO.countStudentClass(studentId, classId);
        if (count <= 0) {
            throw new IllegalArgumentException("해당 클래스에 속한 학생이 아닙니다.");
        }
    }

    private void validateParentClassAccess(int parentId, int studentId, int classId) throws Exception {
        int count = reportDAO.countParentStudentClass(parentId, studentId, classId);
        if (count <= 0) {
            throw new IllegalArgumentException("해당 자녀의 클래스에 접근할 수 없습니다.");
        }
    }

    private void validateParentClassAccess(int parentId, int classId) throws Exception {
        int count = reportDAO.countParentClass(parentId, classId);
        if (count <= 0) {
            throw new IllegalArgumentException("해당 클래스에 접근할 수 없습니다.");
        }
    }

    private void validateDetailExists(ReportDetailDTO detail) {
        if (detail == null) {
            throw new IllegalArgumentException("리포트를 찾을 수 없습니다.");
        }
    }

    private void validateDetailClass(ReportDetailDTO detail, int classId) {
        if (detail.getClassId() != classId) {
            throw new IllegalArgumentException("다른 클래스의 리포트입니다.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}