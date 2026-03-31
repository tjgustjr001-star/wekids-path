package com.spring.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.spring.dto.teacher.TeacherAssignmentDetailDTO;
import com.spring.dto.teacher.TeacherAssignmentManageDTO;
import com.spring.dto.teacher.TeacherAssignmentSubmissionDTO;
import com.spring.dto.teacher.TeacherAssignmentStudentWorkDTO;

public interface TeacherAssignmentDAO {
    List<TeacherAssignmentManageDTO> selectTeacherAssignmentList(Map<String, Object> paramMap) throws SQLException;
    TeacherAssignmentDetailDTO selectTeacherAssignmentDetail(Map<String, Object> paramMap) throws SQLException;
    List<TeacherAssignmentSubmissionDTO> selectTeacherAssignmentSubmissionList(Map<String, Object> paramMap) throws SQLException;
    Integer selectNextAssignmentId() throws SQLException;
    void insertTeacherAssignment(Map<String, Object> paramMap) throws SQLException;
    void updateTeacherAssignment(Map<String, Object> paramMap) throws SQLException;
    void softDeleteTeacherAssignment(Map<String, Object> paramMap) throws SQLException;
    void restoreTeacherAssignment(Map<String, Object> paramMap) throws SQLException;
    java.util.List<String> selectAssignmentAttachPaths(Map<String, Object> paramMap) throws SQLException;
    void deleteAssignmentAttachByAssignment(Map<String, Object> paramMap) throws SQLException;
    void deleteAssignmentFeedbackByAssignment(Map<String, Object> paramMap) throws SQLException;
    void deleteAssignmentSubmissions(Map<String, Object> paramMap) throws SQLException;
    void deleteTeacherAssignmentPermanent(Map<String, Object> paramMap) throws SQLException;
    void closeTeacherAssignment(Map<String, Object> paramMap) throws SQLException;
    void reopenTeacherAssignment(Map<String, Object> paramMap) throws SQLException;
    TeacherAssignmentStudentWorkDTO selectTeacherStudentSubmissionDetail(Map<String, Object> paramMap) throws SQLException;
    void updateTeacherStudentSubmissionReject(Map<String, Object> paramMap) throws SQLException;
    void updateTeacherStudentSubmissionComplete(Map<String, Object> paramMap) throws SQLException;
    Integer selectNextAssignmentFeedbackId() throws SQLException;
    Integer selectAssignmentFeedbackIdBySubId(int subId) throws SQLException;
    void insertTeacherStudentSubmissionFeedback(Map<String, Object> paramMap) throws SQLException;
    void updateTeacherStudentSubmissionFeedback(Map<String, Object> paramMap) throws SQLException;
}
