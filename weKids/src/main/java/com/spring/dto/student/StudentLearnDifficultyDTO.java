package com.spring.dto.student;

public class StudentLearnDifficultyDTO {

    private int feedbackId;
    private int progressId;
    private int difficultYn;
    private String feedbackContent;
    private String createdAt;

    public int getFeedbackId() {
        return feedbackId;
    }
    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getProgressId() {
        return progressId;
    }
    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    public int getDifficultYn() {
        return difficultYn;
    }
    public void setDifficultYn(int difficultYn) {
        this.difficultYn = difficultYn;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }
    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}