package com.spring.dto.teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherAssignmentDetailDTO extends TeacherAssignmentManageDTO {
    private int submittedCount;
    private int resubmittedCount;
    private int lateCount;
    private int notSubmittedCount;
    private List<TeacherAssignmentSubmissionDTO> submissionList = new ArrayList<>();
    public int getSubmittedCount() { return submittedCount; }
    public void setSubmittedCount(int submittedCount) { this.submittedCount = submittedCount; }
    public int getResubmittedCount() { return resubmittedCount; }
    public void setResubmittedCount(int resubmittedCount) { this.resubmittedCount = resubmittedCount; }
    public int getLateCount() { return lateCount; }
    public void setLateCount(int lateCount) { this.lateCount = lateCount; }
    public int getNotSubmittedCount() { return notSubmittedCount; }
    public void setNotSubmittedCount(int notSubmittedCount) { this.notSubmittedCount = notSubmittedCount; }
    public List<TeacherAssignmentSubmissionDTO> getSubmissionList() { return submissionList; }
    public void setSubmissionList(List<TeacherAssignmentSubmissionDTO> submissionList) { this.submissionList = submissionList; }
}
