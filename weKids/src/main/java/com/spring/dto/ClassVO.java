package com.spring.dto;

public class ClassVO {

    private int classId;
    private int year;
    private int semester;
    private int grade;
    private int classNo;
    private String className;
    private String classStatus;
    private int allowSubmissionModifyYn;
    private String inviteCode;
    private String defaultDueTime;
    private String description;

    private String teacherName;
    private int memberCount;
    private String yearLabel;
    private String coverType;

    private String childName;   // 추가

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
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

    public int getAllowSubmissionModifyYn() {
        return allowSubmissionModifyYn;
    }

    public void setAllowSubmissionModifyYn(int allowSubmissionModifyYn) {
        this.allowSubmissionModifyYn = allowSubmissionModifyYn;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getDefaultDueTime() {
        return defaultDueTime;
    }

    public void setDefaultDueTime(String defaultDueTime) {
        this.defaultDueTime = defaultDueTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getYearLabel() {
        return yearLabel;
    }

    public void setYearLabel(String yearLabel) {
        this.yearLabel = yearLabel;
    }

    public String getCoverType() {
        return coverType;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }

    public String getChildName() {   // 추가
        return childName;
    }

    public void setChildName(String childName) {   // 추가
        this.childName = childName;
    }
}