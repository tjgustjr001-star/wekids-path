package com.spring.dao.admin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.dto.admin.AdminClassListDTO;

@Repository
public class AdminClassDAOImpl implements AdminClassDAO {

	@Autowired
	private SqlSession session;

	@Override
	public List<AdminClassListDTO> selectClassList() throws SQLException {
		return session.selectList("AdminClass-Mapper.selectClassList");
	}

	@Override
	public void updateClassStatus(int classId, String classStatus) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("classId", classId);
		paramMap.put("classStatus", classStatus);

		session.update("AdminClass-Mapper.updateClassStatus", paramMap);
	}
	
	@Override
	public int selectActiveClassCount() throws SQLException {
	    Integer count = session.selectOne("AdminClass-Mapper.selectActiveClassCount");
	    return count == null ? 0 : count;
	}
}