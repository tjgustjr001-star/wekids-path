package com.spring.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.dto.student.StudentAssignmentItemDTO;

public interface StudentAssignmentService {
    List<StudentAssignmentItemDTO> getStudentAssignmentList(int studentId, int classId) throws Exception;
    StudentAssignmentItemDTO getStudentAssignmentDetail(int studentId, int classId, int assignmentId) throws Exception;
    StudentAssignmentItemDTO submitStudentAssignment(int studentId, int classId, int assignmentId,
                                                     String content,
                                                     MultipartFile uploadFile,
                                                     String saveDir) throws Exception;
}
