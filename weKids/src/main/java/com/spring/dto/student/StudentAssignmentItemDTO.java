package com.spring.dto.student;

public class StudentAssignmentItemDTO {
    private int id;
    private int classId;
    private int teacherId;
    private int studentId;
    private String title;
    private String subject;
    private String submitFormat;
    private String submitFormatLabel;
    private String status;
    private String deadline;
    private String deadlineValue;
    private String content;
    private boolean submitted;
    private String submittedAt;
    private String feedback;
    private String feedbackAt;
    private String mySubmission;
    private String attachedFile;
    private String uploadPath;
    private long fileSize;
    private int submitCount;
    private int maxEditCount;
    private int remainingEditCount;
    private boolean expired;
    private boolean canSubmit;
    private boolean canResubmit;
    private boolean canDownload;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }
    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getSubmitFormat() { return submitFormat; }
    public void setSubmitFormat(String submitFormat) { this.submitFormat = submitFormat; }
    public String getSubmitFormatLabel() { return submitFormatLabel; }
    public void setSubmitFormatLabel(String submitFormatLabel) { this.submitFormatLabel = submitFormatLabel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getDeadlineValue() { return deadlineValue; }
    public void setDeadlineValue(String deadlineValue) { this.deadlineValue = deadlineValue; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public boolean isSubmitted() { return submitted; }
    public void setSubmitted(boolean submitted) { this.submitted = submitted; }
    public String getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public String getFeedbackAt() { return feedbackAt; }
    public void setFeedbackAt(String feedbackAt) { this.feedbackAt = feedbackAt; }
    public String getMySubmission() { return mySubmission; }
    public void setMySubmission(String mySubmission) { this.mySubmission = mySubmission; }
    public String getAttachedFile() { return attachedFile; }
    public void setAttachedFile(String attachedFile) { this.attachedFile = attachedFile; }
    public String getUploadPath() { return uploadPath; }
    public void setUploadPath(String uploadPath) { this.uploadPath = uploadPath; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public int getSubmitCount() { return submitCount; }
    public void setSubmitCount(int submitCount) { this.submitCount = submitCount; }
    public int getMaxEditCount() { return maxEditCount; }
    public void setMaxEditCount(int maxEditCount) { this.maxEditCount = maxEditCount; }
    public int getRemainingEditCount() { return remainingEditCount; }
    public void setRemainingEditCount(int remainingEditCount) { this.remainingEditCount = remainingEditCount; }
    public boolean isExpired() { return expired; }
    public void setExpired(boolean expired) { this.expired = expired; }
    public boolean isCanSubmit() { return canSubmit; }
    public void setCanSubmit(boolean canSubmit) { this.canSubmit = canSubmit; }
    public boolean isCanResubmit() { return canResubmit; }
    public void setCanResubmit(boolean canResubmit) { this.canResubmit = canResubmit; }
    public boolean isCanDownload() { return canDownload; }
    public void setCanDownload(boolean canDownload) { this.canDownload = canDownload; }
}
