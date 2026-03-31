package com.spring.dto.teacher;

public class TeacherAssignmentStudentWorkDTO {
    private int subId;
    private int studentId;
    private String studentName;
    private String nameFirst;
    private String profileImage;
    private String submitStatus;
    private String submitAt;
    private int submitCount;
    private int reviseCount;
    private int maxEditCount;
    private int remainingEditCount;
    private String content;
    private String attachedFileName;
    private String uploadPath;
    private long fileSize;
    private String returnReason;
    private String feedbackContent;
    private String feedbackCreatedAt;
    private boolean submitted;
    private boolean canDownload;

    public int getSubId() { return subId; }
    public void setSubId(int subId) { this.subId = subId; }
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
    public int getSubmitCount() { return submitCount; }
    public void setSubmitCount(int submitCount) { this.submitCount = submitCount; }
    public int getReviseCount() { return reviseCount; }
    public void setReviseCount(int reviseCount) { this.reviseCount = reviseCount; }
    public int getMaxEditCount() { return maxEditCount; }
    public void setMaxEditCount(int maxEditCount) { this.maxEditCount = maxEditCount; }
    public int getRemainingEditCount() { return remainingEditCount; }
    public void setRemainingEditCount(int remainingEditCount) { this.remainingEditCount = remainingEditCount; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getAttachedFileName() { return attachedFileName; }
    public void setAttachedFileName(String attachedFileName) { this.attachedFileName = attachedFileName; }
    public String getUploadPath() { return uploadPath; }
    public void setUploadPath(String uploadPath) { this.uploadPath = uploadPath; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }
    public String getFeedbackContent() { return feedbackContent; }
    public void setFeedbackContent(String feedbackContent) { this.feedbackContent = feedbackContent; }
    public String getFeedbackCreatedAt() { return feedbackCreatedAt; }
    public void setFeedbackCreatedAt(String feedbackCreatedAt) { this.feedbackCreatedAt = feedbackCreatedAt; }
    public boolean isSubmitted() { return submitted; }
    public void setSubmitted(boolean submitted) { this.submitted = submitted; }
    public boolean isCanDownload() { return canDownload; }
    public void setCanDownload(boolean canDownload) { this.canDownload = canDownload; }
}
