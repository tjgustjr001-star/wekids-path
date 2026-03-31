package com.spring.service;

import java.util.List;

import com.spring.dto.parent.ParentAssignmentChildDTO;
import com.spring.dto.student.StudentAssignmentItemDTO;

public interface ParentAssignmentService {
    List<ParentAssignmentChildDTO> getParentAssignmentChildren(int parentId, int classId) throws Exception;
    List<StudentAssignmentItemDTO> getParentAssignmentList(int parentId, int classId, int studentId) throws Exception;
    StudentAssignmentItemDTO getParentAssignmentDetail(int parentId, int classId, int studentId, int assignmentId) throws Exception;
}
