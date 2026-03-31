package com.spring.dto.teacher;

public class TeacherAssignmentManageDTO {
    private int id;
    private int classId;
    private int teacherId;
    private String title;
    private String content;
    private String subject;
    private String submitFormat;
    private String status;
    private String deadline;
    private String deadlineValue;
    private int submitCount;
    private int maxEditCount;
    private int totalCount;
    private int needFeedback;
    private boolean deleted;
    private String deletedAt;
    private int progressPercent;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }
    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getSubmitFormat() { return submitFormat; }
    public void setSubmitFormat(String submitFormat) { this.submitFormat = submitFormat; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getDeadlineValue() { return deadlineValue; }
    public void setDeadlineValue(String deadlineValue) { this.deadlineValue = deadlineValue; }
    public int getSubmitCount() { return submitCount; }
    public void setSubmitCount(int submitCount) { this.submitCount = submitCount; }
    public int getMaxEditCount() { return maxEditCount; }
    public void setMaxEditCount(int maxEditCount) { this.maxEditCount = maxEditCount; }
    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    public int getNeedFeedback() { return needFeedback; }
    public void setNeedFeedback(int needFeedback) { this.needFeedback = needFeedback; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    public String getDeletedAt() { return deletedAt; }
    public void setDeletedAt(String deletedAt) { this.deletedAt = deletedAt; }
    public int getProgressPercent() { return progressPercent; }
    public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }
}
