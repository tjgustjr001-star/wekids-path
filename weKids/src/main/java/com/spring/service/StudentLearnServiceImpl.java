package com.spring.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.StudentLearnDAO;
import com.spring.dto.student.StudentLearnItemDTO;

@Service
public class StudentLearnServiceImpl implements StudentLearnService {

    @Autowired
    private StudentLearnDAO studentLearnDAO;

    @Override
    public List<StudentLearnItemDTO> getStudentLearnList(int studentId, int classId) throws SQLException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("studentId", studentId);
        paramMap.put("classId", classId);
        return studentLearnDAO.selectStudentLearnList(paramMap);
    }
}