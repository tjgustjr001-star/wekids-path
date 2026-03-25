package com.spring.service;

import java.sql.SQLException;
import java.util.List;

import com.spring.dto.teacher.TeacherLearnManageDTO;
import com.spring.dto.teacher.TeacherLearnSaveDTO;

public interface TeacherLearnService {

    List<TeacherLearnManageDTO> getTeacherLearnList(int teacherId, int classId, int trashYn) throws SQLException;

    TeacherLearnManageDTO getTeacherLearnDetail(int teacherId, int classId, int learnId) throws SQLException;

    void registTeacherLearn(int teacherId, int classId, TeacherLearnSaveDTO dto) throws Exception;

    void modifyTeacherLearn(int teacherId, int classId, int learnId, TeacherLearnSaveDTO dto) throws Exception;

    void deleteTeacherLearn(int teacherId, int classId, int learnId) throws Exception;

    void restoreTeacherLearn(int teacherId, int classId, int learnId) throws Exception;

    void removeTeacherLearn(int teacherId, int classId, int learnId) throws Exception;
}