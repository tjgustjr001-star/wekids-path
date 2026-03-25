package com.spring.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.spring.dto.teacher.TeacherLearnManageDTO;
import com.spring.dto.teacher.TeacherLearnSaveDTO;

public interface TeacherLearnDAO {

    List<TeacherLearnManageDTO> selectTeacherLearnList(Map<String, Object> paramMap) throws SQLException;

    TeacherLearnManageDTO selectTeacherLearnDetail(Map<String, Object> paramMap) throws SQLException;

    int selectNextLearnListId() throws SQLException;

    int selectNextLearnId() throws SQLException;

    void insertLearnList(Map<String, Object> paramMap) throws SQLException;

    void insertLearnCont(Map<String, Object> paramMap) throws SQLException;

    void updateLearnList(Map<String, Object> paramMap) throws SQLException;

    void updateLearnCont(Map<String, Object> paramMap) throws SQLException;

    void softDeleteLearn(Map<String, Object> paramMap) throws SQLException;

    void restoreLearn(Map<String, Object> paramMap) throws SQLException;

    void deleteLearnFeedbackByLearnId(Map<String, Object> paramMap) throws SQLException;

    void deleteLearnProgressByLearnId(Map<String, Object> paramMap) throws SQLException;

    void deleteLearnCont(Map<String, Object> paramMap) throws SQLException;

    void deleteLearnListIfNoChildren(Map<String, Object> paramMap) throws SQLException;
}