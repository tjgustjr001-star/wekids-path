package com.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.dao.ReportAggregateDAO;
import com.spring.dto.report.ReportSnapshotDTO;

@Service
public class ReportAggregateServiceImpl implements ReportAggregateService {

    @Autowired
    private ReportAggregateDAO reportAggregateDAO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String buildPersonalSnapshotJson(int classId,
                                            int studentId,
                                            String startDate,
                                            String endDate) throws Exception {

        int totalLearningCount = reportAggregateDAO.countTotalLearning(classId, studentId, startDate, endDate);
        int completedLearningCount = reportAggregateDAO.countCompletedLearning(classId, studentId, startDate, endDate);

        int totalAssignmentCount = reportAggregateDAO.countTotalAssignment(classId, studentId, startDate, endDate);
        int submittedAssignmentCount = reportAggregateDAO.countSubmittedAssignment(classId, studentId, startDate, endDate);

        List<Map<String, Object>> pendingLearningRows =
                reportAggregateDAO.selectLearningFeedbacks(classId, studentId, startDate, endDate);

        List<Map<String, Object>> missingAssignmentRows =
                reportAggregateDAO.selectMissingAssignments(classId, studentId, startDate, endDate);

        List<Map<String, Object>> assignmentFeedbackRows =
                reportAggregateDAO.selectAssignmentFeedbacks(classId, studentId, startDate, endDate);

        ReportSnapshotDTO snapshot = new ReportSnapshotDTO();

        ReportSnapshotDTO.Summary summary = new ReportSnapshotDTO.Summary();
        summary.setTotalLearningCount(totalLearningCount);
        summary.setCompletedLearningCount(completedLearningCount);
        summary.setLearningCompletionRate(calculateRate(completedLearningCount, totalLearningCount));

        summary.setTotalAssignmentCount(totalAssignmentCount);
        summary.setSubmittedAssignmentCount(submittedAssignmentCount);
        summary.setAssignmentSubmissionRate(calculateRate(submittedAssignmentCount, totalAssignmentCount));

        snapshot.setSummary(summary);
        snapshot.setPendingLearnings(toPendingLearningList(pendingLearningRows));
        snapshot.setMissingAssignments(toMissingAssignmentList(missingAssignmentRows));
        snapshot.setAssignmentFeedbacks(toAssignmentFeedbackList(assignmentFeedbackRows));

        return objectMapper.writeValueAsString(snapshot);
    }

    @Override
    public String buildClassSnapshotJson(int classId,
                                         String startDate,
                                         String endDate) throws Exception {

        List<Integer> studentIds = reportAggregateDAO.selectClassStudentIds(classId);

        int studentCount = studentIds == null ? 0 : studentIds.size();

        int totalLearningCountSum = 0;
        int completedLearningCountSum = 0;
        int totalAssignmentCountSum = 0;
        int submittedAssignmentCountSum = 0;

        List<ReportSnapshotDTO.MissingAssignment> mergedMissingAssignments = new ArrayList<>();
        List<ReportSnapshotDTO.PendingLearning> mergedPendingLearnings = new ArrayList<>();
        List<ReportSnapshotDTO.AssignmentFeedback> mergedAssignmentFeedbacks = new ArrayList<>();

        if (studentIds != null) {
            for (Integer studentId : studentIds) {
                if (studentId == null) {
                    continue;
                }

                int totalLearningCount = reportAggregateDAO.countTotalLearning(classId, studentId, startDate, endDate);
                int completedLearningCount = reportAggregateDAO.countCompletedLearning(classId, studentId, startDate, endDate);

                int totalAssignmentCount = reportAggregateDAO.countTotalAssignment(classId, studentId, startDate, endDate);
                int submittedAssignmentCount = reportAggregateDAO.countSubmittedAssignment(classId, studentId, startDate, endDate);

                totalLearningCountSum += totalLearningCount;
                completedLearningCountSum += completedLearningCount;
                totalAssignmentCountSum += totalAssignmentCount;
                submittedAssignmentCountSum += submittedAssignmentCount;

                mergedPendingLearnings.addAll(
                        toPendingLearningList(
                                reportAggregateDAO.selectLearningFeedbacks(classId, studentId, startDate, endDate)
                        )
                );

                mergedMissingAssignments.addAll(
                        toMissingAssignmentList(
                                reportAggregateDAO.selectMissingAssignments(classId, studentId, startDate, endDate)
                        )
                );

                mergedAssignmentFeedbacks.addAll(
                        toAssignmentFeedbackList(
                                reportAggregateDAO.selectAssignmentFeedbacks(classId, studentId, startDate, endDate)
                        )
                );
            }
        }

        ReportSnapshotDTO snapshot = new ReportSnapshotDTO();

        ReportSnapshotDTO.Summary summary = new ReportSnapshotDTO.Summary();
        summary.setTotalLearningCount(totalLearningCountSum);
        summary.setCompletedLearningCount(completedLearningCountSum);
        summary.setLearningCompletionRate(calculateRate(completedLearningCountSum, totalLearningCountSum));

        summary.setTotalAssignmentCount(totalAssignmentCountSum);
        summary.setSubmittedAssignmentCount(submittedAssignmentCountSum);
        summary.setAssignmentSubmissionRate(calculateRate(submittedAssignmentCountSum, totalAssignmentCountSum));

        snapshot.setSummary(summary);
        snapshot.setMissingAssignments(limitMissingAssignments(mergedMissingAssignments, 20));
        snapshot.setPendingLearnings(limitPendingLearnings(mergedPendingLearnings, 20));
        snapshot.setAssignmentFeedbacks(limitAssignmentFeedbacks(mergedAssignmentFeedbacks, 20));

        return objectMapper.writeValueAsString(snapshot);
    }

    private int calculateRate(int done, int total) {
        if (total <= 0) {
            return 0;
        }
        return (int) Math.round((done * 100.0) / total);
    }

    private List<ReportSnapshotDTO.MissingAssignment> toMissingAssignmentList(List<Map<String, Object>> rows) {
        List<ReportSnapshotDTO.MissingAssignment> result = new ArrayList<>();

        if (rows == null) {
            return result;
        }

        for (Map<String, Object> row : rows) {
            if (row == null) {
                continue;
            }

            ReportSnapshotDTO.MissingAssignment item = new ReportSnapshotDTO.MissingAssignment();
            item.setAssignmentId(toInt(row.get("assignmentId")));
            item.setTitle(toStr(row.get("title")));
            item.setDueDate(toStr(row.get("dueDate")));
            result.add(item);
        }

        return result;
    }

    private List<ReportSnapshotDTO.PendingLearning> toPendingLearningList(List<Map<String, Object>> rows) {
        List<ReportSnapshotDTO.PendingLearning> result = new ArrayList<>();

        if (rows == null) {
            return result;
        }

        for (Map<String, Object> row : rows) {
            if (row == null) {
                continue;
            }

            ReportSnapshotDTO.PendingLearning item = new ReportSnapshotDTO.PendingLearning();
            item.setLearnId(toInt(row.get("learnId")));
            item.setTitle(toStr(row.get("title")));
            item.setStatus("미완료");

            String endDate = toStr(row.get("endDate"));
            if (endDate.isEmpty()) {
                endDate = toStr(row.get("deadline"));
            }
            item.setEndDate(endDate);

            result.add(item);
        }

        return result;
    }

    private List<ReportSnapshotDTO.AssignmentFeedback> toAssignmentFeedbackList(List<Map<String, Object>> rows) {
        List<ReportSnapshotDTO.AssignmentFeedback> result = new ArrayList<>();

        if (rows == null) {
            return result;
        }

        for (Map<String, Object> row : rows) {
            if (row == null) {
                continue;
            }

            ReportSnapshotDTO.AssignmentFeedback item = new ReportSnapshotDTO.AssignmentFeedback();
            item.setAssignmentId(toInt(row.get("assignmentId")));
            item.setTitle(toStr(row.get("title")));
            item.setFeedback(toStr(row.get("feedback")));
            result.add(item);
        }

        return result;
    }

    private List<ReportSnapshotDTO.MissingAssignment> limitMissingAssignments(
            List<ReportSnapshotDTO.MissingAssignment> items, int limit) {

        if (items == null) {
            return new ArrayList<>();
        }
        if (items.size() <= limit) {
            return items;
        }
        return new ArrayList<>(items.subList(0, limit));
    }

    private List<ReportSnapshotDTO.PendingLearning> limitPendingLearnings(
            List<ReportSnapshotDTO.PendingLearning> items, int limit) {

        if (items == null) {
            return new ArrayList<>();
        }
        if (items.size() <= limit) {
            return items;
        }
        return new ArrayList<>(items.subList(0, limit));
    }

    private List<ReportSnapshotDTO.AssignmentFeedback> limitAssignmentFeedbacks(
            List<ReportSnapshotDTO.AssignmentFeedback> items, int limit) {

        if (items == null) {
            return new ArrayList<>();
        }
        if (items.size() <= limit) {
            return items;
        }
        return new ArrayList<>(items.subList(0, limit));
    }

    private int toInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            return 0;
        }
    }

    private String toStr(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}