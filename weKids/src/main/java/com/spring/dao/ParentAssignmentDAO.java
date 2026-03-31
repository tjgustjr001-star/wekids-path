package com.spring.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.spring.dto.parent.ParentAssignmentChildDTO;
import com.spring.dto.student.StudentAssignmentItemDTO;

public interface ParentAssignmentDAO {
    List<ParentAssignmentChildDTO> selectParentAssignmentChildren(Map<String, Object> paramMap) throws SQLException;
    List<StudentAssignmentItemDTO> selectParentAssignmentList(Map<String, Object> paramMap) throws SQLException;
    StudentAssignmentItemDTO selectParentAssignmentDetail(Map<String, Object> paramMap) throws SQLException;
}
