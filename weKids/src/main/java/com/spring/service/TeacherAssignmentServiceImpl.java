package com.spring.service;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.TeacherAssignmentDAO;
import com.spring.dto.teacher.TeacherAssignmentDetailDTO;
import com.spring.dto.teacher.TeacherAssignmentManageDTO;
import com.spring.dto.teacher.TeacherAssignmentSaveDTO;
import com.spring.dto.teacher.TeacherAssignmentStudentWorkDTO;

@Service
public class TeacherAssignmentServiceImpl implements TeacherAssignmentService {

    @Autowired
    private TeacherAssignmentDAO teacherAssignmentDAO;

    @Override
    public List<TeacherAssignmentManageDTO> getTeacherAssignmentList(int teacherId, int classId, int trashYn) throws SQLException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("trashYn", trashYn);
        return teacherAssignmentDAO.selectTeacherAssignmentList(paramMap);
    }

    @Override
    public TeacherAssignmentDetailDTO getTeacherAssignmentDetail(int teacherId, int classId, int assignmentId) throws SQLException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("assignmentId", assignmentId);
        TeacherAssignmentDetailDTO detail = teacherAssignmentDAO.selectTeacherAssignmentDetail(paramMap);
        if (detail != null) {
            detail.setSubmissionList(teacherAssignmentDAO.selectTeacherAssignmentSubmissionList(paramMap));
        }
        return detail;
    }

    @Override
    public void registTeacherAssignment(int teacherId, int classId, TeacherAssignmentSaveDTO dto) throws Exception {
        normalize(dto);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("assignmentId", teacherAssignmentDAO.selectNextAssignmentId());
        paramMap.put("dto", dto);
        teacherAssignmentDAO.insertTeacherAssignment(paramMap);
    }

    @Override
    public void modifyTeacherAssignment(int teacherId, int classId, int assignmentId, TeacherAssignmentSaveDTO dto) throws Exception {
        normalize(dto);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("assignmentId", assignmentId);
        paramMap.put("dto", dto);
        teacherAssignmentDAO.updateTeacherAssignment(paramMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTeacherAssignment(int teacherId, int classId, int assignmentId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("assignmentId", assignmentId);
        teacherAssignmentDAO.softDeleteTeacherAssignment(paramMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreTeacherAssignment(int teacherId, int classId, int assignmentId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("assignmentId", assignmentId);
        teacherAssignmentDAO.restoreTeacherAssignment(paramMap);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTeacherAssignment(int teacherId, int classId, int assignmentId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("assignmentId", assignmentId);

        TeacherAssignmentDetailDTO detail = teacherAssignmentDAO.selectTeacherAssignmentDetail(paramMap);
        if (detail == null) {
            throw new IllegalArgumentException("존재하지 않는 과제입니다.");
        }

        if (!detail.isDeleted()) {
            throw new IllegalStateException("휴지통에 있는 과제만 영구삭제할 수 있습니다.");
        }

        List<String> attachPaths = teacherAssignmentDAO.selectAssignmentAttachPaths(paramMap);

        teacherAssignmentDAO.deleteAssignmentFeedbackByAssignment(paramMap);
        teacherAssignmentDAO.deleteAssignmentAttachByAssignment(paramMap);
        teacherAssignmentDAO.deleteAssignmentSubmissions(paramMap);
        teacherAssignmentDAO.deleteTeacherAssignmentPermanent(paramMap);

        if (attachPaths != null) {
            for (String path : attachPaths) {
                if (path == null || path.isBlank()) continue;
                try {
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception ignore) {
                }
            }
        }
    }

    @Override
    public void toggleTeacherAssignmentStatus(int teacherId, int classId, int assignmentId) throws Exception {
        TeacherAssignmentDetailDTO detail = getTeacherAssignmentDetail(teacherId, classId, assignmentId);
        if (detail == null) return;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("assignmentId", assignmentId);
        if ("마감".equals(detail.getStatus())) {
            teacherAssignmentDAO.reopenTeacherAssignment(paramMap);
        } else {
            teacherAssignmentDAO.closeTeacherAssignment(paramMap);
        }
    }


    @Override
    public TeacherAssignmentStudentWorkDTO getTeacherStudentSubmissionDetail(int teacherId, int classId, int assignmentId, int studentId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("assignmentId", assignmentId);
        paramMap.put("studentId", studentId);
        TeacherAssignmentStudentWorkDTO detail = teacherAssignmentDAO.selectTeacherStudentSubmissionDetail(paramMap);
        if (detail != null && detail.getUploadPath() != null && !detail.getUploadPath().isBlank()) {
            File file = new File(detail.getUploadPath());
            if (file.exists()) {
                detail.setFileSize(file.length());
                detail.setCanDownload(true);
            }
        }
        return detail;
    }

    @Override
    public void rejectTeacherStudentSubmission(int teacherId, int classId, int assignmentId, int studentId, String returnReason) throws Exception {
        String normalizedReason = returnReason == null ? "" : returnReason.trim();
        if (normalizedReason.isBlank()) {
            throw new IllegalArgumentException("반려 사유를 입력해주세요.");
        }

        TeacherAssignmentStudentWorkDTO detail = getTeacherStudentSubmissionDetail(teacherId, classId, assignmentId, studentId);
        if (detail == null || !detail.isSubmitted() || detail.getSubId() <= 0) {
            throw new IllegalArgumentException("학생 제출 내역이 없습니다.");
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("assignmentId", assignmentId);
        paramMap.put("studentId", studentId);
        paramMap.put("subId", detail.getSubId());
        paramMap.put("returnReason", normalizedReason);
        teacherAssignmentDAO.updateTeacherStudentSubmissionReject(paramMap);
    }

    @Override
    public void completeTeacherStudentSubmission(int teacherId, int classId, int assignmentId, int studentId) throws Exception {
        TeacherAssignmentStudentWorkDTO detail = getTeacherStudentSubmissionDetail(teacherId, classId, assignmentId, studentId);
        if (detail == null || !detail.isSubmitted() || detail.getSubId() <= 0) {
            throw new IllegalArgumentException("학생 제출 내역이 없습니다.");
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("assignmentId", assignmentId);
        paramMap.put("studentId", studentId);
        paramMap.put("subId", detail.getSubId());
        teacherAssignmentDAO.updateTeacherStudentSubmissionComplete(paramMap);
    }


    @Override
    public void completeTeacherStudentSubmissionWithFeedback(int teacherId, int classId, int assignmentId, int studentId, String feedbackContent) throws Exception {
        String normalizedFeedback = feedbackContent == null ? "" : feedbackContent.trim();
        if (normalizedFeedback.isBlank()) {
            throw new IllegalArgumentException("피드백 내용을 입력해주세요.");
        }

        TeacherAssignmentStudentWorkDTO detail = getTeacherStudentSubmissionDetail(teacherId, classId, assignmentId, studentId);
        if (detail == null || !detail.isSubmitted() || detail.getSubId() <= 0) {
            throw new IllegalArgumentException("학생 제출 내역이 없습니다.");
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("assignmentId", assignmentId);
        paramMap.put("studentId", studentId);
        paramMap.put("subId", detail.getSubId());

        Integer feedbackId = teacherAssignmentDAO.selectAssignmentFeedbackIdBySubId(detail.getSubId());
        if (feedbackId == null) {
            feedbackId = teacherAssignmentDAO.selectNextAssignmentFeedbackId();
            if (feedbackId == null) {
                feedbackId = 1;
            }
            paramMap.put("feedbackId", feedbackId);
            paramMap.put("feedbackContent", normalizedFeedback);
            teacherAssignmentDAO.insertTeacherStudentSubmissionFeedback(paramMap);
        } else {
            paramMap.put("feedbackId", feedbackId);
            paramMap.put("feedbackContent", normalizedFeedback);
            teacherAssignmentDAO.updateTeacherStudentSubmissionFeedback(paramMap);
        }

        teacherAssignmentDAO.updateTeacherStudentSubmissionComplete(paramMap);
    }

    private void normalize(TeacherAssignmentSaveDTO dto) {
        if (dto.getTitle() != null) dto.setTitle(dto.getTitle().trim());
        if (dto.getContent() != null) dto.setContent(dto.getContent().trim());
        if (dto.getContent() == null || dto.getContent().isBlank()) dto.setContent(" ");
        if (dto.getSubject() == null || dto.getSubject().isBlank()) dto.setSubject("국어");
        if (dto.getSubmitFormat() == null || dto.getSubmitFormat().isBlank()) dto.setSubmitFormat("파일");
    }
}
