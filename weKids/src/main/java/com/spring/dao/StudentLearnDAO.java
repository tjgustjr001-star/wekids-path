package com.spring.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.spring.dto.student.StudentLearnItemDTO;

public interface StudentLearnDAO {

    List<StudentLearnItemDTO> selectStudentLearnList(Map<String, Object> paramMap) throws SQLException;

    String selectStudentLearnOpenStatus(Map<String, Object> paramMap) throws SQLException;
}