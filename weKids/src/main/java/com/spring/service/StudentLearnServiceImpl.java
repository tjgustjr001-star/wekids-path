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

    @Override
    public void validateLearnOpenForStudent(int studentId, int classId, int learnId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("studentId", studentId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);

        String openStatus = studentLearnDAO.selectStudentLearnOpenStatus(paramMap);

        if (openStatus == null) {
            throw new IllegalArgumentException("열람 가능한 학습이 아니거나 클래스 소속이 아닙니다.");
        }

        if (!"OPEN".equals(openStatus)) {
            if ("WAITING".equals(openStatus)) {
                throw new IllegalStateException("아직 시작되지 않은 학습입니다.");
            }
            throw new IllegalStateException("마감된 학습입니다.");
        }
    }
}