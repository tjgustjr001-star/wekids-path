package com.spring.service.admin;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.admin.AdminClassDAO;
import com.spring.dto.admin.AdminClassListDTO;

@Service
public class AdminClassServiceImpl implements AdminClassService {

	@Autowired
	private AdminClassDAO adminClassDAO;

	@Override
	public List<AdminClassListDTO> getClassList() throws SQLException {
		return adminClassDAO.selectClassList();
	}

	@Override
	public void modifyClassStatus(int classId, String classStatus) throws SQLException {
		adminClassDAO.updateClassStatus(classId, classStatus);
	}
	
	@Override
	public int getActiveClassCount() throws SQLException {
	    return adminClassDAO.selectActiveClassCount();
	}
}