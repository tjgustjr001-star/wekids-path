package com.spring.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.ClassDAO;
import com.spring.dto.ClassVO;
import com.spring.dto.teacher.TeacherClassCreateDTO;
import com.spring.dto.teacher.TeacherClassManageDTO;
import com.spring.dto.teacher.TeacherStudentManageDTO;
import com.spring.dto.teacher.TeacherStudentObservationSaveDTO;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassDAO classDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createClass(int teacherId, TeacherClassCreateDTO dto) throws Exception {
        int classId = classDAO.selectNextClassId();
        int teacherClassId = classDAO.selectNextTeacherClassId();
        String inviteCode = "INV-" + classId;

        classDAO.insertClass(classId, inviteCode, dto);
        classDAO.insertTeacherClass(teacherClassId, teacherId, classId);
    }

    @Override
    public List<ClassVO> getTeacherClassList(int teacherId) throws Exception {
        List<ClassVO> list = classDAO.selectTeacherClassList(teacherId);
        applyViewFields(list);
        return list;
    }

    @Override
    public ClassVO getTeacherClassDetail(int teacherId, int classId) throws Exception {
        return classDAO.selectTeacherClassDetail(teacherId, classId);
    }

    @Override
    public List<ClassVO> getStudentClassList(int studentId) throws Exception {
        List<ClassVO> list = classDAO.selectStudentClassList(studentId);
        applyViewFields(list);
        return list;
    }

    @Override
    public ClassVO getStudentClassDetail(int studentId, int classId) throws Exception {
        return classDAO.selectStudentClassDetail(studentId, classId);
    }

    @Override
    public List<ClassVO> getParentClassList(int parentId) throws Exception {
        List<ClassVO> list = classDAO.selectParentClassList(parentId);
        applyViewFields(list);
        return list;
    }

    @Override
    public ClassVO getParentClassDetail(int parentId, int classId) throws Exception {
        return classDAO.selectParentClassDetail(parentId, classId);
    }

    private void applyViewFields(List<ClassVO> list) {
        String[] coverTypes = { "blue", "green", "dark", "brand-blue" };

        for (int i = 0; i < list.size(); i++) {
            ClassVO item = list.get(i);
            item.setYearLabel(item.getYear() + "학년도");
            item.setCoverType(coverTypes[i % coverTypes.length]);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void joinClass(int studentId, String inviteCode) throws Exception {
        if (inviteCode == null || inviteCode.trim().isEmpty()) {
            throw new IllegalArgumentException("초대 코드를 입력해주세요.");
        }

        String normalizedCode = inviteCode.trim().toUpperCase();

        ClassVO classInfo = classDAO.selectClassByInviteCode(normalizedCode);

        if (classInfo == null) {
            throw new IllegalArgumentException("유효한 초대 코드가 아닙니다.");
        }

        int activeCount = classDAO.countActiveStudentClassByStudentIdAndClassId(studentId, classInfo.getClassId());
        if (activeCount > 0) {
            throw new IllegalArgumentException("이미 가입한 클래스입니다.");
        }

        int removedCount = classDAO.countRemovedStudentClassByStudentIdAndClassId(studentId, classInfo.getClassId());
        if (removedCount > 0) {
            classDAO.reactivateStudentClass(studentId, classInfo.getClassId());
            return;
        }

        int studentClassId = classDAO.selectNextStudentClassId();
        classDAO.insertStudentClass(studentClassId, studentId, classInfo.getClassId());
    }
    @Override
    public void updateTeacherClassBasicSettings(int teacherId, int classId, TeacherClassManageDTO dto) throws Exception {
        classDAO.updateTeacherClassBasicSettings(teacherId, classId, dto);
    }

    @Override
    public void updateTeacherAssignmentSettings(int teacherId, int classId, TeacherClassManageDTO dto) throws Exception {
        classDAO.updateTeacherAssignmentSettings(teacherId, classId, dto);
    }

    @Override
    public void regenerateInviteCode(int teacherId, int classId) throws Exception {
        String inviteCode = "INV-" + classId + "-" + randomCode(4);
        classDAO.regenerateInviteCode(teacherId, classId, inviteCode);
    }

    @Override
    public void archiveTeacherClass(int teacherId, int classId) throws Exception {
        classDAO.archiveTeacherClass(teacherId, classId);
    }

    @Override
    public int getTeacherClassStudentCount(int teacherId, int classId) throws Exception {
        return classDAO.countTeacherClassStudents(teacherId, classId);
    }

    private String randomCode(int length) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int idx = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(idx));
        }

        return sb.toString();
    }
    
    @Override
    public List<TeacherStudentManageDTO> getTeacherStudentManageList(int teacherId, int classId) throws Exception {
        return classDAO.selectTeacherStudentManageList(teacherId, classId);
    }
    
    @Override
    public void saveTeacherStudentObservation(int teacherId, TeacherStudentObservationSaveDTO dto) throws Exception {
        int count = classDAO.countActiveTeacherStudentClass(teacherId, dto.getStudentClassId());
        if (count == 0) {
            throw new IllegalArgumentException("유효하지 않은 학생 클래스 정보입니다.");
        }

        classDAO.updateStudentObservationMemo(teacherId, dto);
        classDAO.deleteStudentObservationTags(teacherId, dto.getStudentClassId());

        if (dto.getTagNameList() == null || dto.getTagNameList().isEmpty()) {
            return;
        }

        Set<String> uniqueTags = new LinkedHashSet<>();
        for (String tag : dto.getTagNameList()) {
            if (tag == null) continue;
            String trimmed = tag.trim();
            if (!trimmed.isEmpty()) {
                uniqueTags.add(trimmed);
            }
        }

        for (String tagName : uniqueTags) {
            int studentClassTagId = classDAO.selectNextStudentClassTagId();
            classDAO.insertStudentObservationTag(studentClassTagId, dto.getStudentClassId(), tagName);
        }
    }

    @Override
    public void removeStudentFromClass(int teacherId, int studentClassId) throws Exception {
        int count = classDAO.countActiveTeacherStudentClass(teacherId, studentClassId);
        if (count == 0) {
            throw new IllegalArgumentException("유효하지 않은 학생 클래스 정보입니다.");
        }

        classDAO.endStudentClass(teacherId, studentClassId);
    }
}