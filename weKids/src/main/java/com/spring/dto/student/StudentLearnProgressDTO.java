package com.spring.dto.student;

public class StudentLearnProgressDTO {

    private int progressId;
    private int learnId;
    private int studentId;

    private String status;        // IN_PROGRESS / COMPLETED
    private int progressRate;     // 0~100
    private String startedAt;
    private String completedAt;
    private String updatedAt;

    public int getProgressId() {
        return progressId;
    }
    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    public int getLearnId() {
        return learnId;
    }
    public void setLearnId(int learnId) {
        this.learnId = learnId;
    }

    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgressRate() {
        return progressRate;
    }
    public void setProgressRate(int progressRate) {
        this.progressRate = progressRate;
    }

    public String getStartedAt() {
        return startedAt;
    }
    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }
    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}