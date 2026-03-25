package com.spring.dto.teacher;

public class TeacherLearnSaveDTO {

    private String title;
    private String type;
    private String required;
    private String status;
    private Integer duration;
    private String startDate;
    private String deadline;
    private String linkUrl;
    private String textContent;
    private String content;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRequired() { return required; }
    public void setRequired(String required) { this.required = required; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }

    public String getTextContent() { return textContent; }
    public void setTextContent(String textContent) { this.textContent = textContent; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}