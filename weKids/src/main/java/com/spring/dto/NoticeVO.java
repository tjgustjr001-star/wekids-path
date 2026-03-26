package com.spring.dto;

import java.util.Date;
import java.util.List;

public class NoticeVO {
    private int noticeId;
    private int classId;
    private int memberId;
    private String title;
    private String content;
    private int confirmYn;
    private String target;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    private String writerName;
    private String className;
    private Integer confirmedYn;
    private List<AttachVO> attachList;

    public int getNoticeId() { return noticeId; }
    public void setNoticeId(int noticeId) { this.noticeId = noticeId; }
    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getConfirmYn() { return confirmYn; }
    public void setConfirmYn(int confirmYn) { this.confirmYn = confirmYn; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public String getWriterName() { return writerName; }
    public void setWriterName(String writerName) { this.writerName = writerName; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public Integer getConfirmedYn() { return confirmedYn; }
    public void setConfirmedYn(Integer confirmedYn) { this.confirmedYn = confirmedYn; }
    public List<AttachVO> getAttachList() { return attachList; }
    public void setAttachList(List<AttachVO> attachList) { this.attachList = attachList; }

    public boolean isRequiredUnread() {
        return confirmYn == 1 && (confirmedYn == null || confirmedYn == 0);
    }
    
    public boolean isReadConfirmed() {
        return confirmedYn != null && confirmedYn == 1;
    }
}
