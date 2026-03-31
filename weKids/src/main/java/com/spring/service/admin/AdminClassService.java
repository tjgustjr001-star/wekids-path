package com.spring.service.admin;

import java.sql.SQLException;
import java.util.List;

import com.spring.dto.admin.AdminClassListDTO;

public interface AdminClassService {

	List<AdminClassListDTO> getClassList() throws SQLException;

	void modifyClassStatus(int classId, String classStatus) throws SQLException;
	
	int getActiveClassCount() throws SQLException;
}