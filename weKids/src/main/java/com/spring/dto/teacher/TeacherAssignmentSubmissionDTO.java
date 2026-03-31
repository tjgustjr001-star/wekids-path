package com.spring.dto.teacher;

public class TeacherAssignmentSubmissionDTO {
    private int studentId;
    private String studentName;
    private String nameFirst;
    private String profileImage;
    private String submitStatus;
    private String submitAt;
    private int reviseCount;
    private boolean feedbackDone;
    private String feedbackContent;
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getNameFirst() { return nameFirst; }
    public void setNameFirst(String nameFirst) { this.nameFirst = nameFirst; }
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    public String getSubmitStatus() { return submitStatus; }
    public void setSubmitStatus(String submitStatus) { this.submitStatus = submitStatus; }
    public String getSubmitAt() { return submitAt; }
    public void setSubmitAt(String submitAt) { this.submitAt = submitAt; }
    public int getReviseCount() { return reviseCount; }
    public void setReviseCount(int reviseCount) { this.reviseCount = reviseCount; }
    public boolean isFeedbackDone() { return feedbackDone; }
    public void setFeedbackDone(boolean feedbackDone) { this.feedbackDone = feedbackDone; }
    public String getFeedbackContent() { return feedbackContent; }
    public void setFeedbackContent(String feedbackContent) { this.feedbackContent = feedbackContent; }
}
