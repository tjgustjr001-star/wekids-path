package com.spring.service;

import java.sql.SQLException;
import java.util.List;

import com.spring.dto.teacher.TeacherAssignmentDetailDTO;
import com.spring.dto.teacher.TeacherAssignmentManageDTO;
import com.spring.dto.teacher.TeacherAssignmentSaveDTO;
import com.spring.dto.teacher.TeacherAssignmentStudentWorkDTO;

public interface TeacherAssignmentService {
    List<TeacherAssignmentManageDTO> getTeacherAssignmentList(int teacherId, int classId, int trashYn) throws SQLException;
    TeacherAssignmentDetailDTO getTeacherAssignmentDetail(int teacherId, int classId, int assignmentId) throws SQLException;
    void registTeacherAssignment(int teacherId, int classId, TeacherAssignmentSaveDTO dto) throws Exception;
    void modifyTeacherAssignment(int teacherId, int classId, int assignmentId, TeacherAssignmentSaveDTO dto) throws Exception;
    void deleteTeacherAssignment(int teacherId, int classId, int assignmentId) throws Exception;
    void restoreTeacherAssignment(int teacherId, int classId, int assignmentId) throws Exception;
    void removeTeacherAssignment(int teacherId, int classId, int assignmentId) throws Exception;
    void toggleTeacherAssignmentStatus(int teacherId, int classId, int assignmentId) throws Exception;
    TeacherAssignmentStudentWorkDTO getTeacherStudentSubmissionDetail(int teacherId, int classId, int assignmentId, int studentId) throws Exception;
    void rejectTeacherStudentSubmission(int teacherId, int classId, int assignmentId, int studentId, String returnReason) throws Exception;
    void completeTeacherStudentSubmission(int teacherId, int classId, int assignmentId, int studentId) throws Exception;
    void completeTeacherStudentSubmissionWithFeedback(int teacherId, int classId, int assignmentId, int studentId, String feedbackContent) throws Exception;
}
