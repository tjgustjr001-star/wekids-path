package com.spring.dto.student;

public class StudentAssignmentSubmitDTO {
    private int subId;
    private int assignId;
    private int studentId;
    private String content;
    private String submitStatus;
    private String submitAt;
    private String lastSavedAt;
    private String returnReason;
    private int submitCount;
    private String attachedFileName;
    private String uploadPath;

    public int getSubId() { return subId; }
    public void setSubId(int subId) { this.subId = subId; }
    public int getAssignId() { return assignId; }
    public void setAssignId(int assignId) { this.assignId = assignId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSubmitStatus() { return submitStatus; }
    public void setSubmitStatus(String submitStatus) { this.submitStatus = submitStatus; }
    public String getSubmitAt() { return submitAt; }
    public void setSubmitAt(String submitAt) { this.submitAt = submitAt; }
    public String getLastSavedAt() { return lastSavedAt; }
    public void setLastSavedAt(String lastSavedAt) { this.lastSavedAt = lastSavedAt; }
    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }
    public int getSubmitCount() { return submitCount; }
    public void setSubmitCount(int submitCount) { this.submitCount = submitCount; }
    public String getAttachedFileName() { return attachedFileName; }
    public void setAttachedFileName(String attachedFileName) { this.attachedFileName = attachedFileName; }
    public String getUploadPath() { return uploadPath; }
    public void setUploadPath(String uploadPath) { this.uploadPath = uploadPath; }
}
