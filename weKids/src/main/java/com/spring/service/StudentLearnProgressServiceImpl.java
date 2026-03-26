package com.spring.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.StudentLearnProgressDAO;

@Service
public class StudentLearnProgressServiceImpl implements StudentLearnProgressService {

    @Autowired
    private StudentLearnProgressDAO studentLearnProgressDAO;

    @Override
    public void startLearning(int studentId, int classId, int learnId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("studentId", studentId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);

        Integer progressId = studentLearnProgressDAO.selectProgressId(paramMap);

        if (progressId == null) {
            int nextProgressId = studentLearnProgressDAO.selectNextProgressId();
            paramMap.put("progressId", nextProgressId);
            studentLearnProgressDAO.insertLearnProgress(paramMap);
        } else {
            paramMap.put("progressId", progressId);
            studentLearnProgressDAO.updateLearnProgressToInProgress(paramMap);
        }
    }

    @Override
    public void completeLearning(int studentId, int classId, int learnId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("studentId", studentId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);

        Integer progressId = studentLearnProgressDAO.selectProgressId(paramMap);

        if (progressId == null) {
            progressId = studentLearnProgressDAO.selectNextProgressId();
            paramMap.put("progressId", progressId);
            studentLearnProgressDAO.insertLearnProgress(paramMap);
        } else {
            paramMap.put("progressId", progressId);
        }

        studentLearnProgressDAO.updateLearnProgressToCompleted(paramMap);
    }

    @Override
    public void saveDifficultyFeedback(int studentId, int classId, int learnId, String feedbackContent) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("studentId", studentId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);

        Integer progressId = studentLearnProgressDAO.selectProgressIdByLearnAndStudent(paramMap);

        if (progressId == null) {
            int nextProgressId = studentLearnProgressDAO.selectNextProgressId();
            paramMap.put("progressId", nextProgressId);
            studentLearnProgressDAO.insertLearnProgress(paramMap);
            progressId = nextProgressId;
        }

        paramMap.put("progressId", progressId);
        paramMap.put("feedbackContent", feedbackContent);

        Integer feedbackId = studentLearnProgressDAO.selectDifficultyFeedbackIdByProgressId(paramMap);

        if (feedbackId == null) {
            feedbackId = studentLearnProgressDAO.selectNextFeedbackId();
            paramMap.put("feedbackId", feedbackId);
            studentLearnProgressDAO.insertLearnDifficultyFeedback(paramMap);
        } else {
            paramMap.put("feedbackId", feedbackId);
            studentLearnProgressDAO.updateLearnDifficultyFeedback(paramMap);
        }
    }

    @Override
    public void saveVideoProgress(int studentId, int classId, int learnId, int currentSecond, int durationSecond, int progressRate) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("studentId", studentId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);
        paramMap.put("currentSecond", currentSecond);
        paramMap.put("durationSecond", durationSecond);
        paramMap.put("progressRate", progressRate);

        Integer progressId = studentLearnProgressDAO.selectProgressId(paramMap);

        if (progressId == null) {
            progressId = studentLearnProgressDAO.selectNextProgressId();
            paramMap.put("progressId", progressId);
            studentLearnProgressDAO.insertLearnProgress(paramMap);
        } else {
            paramMap.put("progressId", progressId);
        }

        studentLearnProgressDAO.updateVideoProgress(paramMap);

        if (progressRate >= 90) {
            studentLearnProgressDAO.updateLearnProgressToCompleted(paramMap);
        } else {
            studentLearnProgressDAO.updateLearnProgressToInProgress(paramMap);
        }
    }
}