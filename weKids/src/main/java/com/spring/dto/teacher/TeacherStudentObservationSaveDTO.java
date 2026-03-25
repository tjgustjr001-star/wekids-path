package com.spring.dto.teacher;

import java.util.List;

public class TeacherStudentObservationSaveDTO {

    private int studentClassId;
    private String observationMemo;
    private List<String> tagNameList;

    public int getStudentClassId() {
        return studentClassId;
    }

    public void setStudentClassId(int studentClassId) {
        this.studentClassId = studentClassId;
    }

    public String getObservationMemo() {
        return observationMemo;
    }

    public void setObservationMemo(String observationMemo) {
        this.observationMemo = observationMemo;
    }

    public List<String> getTagNameList() {
        return tagNameList;
    }

    public void setTagNameList(List<String> tagNameList) {
        this.tagNameList = tagNameList;
    }
}