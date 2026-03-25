package com.spring.dto;

import java.util.Date;

public class ParentChildVO {

    private int linkId;
    private int studentId;
    private int parentId;
    private int linkStatus;
    private String relationType;
    private Date linkedAt;
    private Date unlinkedAt;

    private String studentName;
    private String parentName;
    private String email;
    private String profileImage;
    private String parentLinkCode;

    // 반/학급 정보
    private int year;
    private int grade;
    private int classNo;
    private String className;

    // 선생님 한마디
    private String teacherComment;

    // 학습 통계
    private int totalLearningCount;
    private int completedLearningCount;
    private int learningProgressRate;

    // 과제 통계
    private int totalAssignmentCount;
    private int submittedAssignmentCount;
    private int assignmentRate;

    // 미확인 가정통신문 수
    private int unconfirmedNoticeCount;

    public int getLinkId() {
        return linkId;
    }
    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }
    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public int getParentId() {
        return parentId;
    }
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    public int getLinkStatus() {
        return linkStatus;
    }
    public void setLinkStatus(int linkStatus) {
        this.linkStatus = linkStatus;
    }
    public String getRelationType() {
        return relationType;
    }
    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
    public Date getLinkedAt() {
        return linkedAt;
    }
    public void setLinkedAt(Date linkedAt) {
        this.linkedAt = linkedAt;
    }
    public Date getUnlinkedAt() {
        return unlinkedAt;
    }
    public void setUnlinkedAt(Date unlinkedAt) {
        this.unlinkedAt = unlinkedAt;
    }
    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public String getParentLinkCode() {
        return parentLinkCode;
    }
    public void setParentLinkCode(String parentLinkCode) {
        this.parentLinkCode = parentLinkCode;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
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
    public String getTeacherComment() {
        return teacherComment;
    }
    public void setTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
    }
    public int getTotalLearningCount() {
        return totalLearningCount;
    }
    public void setTotalLearningCount(int totalLearningCount) {
        this.totalLearningCount = totalLearningCount;
    }
    public int getCompletedLearningCount() {
        return completedLearningCount;
    }
    public void setCompletedLearningCount(int completedLearningCount) {
        this.completedLearningCount = completedLearningCount;
    }
    public int getLearningProgressRate() {
        return learningProgressRate;
    }
    public void setLearningProgressRate(int learningProgressRate) {
        this.learningProgressRate = learningProgressRate;
    }
    public int getTotalAssignmentCount() {
        return totalAssignmentCount;
    }
    public void setTotalAssignmentCount(int totalAssignmentCount) {
        this.totalAssignmentCount = totalAssignmentCount;
    }
    public int getSubmittedAssignmentCount() {
        return submittedAssignmentCount;
    }
    public void setSubmittedAssignmentCount(int submittedAssignmentCount) {
        this.submittedAssignmentCount = submittedAssignmentCount;
    }
    public int getAssignmentRate() {
        return assignmentRate;
    }
    public void setAssignmentRate(int assignmentRate) {
        this.assignmentRate = assignmentRate;
    }
    public int getUnconfirmedNoticeCount() {
        return unconfirmedNoticeCount;
    }
    public void setUnconfirmedNoticeCount(int unconfirmedNoticeCount) {
        this.unconfirmedNoticeCount = unconfirmedNoticeCount;
    }
}