package com.spring.dto.teacher;

public class TeacherLearnDifficultyDTO {

    private int learnFbdId;
    private int progressId;
    private int studentId;
    private String studentName;
    private String profileImage;
    private String feedbackContent;
    private String createdAt;
    private String updatedAt;
    private String status;
    private int progressRate;

    public int getLearnFbdId() { return learnFbdId; }
    public void setLearnFbdId(int learnFbdId) { this.learnFbdId = learnFbdId; }

    public int getProgressId() { return progressId; }
    public void setProgressId(int progressId) { this.progressId = progressId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getFeedbackContent() { return feedbackContent; }
    public void setFeedbackContent(String feedbackContent) { this.feedbackContent = feedbackContent; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getProgressRate() { return progressRate; }
    public void setProgressRate(int progressRate) { this.progressRate = progressRate; }
}