package com.spring.service;

import java.util.List;

import com.spring.dto.ClassVO;
import com.spring.dto.parent.ParentChildClassOptionDTO;
import com.spring.dto.teacher.TeacherClassCreateDTO;
import com.spring.dto.teacher.TeacherClassManageDTO;
import com.spring.dto.teacher.TeacherStudentManageDTO;
import com.spring.dto.teacher.TeacherStudentObservationSaveDTO;

public interface ClassService {

    void createClass(int teacherId, TeacherClassCreateDTO dto) throws Exception;

    void joinClass(int studentId, String inviteCode) throws Exception;
    
    void updateTeacherClassBasicSettings(int teacherId, int classId, TeacherClassManageDTO dto) throws Exception;

    void updateTeacherAssignmentSettings(int teacherId, int classId, TeacherClassManageDTO dto) throws Exception;

    void regenerateInviteCode(int teacherId, int classId) throws Exception;

    void archiveTeacherClass(int teacherId, int classId) throws Exception;

    int getTeacherClassStudentCount(int teacherId, int classId) throws Exception;
    
    void saveTeacherStudentObservation(int teacherId, TeacherStudentObservationSaveDTO dto) throws Exception;

    void removeStudentFromClass(int teacherId, int studentClassId) throws Exception;
    
    List<ClassVO> getTeacherClassList(int teacherId) throws Exception;
    ClassVO getTeacherClassDetail(int teacherId, int classId) throws Exception;

    List<ClassVO> getStudentClassList(int studentId) throws Exception;
    ClassVO getStudentClassDetail(int studentId, int classId) throws Exception;

    List<ClassVO> getParentClassList(int parentId) throws Exception;
    ClassVO getParentClassDetail(int parentId, int classId) throws Exception;
    
    List<TeacherStudentManageDTO> getTeacherStudentManageList(int teacherId, int classId) throws Exception;
    
    List<ParentChildClassOptionDTO> getParentChildClassOptions(int parentId) throws Exception;
}