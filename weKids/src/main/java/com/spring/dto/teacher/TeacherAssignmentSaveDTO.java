package com.spring.dto.teacher;

public class TeacherAssignmentSaveDTO {
    private String title;
    private String subject;
    private String status;
    private String deadline;
    private String submitFormat;
    private Integer maxEditCount;
    private String content;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getSubmitFormat() { return submitFormat; }
    public void setSubmitFormat(String submitFormat) { this.submitFormat = submitFormat; }
    public Integer getMaxEditCount() { return maxEditCount; }
    public void setMaxEditCount(Integer maxEditCount) { this.maxEditCount = maxEditCount; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
