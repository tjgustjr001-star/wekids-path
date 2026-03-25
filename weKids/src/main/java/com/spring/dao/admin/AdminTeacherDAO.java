package com.spring.dao.admin;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.admin.AdminTeacherClassDTO;
import com.spring.dto.admin.AdminTeacherDetailDTO;
import com.spring.dto.admin.AdminTeacherListDTO;
import com.spring.dto.admin.AdminTeacherRegistDTO;

public interface AdminTeacherDAO {

	List<AdminTeacherListDTO> selectTeacherList() throws SQLException;

	AdminTeacherDetailDTO selectTeacherDetailById(@Param("teacherId") int teacherId) throws SQLException;

	List<AdminTeacherClassDTO> selectTeacherClassListById(@Param("teacherId") int teacherId) throws SQLException;
	
	void updateTeacherStatus(@Param("teacherId") int teacherId,
							 @Param("accountStatus") String accountStatus) throws SQLException;
	
	int selectNextMemberId() throws SQLException;
	int selectNextAuthorityId() throws SQLException;

	void insertTeacherMember(@Param("memberId") int memberId,
							 @Param("registDTO") AdminTeacherRegistDTO registDTO) throws SQLException;

	void insertTeacherAuthority(@Param("authorityId") int authorityId,
								@Param("memberId") int memberId) throws SQLException;

	void insertTeacher(@Param("memberId") int memberId,
					   @Param("registDTO") AdminTeacherRegistDTO registDTO) throws SQLException;
}