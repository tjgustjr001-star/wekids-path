package com.spring.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.TeacherLearnDAO;
import com.spring.dto.teacher.TeacherLearnDifficultyDTO;
import com.spring.dto.teacher.TeacherLearnManageDTO;
import com.spring.dto.teacher.TeacherLearnProgressDTO;
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
    @Transactional(rollbackFor = Exception.class)
    public void registTeacherLearn(int teacherId, int classId, TeacherLearnSaveDTO dto) throws Exception {
        normalizeTeacherLearnDto(dto);

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
    @Transactional(rollbackFor = Exception.class)
    public void modifyTeacherLearn(int teacherId, int classId, int learnId, TeacherLearnSaveDTO dto) throws Exception {
        normalizeTeacherLearnDto(dto);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);
        paramMap.put("dto", dto);

        teacherLearnDAO.updateLearnList(paramMap);
        teacherLearnDAO.updateLearnCont(paramMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTeacherLearn(int teacherId, int classId, int learnId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);

        teacherLearnDAO.softDeleteLearn(paramMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreTeacherLearn(int teacherId, int classId, int learnId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);

        teacherLearnDAO.restoreLearn(paramMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTeacherLearn(int teacherId, int classId, int learnId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);

        TeacherLearnManageDTO learn = teacherLearnDAO.selectTeacherLearnDetail(paramMap);
        if (learn == null) {
            throw new IllegalArgumentException("존재하지 않는 학습입니다.");
        }

        if (!learn.isDeleted()) {
            throw new IllegalStateException("휴지통에 있는 학습만 영구삭제할 수 있습니다.");
        }

        paramMap.put("learnListId", learn.getLearnListId());

        teacherLearnDAO.deleteLearnFeedbackByLearnId(paramMap);
        teacherLearnDAO.deleteLearnProgressByLearnId(paramMap);
        teacherLearnDAO.deleteLearnCont(paramMap);
        teacherLearnDAO.deleteLearnListIfNoChildren(paramMap);
    }

    private void normalizeTeacherLearnDto(TeacherLearnSaveDTO dto) {
        if (dto == null) return;

        dto.setTitle(trimToNull(dto.getTitle()));
        dto.setType(trimToNull(dto.getType()));
        dto.setRequired(trimToNull(dto.getRequired()));
        dto.setStatus(trimToNull(dto.getStatus()));
        dto.setStartDate(trimToNull(dto.getStartDate()));
        dto.setDeadline(trimToNull(dto.getDeadline()));
        dto.setLinkUrl(trimToNull(dto.getLinkUrl()));
        dto.setTextContent(trimToNull(dto.getTextContent()));
        dto.setContent(trimToNull(dto.getContent()));

        if (dto.getDuration() != null && dto.getDuration() <= 0) {
            dto.setDuration(null);
        }

        String type = dto.getType();

        if ("영상".equals(type)) {
            dto.setTextContent(null);
            if (dto.getLinkUrl() == null) {
                throw new IllegalArgumentException("영상 유형은 영상 URL이 필요합니다.");
            }
        } else if ("링크".equals(type)) {
            dto.setTextContent(null);
            if (dto.getLinkUrl() == null) {
                throw new IllegalArgumentException("링크 유형은 링크 URL이 필요합니다.");
            }
        } else if ("지문읽기".equals(type)) {
            dto.setLinkUrl(null);

            String textBody = trimToNull(dto.getTextContent());
            String contentBody = trimToNull(dto.getContent());

            // 긴 지문 본문은 CONTENT(CLOB)에 저장
            if (contentBody == null && textBody != null) {
                dto.setContent(textBody);
            }

            // GUIDE_POINT(VARCHAR2 1000)에는 긴 지문 넣지 않음
            dto.setTextContent(null);

            if (dto.getContent() == null) {
                throw new IllegalArgumentException("지문읽기 유형은 지문 내용이 필요합니다.");
            }
        }else if ("파일".equals(type)) {
            dto.setTextContent(null);
            if (dto.getLinkUrl() == null) {
                throw new IllegalArgumentException("파일 유형은 파일 경로 또는 URL이 필요합니다.");
            }
        } else {
            throw new IllegalArgumentException("올바르지 않은 학습 유형입니다.");
        }
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    @Override
    public List<TeacherLearnProgressDTO> getTeacherLearnProgressList(int teacherId, int classId, int learnId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);
        return teacherLearnDAO.selectTeacherLearnProgressList(paramMap);
    }
    
    @Override
    public List<TeacherLearnDifficultyDTO> getTeacherLearnDifficultyList(int teacherId, int classId, int learnId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teacherId", teacherId);
        paramMap.put("classId", classId);
        paramMap.put("learnId", learnId);
        return teacherLearnDAO.selectTeacherLearnDifficultyList(paramMap);
    }
}