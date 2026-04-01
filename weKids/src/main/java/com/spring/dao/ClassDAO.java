package com.spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.ClassVO;
import com.spring.dto.parent.ParentChildClassOptionDTO;
import com.spring.dto.teacher.TeacherClassCreateDTO;
import com.spring.dto.teacher.TeacherClassManageDTO;
import com.spring.dto.teacher.TeacherStudentManageDTO;
import com.spring.dto.teacher.TeacherStudentObservationSaveDTO;

public interface ClassDAO {

    int selectNextClassId() throws Exception;

    int selectNextTeacherClassId() throws Exception;

    int selectNextStudentClassId() throws Exception;
    
    
    
    int countStudentClassByStudentIdAndClassId(@Param("studentId") int studentId,
            @Param("classId") int classId) throws Exception;
  
	int countTeacherClassStudents(@Param("teacherId") int teacherId,
		     @Param("classId") int classId) throws Exception;
		
		int selectNextStudentClassTagId() throws Exception;

		int countActiveTeacherStudentClass(@Param("teacherId") int teacherId,
		                                   @Param("studentClassId") int studentClassId) throws Exception;
		int countActiveStudentClassByStudentIdAndClassId(@Param("studentId") int studentId,
	                @Param("classId") int classId) throws Exception;
	
	int countRemovedStudentClassByStudentIdAndClassId(@Param("studentId") int studentId,
	                 @Param("classId") int classId) throws Exception;
	
	void reactivateStudentClass(@Param("studentId") int studentId,
					@Param("classId") int classId) throws Exception;
	
    void insertClass(@Param("classId") int classId,
                     @Param("inviteCode") String inviteCode,
                     @Param("dto") TeacherClassCreateDTO dto) throws Exception;

    void insertTeacherClass(@Param("teacherClassId") int teacherClassId,
                            @Param("teacherId") int teacherId,
                            @Param("classId") int classId) throws Exception;

    void insertStudentClass(@Param("studentClassId") int studentClassId,
                            @Param("studentId") int studentId,
                            @Param("classId") int classId) throws Exception;
    void updateTeacherClassBasicSettings(@Param("teacherId") int teacherId,
            @Param("classId") int classId,
            @Param("dto") TeacherClassManageDTO dto) throws Exception;

	void updateTeacherAssignmentSettings(@Param("teacherId") int teacherId,
	            @Param("classId") int classId,
	            @Param("dto") TeacherClassManageDTO dto) throws Exception;
	
	void regenerateInviteCode(@Param("teacherId") int teacherId,
	 @Param("classId") int classId,
	 @Param("inviteCode") String inviteCode) throws Exception;
	
	void archiveTeacherClass(@Param("teacherId") int teacherId,
	@Param("classId") int classId) throws Exception;
	


	void updateStudentObservationMemo(@Param("teacherId") int teacherId,
	                                  @Param("dto") TeacherStudentObservationSaveDTO dto) throws Exception;

	void deleteStudentObservationTags(@Param("teacherId") int teacherId,
	                                  @Param("studentClassId") int studentClassId) throws Exception;

	void insertStudentObservationTag(@Param("studentClassTagId") int studentClassTagId,
	                                 @Param("studentClassId") int studentClassId,
	                                 @Param("tagName") String tagName) throws Exception;

	void endStudentClass(@Param("teacherId") int teacherId,
	                     @Param("studentClassId") int studentClassId) throws Exception;
	
    List<ClassVO> selectTeacherClassList(@Param("teacherId") int teacherId) throws Exception;

    ClassVO selectTeacherClassDetail(@Param("teacherId") int teacherId,
                                     @Param("classId") int classId) throws Exception;

    List<ClassVO> selectStudentClassList(@Param("studentId") int studentId) throws Exception;

    ClassVO selectStudentClassDetail(@Param("studentId") int studentId,
                                     @Param("classId") int classId) throws Exception;

    List<ClassVO> selectParentClassList(@Param("parentId") int parentId) throws Exception;

    ClassVO selectParentClassDetail(@Param("parentId") int parentId,
                                    @Param("classId") int classId) throws Exception;
    
    ClassVO selectClassByInviteCode(@Param("inviteCode") String inviteCode) throws Exception;
    
    List<TeacherStudentManageDTO> selectTeacherStudentManageList(@Param("teacherId") int teacherId,
            @Param("classId") int classId) throws Exception;
    
    List<ParentChildClassOptionDTO> selectParentChildClassOptions(@Param("parentId") int parentId) throws Exception;
    
}