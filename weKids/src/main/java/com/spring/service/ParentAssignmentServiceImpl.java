package com.spring.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.ParentAssignmentDAO;
import com.spring.dto.parent.ParentAssignmentChildDTO;
import com.spring.dto.student.StudentAssignmentItemDTO;

@Service
public class ParentAssignmentServiceImpl implements ParentAssignmentService {

    @Autowired
    private ParentAssignmentDAO parentAssignmentDAO;

    @Override
    public List<ParentAssignmentChildDTO> getParentAssignmentChildren(int parentId, int classId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("parentId", parentId);
        paramMap.put("classId", classId);
        return parentAssignmentDAO.selectParentAssignmentChildren(paramMap);
    }

    @Override
    public List<StudentAssignmentItemDTO> getParentAssignmentList(int parentId, int classId, int studentId) throws Exception {
        Map<String, Object> paramMap = createParamMap(parentId, classId, studentId, null);
        List<StudentAssignmentItemDTO> list = parentAssignmentDAO.selectParentAssignmentList(paramMap);
        for (StudentAssignmentItemDTO item : list) {
            applyDisplayMeta(item);
        }
        return list;
    }

    @Override
    public StudentAssignmentItemDTO getParentAssignmentDetail(int parentId, int classId, int studentId, int assignmentId) throws Exception {
        Map<String, Object> paramMap = createParamMap(parentId, classId, studentId, assignmentId);
        StudentAssignmentItemDTO item = parentAssignmentDAO.selectParentAssignmentDetail(paramMap);
        if (item == null) {
            throw new IllegalArgumentException("과제를 찾을 수 없습니다.");
        }
        applyDisplayMeta(item);
        return item;
    }

    private Map<String, Object> createParamMap(int parentId, int classId, int studentId, Integer assignmentId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("parentId", parentId);
        paramMap.put("classId", classId);
        paramMap.put("studentId", studentId);
        if (assignmentId != null) {
            paramMap.put("assignmentId", assignmentId);
        }
        return paramMap;
    }

    private void applyDisplayMeta(StudentAssignmentItemDTO item) {
        item.setSubmitFormatLabel(toSubmitFormatLabel(item.getSubmitFormat()));

        if ("확인완료".equals(item.getStatus())) {
            item.setStatus("확인완료");
        } else if ("반려".equals(item.getStatus())) {
            item.setStatus("반려");
        }

        if (item.getUploadPath() != null && !item.getUploadPath().isBlank()) {
            File file = new File(item.getUploadPath());
            if (file.exists()) {
                item.setFileSize(file.length());
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

    private String safeUpper(String value) {
        return value == null ? "" : value.toUpperCase();
    }
}
