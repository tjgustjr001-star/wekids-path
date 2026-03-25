package com.spring.dto.teacher;

public class TeacherStudentManageDTO {

    private int studentId;
    private int studentNo;
    private String studentName;
    private String nameFirst;

    private String phone;
    private String profileImage;
    private String intro;
    private String parentLinkCode;

    private int parentLinked;
    private String parentRelationType;

    private String lastLoginAtText;

    private int learningProgressRate;
    private int totalLearningCount;
    private int completedLearningCount;

    private int totalAssignmentCount;
    private int submittedAssignmentCount;
    private int assignmentSubmitRate;

    private int studentClassId;
    private String observationMemo;
    private String tagsCsv;
    
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(int studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getParentLinkCode() {
        return parentLinkCode;
    }

    public void setParentLinkCode(String parentLinkCode) {
        this.parentLinkCode = parentLinkCode;
    }

    public int getParentLinked() {
        return parentLinked;
    }

    public void setParentLinked(int parentLinked) {
        this.parentLinked = parentLinked;
    }

    public String getParentRelationType() {
        return parentRelationType;
    }

    public void setParentRelationType(String parentRelationType) {
        this.parentRelationType = parentRelationType;
    }

    public String getLastLoginAtText() {
        return lastLoginAtText;
    }

    public void setLastLoginAtText(String lastLoginAtText) {
        this.lastLoginAtText = lastLoginAtText;
    }

    public int getLearningProgressRate() {
        return learningProgressRate;
    }

    public void setLearningProgressRate(int learningProgressRate) {
        this.learningProgressRate = learningProgressRate;
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

    public int getAssignmentSubmitRate() {
        return assignmentSubmitRate;
    }

    public void setAssignmentSubmitRate(int assignmentSubmitRate) {
        this.assignmentSubmitRate = assignmentSubmitRate;
    }
    
    public int getStudentClassId() {
        return studentClassId;
    }

    public void setStudentClassId(int studentClassId) {
        this.studentClassId = studentClassId;
    }

    public String getObservationMemo() {
        return observationMemo;
    }

    public void setObservationMemo(String observationMemo) {
        this.observationMemo = observationMemo;
    }

    public String getTagsCsv() {
        return tagsCsv;
    }

    public void setTagsCsv(String tagsCsv) {
        this.tagsCsv = tagsCsv;
    }
}