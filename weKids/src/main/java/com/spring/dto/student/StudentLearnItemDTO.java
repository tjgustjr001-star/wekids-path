package com.spring.dto.student;

public class StudentLearnItemDTO {

    private int id;
    private int classId;
    private int studentId;

    private String title;
    private String type;          // 영상 / 지문읽기 / 링크 / 파일
    private boolean required;

    private String status;        // 미시작 / 진행중 / 완료
    private String deadline;      // yyyy-MM-dd HH:mm
    private String duration;      // 15분
    private int progress;         // 0 ~ 100

    private String content;       // 설명
    private String textContent;   // 지문 내용
    private String linkUrl;       // 링크형일 때
    private String startDate;     // yyyy-MM-dd

    private String lastPosition; //마지막학습위치

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTextContent() { return textContent; }
    public void setTextContent(String textContent) { this.textContent = textContent; }

    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    
    public String getLastPosition() { return lastPosition; }
    public void setLastPosition(String lastPosition) { this.lastPosition = lastPosition; }
    
}