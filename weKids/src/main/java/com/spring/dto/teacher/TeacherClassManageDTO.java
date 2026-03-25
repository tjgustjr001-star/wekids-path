package com.spring.dto.teacher;

public class TeacherClassManageDTO {

    private String className;
    private String classStatus;
    private int grade;
    private int classNo;
    private String description;

    private String defaultDueTime;
    private int allowSubmissionModifyYn;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassStatus() {
        return classStatus;
    }

    public void setClassStatus(String classStatus) {
        this.classStatus = classStatus;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getClassNo() {
        return classNo;
    }

    public void setClassNo(int classNo) {
        this.classNo = classNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultDueTime() {
        return defaultDueTime;
    }

    public void setDefaultDueTime(String defaultDueTime) {
        this.defaultDueTime = defaultDueTime;
    }

    public int getAllowSubmissionModifyYn() {
        return allowSubmissionModifyYn;
    }

    public void setAllowSubmissionModifyYn(int allowSubmissionModifyYn) {
        this.allowSubmissionModifyYn = allowSubmissionModifyYn;
    }
}