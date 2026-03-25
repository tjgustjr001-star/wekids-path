package com.spring.dao.admin;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.admin.AdminClassListDTO;

public interface AdminClassDAO {

	List<AdminClassListDTO> selectClassList() throws SQLException;

	void updateClassStatus(@Param("classId") int classId,
						   @Param("classStatus") String classStatus) throws SQLException;
}