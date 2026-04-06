package com.spring.dto.admin;

public class UserRoleLoginTrendDTO {
	  private String dayLabel;
	    private int studentCount;
	    private int parentCount;
	    private int teacherCount;

	    public String getDayLabel() { return dayLabel; }
	    public void setDayLabel(String dayLabel) { this.dayLabel = dayLabel; }

	    public int getStudentCount() { return studentCount; }
	    public void setStudentCount(int studentCount) { this.studentCount = studentCount; }

	    public int getParentCount() { return parentCount; }
	    public void setParentCount(int parentCount) { this.parentCount = parentCount; }

	    public int getTeacherCount() { return teacherCount; }
	    public void setTeacherCount(int teacherCount) { this.teacherCount = teacherCount; }
}
