package com.spring.dto.report;

import java.util.ArrayList;
import java.util.List;

public class ReportSnapshotDTO {

    private Summary summary = new Summary();
    private List<MissingAssignment> missingAssignments = new ArrayList<>();
    private List<LearningFeedback> learningFeedbacks = new ArrayList<>();
    private List<AssignmentFeedback> assignmentFeedbacks = new ArrayList<>();

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<MissingAssignment> getMissingAssignments() {
        return missingAssignments;
    }

    public void setMissingAssignments(List<MissingAssignment> missingAssignments) {
        this.missingAssignments = missingAssignments;
    }

    public List<LearningFeedback> getLearningFeedbacks() {
        return learningFeedbacks;
    }

    public void setLearningFeedbacks(List<LearningFeedback> learningFeedbacks) {
        this.learningFeedbacks = learningFeedbacks;
    }

    public List<AssignmentFeedback> getAssignmentFeedbacks() {
        return assignmentFeedbacks;
    }

    public void setAssignmentFeedbacks(List<AssignmentFeedback> assignmentFeedbacks) {
        this.assignmentFeedbacks = assignmentFeedbacks;
    }

    private List<PendingLearning> pendingLearnings;

    public List<PendingLearning> getPendingLearnings() {
        return pendingLearnings;
    }

    public void setPendingLearnings(List<PendingLearning> pendingLearnings) {
        this.pendingLearnings = pendingLearnings;
    }

    public static class PendingLearning {
        private int learnId;
        private String title;
        private String status;
        private String endDate;

        public int getLearnId() { return learnId; }
        public void setLearnId(int learnId) { this.learnId = learnId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
    }
    
    public static class Summary {
        private int completedLearningCount;
        private int totalLearningCount;
        private int learningCompletionRate;

        private int submittedAssignmentCount;
        private int totalAssignmentCount;
        private int assignmentSubmissionRate;

        public int getCompletedLearningCount() {
            return completedLearningCount;
        }

        public void setCompletedLearningCount(int completedLearningCount) {
            this.completedLearningCount = completedLearningCount;
        }

        public int getTotalLearningCount() {
            return totalLearningCount;
        }

        public void setTotalLearningCount(int totalLearningCount) {
            this.totalLearningCount = totalLearningCount;
        }

        public int getLearningCompletionRate() {
            return learningCompletionRate;
        }

        public void setLearningCompletionRate(int learningCompletionRate) {
            this.learningCompletionRate = learningCompletionRate;
        }

        public int getSubmittedAssignmentCount() {
            return submittedAssignmentCount;
        }

        public void setSubmittedAssignmentCount(int submittedAssignmentCount) {
            this.submittedAssignmentCount = submittedAssignmentCount;
        }

        public int getTotalAssignmentCount() {
            return totalAssignmentCount;
        }

        public void setTotalAssignmentCount(int totalAssignmentCount) {
            this.totalAssignmentCount = totalAssignmentCount;
        }

        public int getAssignmentSubmissionRate() {
            return assignmentSubmissionRate;
        }

        public void setAssignmentSubmissionRate(int assignmentSubmissionRate) {
            this.assignmentSubmissionRate = assignmentSubmissionRate;
        }
    }

    public static class MissingAssignment {
        private int assignmentId;
        private String title;
        private String dueDate;

        public int getAssignmentId() {
            return assignmentId;
        }

        public void setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }
    }

    public static class LearningFeedback {
        private int learnId;
        private String title;
        private String feedback;

        public int getLearnId() {
            return learnId;
        }

        public void setLearnId(int learnId) {
            this.learnId = learnId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }
    }

    public static class AssignmentFeedback {
        private int assignmentId;
        private String title;
        private String feedback;

        public int getAssignmentId() {
            return assignmentId;
        }

        public void setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }
    }
}