package com.spring.dto;

import java.util.Date;

public class ChildLinkVO {

    private int studentId;
    private String studentName;
    private String parentLinkCode;
    private Date updatedAt;

    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getParentLinkCode() {
        return parentLinkCode;
    }
    public void setParentLinkCode(String parentLinkCode) {
        this.parentLinkCode = parentLinkCode;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}