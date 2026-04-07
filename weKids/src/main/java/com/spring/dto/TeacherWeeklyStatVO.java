package com.spring.dto;

public class TeacherWeeklyStatVO {
	  private String weekLabel;
	    private int assignmentCount;
	    private int feedbackCount;

	    public String getWeekLabel() {
	        return weekLabel;
	    }

	    public void setWeekLabel(String weekLabel) {
	        this.weekLabel = weekLabel;
	    }

	    public int getAssignmentCount() {
	        return assignmentCount;
	    }

	    public void setAssignmentCount(int assignmentCount) {
	        this.assignmentCount = assignmentCount;
	    }

	    public int getFeedbackCount() {
	        return feedbackCount;
	    }

	    public void setFeedbackCount(int feedbackCount) {
	        this.feedbackCount = feedbackCount;
	    }
}
