package com.spring.dto.teacher;

public class TeacherLearnProgressDTO {

    private int studentId;
    private String studentName;
    private String profileImage;
    private String status;
    private int progressRate;
    private String lastAccessedAt;
    private int totalMinute;

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getProgressRate() { return progressRate; }
    public void setProgressRate(int progressRate) { this.progressRate = progressRate; }

    public String getLastAccessedAt() { return lastAccessedAt; }
    public void setLastAccessedAt(String lastAccessedAt) { this.lastAccessedAt = lastAccessedAt; }

    public int getTotalMinute() { return totalMinute; }
    public void setTotalMinute(int totalMinute) { this.totalMinute = totalMinute; }
}