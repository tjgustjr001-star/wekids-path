package com.spring.service;

public interface StudentLearnProgressService {

    void startLearning(int studentId, int classId, int learnId) throws Exception;

    void completeLearning(int studentId, int classId, int learnId) throws Exception;

    void saveDifficultyFeedback(int studentId, int classId, int learnId, String feedbackContent) throws Exception;

    void saveVideoProgress(int studentId, int classId, int learnId, int currentSecond, int durationSecond, int progressRate) throws Exception;
}