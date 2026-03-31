package com.spring.service;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.dao.StudentAssignmentDAO;
import com.spring.dto.student.StudentAssignmentAttachDTO;
import com.spring.dto.student.StudentAssignmentItemDTO;
import com.spring.dto.student.StudentAssignmentSubmitDTO;

@Service
public class StudentAssignmentServiceImpl implements StudentAssignmentService {

    @Autowired
    private StudentAssignmentDAO studentAssignmentDAO;

    @Override
    public List<StudentAssignmentItemDTO> getStudentAssignmentList(int studentId, int classId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("studentId", studentId);
        paramMap.put("classId", classId);

        List<StudentAssignmentItemDTO> list = studentAssignmentDAO.selectStudentAssignmentList(paramMap);
        for (StudentAssignmentItemDTO item : list) {
            applyDisplayMeta(item);
        }
        return list;
    }

    @Override
    public StudentAssignmentItemDTO getStudentAssignmentDetail(int studentId, int classId, int assignmentId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("studentId", studentId);
        paramMap.put("classId", classId);
        paramMap.put("assignmentId", assignmentId);

        StudentAssignmentItemDTO item = studentAssignmentDAO.selectStudentAssignmentDetail(paramMap);
        if (item == null) {
            throw new IllegalArgumentException("과제를 찾을 수 없습니다.");
        }
        applyDisplayMeta(item);
        return item;
    }

    @Override
    public StudentAssignmentItemDTO submitStudentAssignment(int studentId, int classId, int assignmentId,
                                                            String content,
                                                            MultipartFile uploadFile,
                                                            String saveDir) throws Exception {

        StudentAssignmentItemDTO detail = getStudentAssignmentDetail(studentId, classId, assignmentId);

        String submitFormat = safeUpper(detail.getSubmitFormat());

        boolean needText = submitFormat.contains("TEXT") || submitFormat.contains("텍스트");
        boolean needFile = submitFormat.contains("FILE") || submitFormat.contains("파일")
                || submitFormat.contains("IMAGE") || submitFormat.contains("이미지");

        // 텍스트 필수 검사
        if (needText && (content == null || content.trim().isEmpty())) {
            throw new IllegalArgumentException("제출 내용을 입력해주세요.");
        }

        // 파일 필수 검사
        if (needFile && (uploadFile == null || uploadFile.isEmpty()) && !detail.isCanDownload()) {
            throw new IllegalArgumentException("첨부 파일을 선택해주세요.");
        }

        // 기존 제출 가져오기
        StudentAssignmentSubmitDTO latest = getLatestSubmit(studentId, assignmentId);
        int nextSubmitCount = latest == null ? 1 : latest.getSubmitCount() + 1;
        boolean lateSubmit = detail.isExpired();

        // 수정횟수 제한 체크
        boolean rejected = "반려".equals(detail.getStatus());
        if (latest != null && !rejected) {
            int usedEditCount = Math.max(0, latest.getSubmitCount() - 1);
            if (usedEditCount >= detail.getMaxEditCount()) {
                throw new IllegalArgumentException("선생님이 설정한 수정 가능 횟수를 모두 사용했습니다.");
            }
        }

        // 기존 파일 유지
        StudentAssignmentAttachDTO latestAttach = getLatestAttach(assignmentId, studentId);
        String attachedFileName = latestAttach == null ? null : latestAttach.getFileName();
        String uploadPath = latestAttach == null ? null : latestAttach.getUploadPath();

        // 새 파일 업로드
        if (uploadFile != null && !uploadFile.isEmpty()) {
            validateUploadFile(detail.getSubmitFormat(), uploadFile);

            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String ext = getExtension(uploadFile.getOriginalFilename());
            String generated = UUID.randomUUID().toString().replace("-", "");
            if (!ext.isBlank()) {
                generated += "." + ext;
            }

            File dest = new File(dir, generated);
            uploadFile.transferTo(dest);

            attachedFileName = uploadFile.getOriginalFilename();
            uploadPath = dest.getAbsolutePath();
        }

        // 🔥🔥🔥 핵심 추가 (CONTENT NULL 방지)
        if (content == null || content.trim().isEmpty()) {
            if (uploadFile != null && !uploadFile.isEmpty()) {
                content = "파일 제출";
            } else if (attachedFileName != null && !attachedFileName.isBlank()) {
                content = "첨부파일 유지 제출";
            } else {
                content = "제출";
            }
        }

        // 반려된 재제출은 기존 제출행을 수정하고, 일반 제출은 새 행을 추가
        boolean reuseLatestSubmit = latest != null && rejected;

        int targetSubId;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("assignmentId", assignmentId);
        paramMap.put("studentId", studentId);
        paramMap.put("content", content);
        paramMap.put("submitStatus", lateSubmit ? "LATE_SUBMITTED" : (latest == null ? "SUBMITTED" : "RESUBMITTED"));
        paramMap.put("submitCount", nextSubmitCount);

        if (reuseLatestSubmit) {
            targetSubId = latest.getSubId();
            paramMap.put("subId", targetSubId);
            studentAssignmentDAO.updateStudentAssignmentSubmit(paramMap);
            studentAssignmentDAO.deleteStudentAssignmentAttachByPno(targetSubId);
            studentAssignmentDAO.deleteStudentAssignmentFeedbackBySubId(targetSubId);
        } else {
            Integer nextSubId = studentAssignmentDAO.selectNextAssignmentSubId();
            if (nextSubId == null) {
                nextSubId = 1;
            }
            targetSubId = nextSubId;
            paramMap.put("subId", targetSubId);
            studentAssignmentDAO.insertStudentAssignmentSubmit(paramMap);
        }

        // 첨부파일 저장 (ATTACH)
        if (attachedFileName != null && !attachedFileName.isBlank()
                && uploadPath != null && !uploadPath.isBlank()) {

            Integer nextAno = studentAssignmentDAO.selectNextAttachAno();
            if (nextAno == null) {
                nextAno = 1;
            }

            Map<String, Object> attachMap = new HashMap<>();
            attachMap.put("ano", nextAno);
            attachMap.put("pno", targetSubId);
            attachMap.put("uploadPath", uploadPath);
            attachMap.put("fileName", attachedFileName);
            attachMap.put("fileType", getExtension(attachedFileName).toLowerCase());
            attachMap.put("attacher", String.valueOf(studentId));

            studentAssignmentDAO.insertStudentAssignmentAttach(attachMap);
        }

        return getStudentAssignmentDetail(studentId, classId, assignmentId);
    }

    private StudentAssignmentSubmitDTO getLatestSubmit(int studentId, int assignmentId) throws SQLException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("studentId", studentId);
        paramMap.put("assignmentId", assignmentId);
        return studentAssignmentDAO.selectLatestStudentAssignmentSubmit(paramMap);
    }

    private StudentAssignmentAttachDTO getLatestAttach(int assignmentId, int studentId) throws SQLException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("assignmentId", assignmentId);
        paramMap.put("studentId", studentId);
        return studentAssignmentDAO.selectLatestStudentAssignmentAttach(paramMap);
    }

    private void applyDisplayMeta(StudentAssignmentItemDTO item) {
        item.setSubmitFormatLabel(toSubmitFormatLabel(item.getSubmitFormat()));

        int usedEditCount = Math.max(0, item.getSubmitCount() - 1);
        int remainingEditCount = Math.max(0, item.getMaxEditCount() - usedEditCount);
        item.setRemainingEditCount(remainingEditCount);

        boolean hasFeedback = item.getFeedback() != null && !item.getFeedback().trim().isEmpty();
        boolean rejected = "반려".equals(item.getStatus());
        boolean confirmed = "확인완료".equals(item.getStatus());

        boolean submitted = item.isSubmitted();
        boolean canNewSubmit = !submitted;
        boolean canResubmit = submitted && remainingEditCount > 0;

        if (rejected) {
            canResubmit = true;
        }
        if (confirmed) {
            canResubmit = false;
        }

        item.setCanSubmit(canNewSubmit || canResubmit);
        item.setCanResubmit(canResubmit);

        if (item.getUploadPath() != null && !item.getUploadPath().isBlank()) {
            Long resolvedSize = resolveFileSize(item.getUploadPath());
            if (resolvedSize != null) {
                item.setFileSize(resolvedSize);
            }
        }

        item.setCanDownload(item.getAttachedFile() != null && !item.getAttachedFile().isBlank()
                && item.getUploadPath() != null && !item.getUploadPath().isBlank());
    }

    private String toSubmitFormatLabel(String submitFormat) {
        String value = safeUpper(submitFormat);
        if (value.contains("TEXT") || value.contains("텍스트")) {
            return "텍스트 작성";
        }
        if (value.contains("IMAGE") || value.contains("이미지")) {
            return "이미지 제출";
        }
        return "파일 제출";
    }

    private void validateUploadFile(String submitFormat, MultipartFile uploadFile) throws Exception {
        if (uploadFile.getSize() > 50L * 1024L * 1024L) {
            throw new IllegalArgumentException("파일 크기는 50MB 이하여야 합니다.");
        }

        String ext = getExtension(uploadFile.getOriginalFilename()).toLowerCase();
        String value = safeUpper(submitFormat);

        if (value.contains("IMAGE") || value.contains("이미지")) {
            if (!("jpg".equals(ext) || "jpeg".equals(ext) || "png".equals(ext) || "webp".equals(ext))) {
                throw new IllegalArgumentException("이미지 제출은 JPG, PNG, WEBP 파일만 가능합니다.");
            }
            return;
        }

        if (!("hwp".equals(ext) || "pdf".equals(ext) || "jpg".equals(ext) || "jpeg".equals(ext)
                || "png".equals(ext) || "webp".equals(ext) || "doc".equals(ext) || "docx".equals(ext))) {
            throw new IllegalArgumentException("업로드할 수 없는 파일 형식입니다.");
        }
    }

    private Long resolveFileSize(String uploadPath) {
        if (uploadPath == null || uploadPath.isBlank()) {
            return null;
        }
        File file = new File(uploadPath);
        return file.exists() ? file.length() : null;
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private String safeUpper(String value) {
        return value == null ? "" : value.toUpperCase();
    }
}