package com.spring.dto.parent;

public class ParentChildClassOptionDTO {

    private int studentId;
    private String studentName;
    private int classId;
    private String className;
    private int grade;
    private int classNo;

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

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getClassNo() {
        return classNo;
    }

    public void setClassNo(int classNo) {
        this.classNo = classNo;
    }

    public String getLabel() {
        if (className != null && !className.isBlank()) {
            return studentName + " - " + className;
        }
        if (grade > 0 && classNo > 0) {
            return studentName + " - " + grade + "학년 " + classNo + "반";
        }
        return studentName;
    }

    public String getValue() {
        return studentId + ":" + classId;
    }
}