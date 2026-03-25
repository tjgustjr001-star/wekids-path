package com.spring.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.TeacherLearnDAO;
import com.spring.dto.teacher.TeacherLearnManageDTO;
import com.spring.dto.teacher.TeacherLearnSaveDTO;

@Service
public class TeacherLearnServiceImpl implements TeacherLearnService {

	@Autowired
    private TeacherLearnDAO teacherLearnDAO;

    @Override
    public List<TeacherLearnManageDTO> getTeacherLearnList(int teacherId, int classId, int trashYn) throws SQLException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("trashYn", trashYn);
        return teacherLearnDAO.selectTeacherLearnList(paramMap);
    }

    @Override
    public TeacherLearnManageDTO getTeacherLearnDetail(int teacherId, int classId, int learnId) throws SQLException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);
        return teacherLearnDAO.selectTeacherLearnDetail(paramMap);
    }

    @Override
    public void registTeacherLearn(int teacherId, int classId, TeacherLearnSaveDTO dto) throws Exception {
        int learnListId = teacherLearnDAO.selectNextLearnListId();
        int learnId = teacherLearnDAO.selectNextLearnId();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("learnListId", learnListId);
        paramMap.put("learnId", learnId);
        paramMap.put("dto", dto);

        teacherLearnDAO.insertLearnList(paramMap);
        teacherLearnDAO.insertLearnCont(paramMap);
    }

    @Override
    public void modifyTeacherLearn(int teacherId, int classId, int learnId, TeacherLearnSaveDTO dto) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);
        paramMap.put("dto", dto);

        teacherLearnDAO.updateLearnList(paramMap);
        teacherLearnDAO.updateLearnCont(paramMap);
    }

    @Override
    public void deleteTeacherLearn(int teacherId, int classId, int learnId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);

        teacherLearnDAO.softDeleteLearn(paramMap);
    }

    @Override
    public void restoreTeacherLearn(int teacherId, int classId, int learnId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);

        teacherLearnDAO.restoreLearn(paramMap);
    }

    @Override
    public void removeTeacherLearn(int teacherId, int classId, int learnId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);

        teacherLearnDAO.deleteLearnFeedbackByLearnId(paramMap);
        teacherLearnDAO.deleteLearnProgressByLearnId(paramMap);
        teacherLearnDAO.deleteLearnCont(paramMap);
        teacherLearnDAO.deleteLearnListIfNoChildren(paramMap);
    }
}