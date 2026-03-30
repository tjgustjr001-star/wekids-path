package com.spring.dto.teacher;

public class TeacherLearnManageDTO {

    private int id;
    private int learnListId;
    private int classId;
    private int teacherId;

    private String title;
    private String type;
    private boolean required;
    private String status;
    private String manualStatus;

    private String startDate;
    private String endDate;
    private String deadline;

    private Integer duration;
    private String linkUrl;

    private String description; // 설명
    private String content;     // 지문 본문 / 상세 내용
    private String textContent; // 기존 구조 유지용(당장은 둬도 됨)
    private String guidePoint;
    
    private String target;
    private int difficultCount;
    private boolean deleted;
    private String deletedAt;

    private int completedCount;
    private int inProgressCount;
    private int notStartedCount;
    private int completionRate;
    private int totalStudentCount;

    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLearnListId() { return learnListId; }
    public void setLearnListId(int learnListId) { this.learnListId = learnListId; }

    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getManualStatus() { return manualStatus; }
    public void setManualStatus(String manualStatus) { this.manualStatus = manualStatus; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTextContent() { return textContent; }
    public void setTextContent(String textContent) { this.textContent = textContent; }

    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }

    public int getDifficultCount() { return difficultCount; }
    public void setDifficultCount(int difficultCount) { this.difficultCount = difficultCount; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public String getDeletedAt() { return deletedAt; }
    public void setDeletedAt(String deletedAt) { this.deletedAt = deletedAt; }

    public int getCompletedCount() { return completedCount; }
    public void setCompletedCount(int completedCount) { this.completedCount = completedCount; }

    public int getInProgressCount() { return inProgressCount; }
    public void setInProgressCount(int inProgressCount) { this.inProgressCount = inProgressCount; }

    public int getNotStartedCount() { return notStartedCount; }
    public void setNotStartedCount(int notStartedCount) { this.notStartedCount = notStartedCount; }

    public int getCompletionRate() { return completionRate; }
    public void setCompletionRate(int completionRate) { this.completionRate = completionRate; }

    public int getTotalStudentCount() { return totalStudentCount; }
    public void setTotalStudentCount(int totalStudentCount) { this.totalStudentCount = totalStudentCount; }
	public String getGuidePoint() {
		return guidePoint;
	}
	public void setGuidePoint(String guidePoint) {
		this.guidePoint = guidePoint;
	}
}