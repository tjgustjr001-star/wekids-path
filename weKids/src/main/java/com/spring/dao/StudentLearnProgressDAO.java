package com.spring.dao;

import java.sql.SQLException;
import java.util.Map;

public interface StudentLearnProgressDAO {

    Integer selectProgressId(Map<String, Object> paramMap) throws SQLException;

    Integer selectNextProgressId() throws SQLException;

    Integer selectNextFeedbackId() throws SQLException;

    void insertLearnProgress(Map<String, Object> paramMap) throws SQLException;

    void updateLearnProgressToInProgress(Map<String, Object> paramMap) throws SQLException;

    void updateLearnProgressToCompleted(Map<String, Object> paramMap) throws SQLException;

    Integer selectProgressIdByLearnAndStudent(Map<String, Object> paramMap) throws SQLException;

    void insertLearnDifficultyFeedback(Map<String, Object> paramMap) throws SQLException;

    void updateVideoProgress(Map<String, Object> paramMap) throws SQLException;

    void updateTextProgress(Map<String, Object> paramMap) throws SQLException;

    Integer selectDifficultyFeedbackIdByProgressId(Map<String, Object> paramMap) throws SQLException;

    void updateLearnDifficultyFeedback(Map<String, Object> paramMap) throws SQLException;

    String selectStudentLearnOpenStatus(Map<String, Object> paramMap) throws SQLException;
    
    String selectProgressStatus(Map<String, Object> paramMap) throws SQLException;
}