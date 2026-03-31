package com.spring.service.admin;

import java.sql.SQLException;
import java.util.List;

import com.spring.dto.admin.AdminTeacherClassDTO;
import com.spring.dto.admin.AdminTeacherDetailDTO;
import com.spring.dto.admin.AdminTeacherListDTO;
import com.spring.dto.admin.AdminTeacherRegistDTO;
import com.spring.dto.admin.MonthlyJoinCountDTO;

public interface AdminTeacherService {

	List<AdminTeacherListDTO> getTeacherList() throws SQLException;

	AdminTeacherDetailDTO getTeacherDetailById(int teacherId) throws SQLException;

	List<AdminTeacherClassDTO> getTeacherClassListById(int teacherId) throws SQLException;

	void modifyTeacherStatus(int teacherId, String accountStatus) throws SQLException;

	void registTeacher(AdminTeacherRegistDTO registDTO) throws SQLException;

    List<MonthlyJoinCountDTO> getTeacherJoinTrend() throws SQLException;
    
    int getNewTeacherCount() throws SQLException;
}