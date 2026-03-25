package com.spring.service;

import java.sql.SQLException;
import java.util.List;

import com.spring.dto.student.StudentLearnItemDTO;

public interface StudentLearnService {

    List<StudentLearnItemDTO> getStudentLearnList(int studentId, int classId) throws SQLException;
}