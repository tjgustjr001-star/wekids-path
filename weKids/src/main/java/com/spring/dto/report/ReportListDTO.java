package com.spring.dto.report;

public class ReportListDTO {

    private int reportId;
    private int classId;
    private Integer studentId;

    private String studentName;
    private String teacherName;

    private String reportType;     // PERSONAL / CLASS
    private String periodType;     // WEEKLY / MONTHLY

    private String title;
    private String startDate;
    private String endDate;
    private String createdAt;

    private Integer parentViewCount;
    private String parentViewedAt;

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getParentViewCount() {
        return parentViewCount;
    }

    public void setParentViewCount(Integer parentViewCount) {
        this.parentViewCount = parentViewCount;
    }

    public String getParentViewedAt() {
        return parentViewedAt;
    }

    public void setParentViewedAt(String parentViewedAt) {
        this.parentViewedAt = parentViewedAt;
    }
}