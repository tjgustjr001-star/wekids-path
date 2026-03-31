package com.spring.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.student.StudentAssignmentAttachDTO;
import com.spring.dto.student.StudentAssignmentItemDTO;
import com.spring.dto.student.StudentAssignmentSubmitDTO;

public interface StudentAssignmentDAO {
    List<StudentAssignmentItemDTO> selectStudentAssignmentList(Map<String, Object> paramMap) throws SQLException;
    StudentAssignmentItemDTO selectStudentAssignmentDetail(Map<String, Object> paramMap) throws SQLException;
    StudentAssignmentSubmitDTO selectLatestStudentAssignmentSubmit(Map<String, Object> paramMap) throws SQLException;
    StudentAssignmentAttachDTO selectLatestStudentAssignmentAttach(Map<String, Object> paramMap) throws SQLException;
    Integer selectNextAssignmentSubId() throws SQLException;
    Integer selectNextAttachAno() throws SQLException;
    void insertStudentAssignmentSubmit(Map<String, Object> paramMap) throws SQLException;
    void updateStudentAssignmentSubmit(Map<String, Object> paramMap) throws SQLException;
    void insertStudentAssignmentAttach(Map<String, Object> paramMap) throws SQLException;
    void deleteStudentAssignmentAttachByPno(@Param("pno") int pno) throws SQLException;
    void deleteStudentAssignmentFeedbackBySubId(@Param("subId") int subId) throws SQLException;
}
