//package com.spring.dao;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.ibatis.session.SqlSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import com.spring.dto.ClassVO;
//import com.spring.dto.TeacherClassCreateDTO;
//
//@Repository
//public class ClassDAOImpl implements ClassDAO {
//
//    private static final String NAMESPACE = "com.spring.dao.ClassDAO.";
//
//    @Autowired
//    private SqlSession sqlSession;
//
//    @Override
//    public int selectNextClassId() throws Exception {
//        return sqlSession.selectOne(NAMESPACE + "selectNextClassId");
//    }
//
//    @Override
//    public int selectNextTeacherClassId() throws Exception {
//        return sqlSession.selectOne(NAMESPACE + "selectNextTeacherClassId");
//    }
//
//    @Override
//    public void insertClass(int classId, String inviteCode, TeacherClassCreateDTO dto) throws Exception {
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("classId", classId);
//        paramMap.put("inviteCode", inviteCode);
//        paramMap.put("dto", dto);
//
//        sqlSession.insert(NAMESPACE + "insertClass", paramMap);
//    }
//
//    @Override
//    public void insertTeacherClass(int teacherClassId, int teacherId, int classId) throws Exception {
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("teacherClassId", teacherClassId);
//        paramMap.put("teacherId", teacherId);
//        paramMap.put("classId", classId);
//
//        sqlSession.insert(NAMESPACE + "insertTeacherClass", paramMap);
//    }
//
//    @Override
//    public List<ClassVO> selectTeacherClassList(int teacherId) throws Exception {
//        return sqlSession.selectList(NAMESPACE + "selectTeacherClassList", teacherId);
//    }
//
//    @Override
//    public ClassVO selectTeacherClassDetail(int teacherId, int classId) throws Exception {
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("teacherId", teacherId);
//        paramMap.put("classId", classId);
//
//        return sqlSession.selectOne(NAMESPACE + "selectTeacherClassDetail", paramMap);
//    }
//
//	@Override
//	public List<ClassVO> selectStudentClassList(int studentId) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ClassVO selectStudentClassDetail(int studentId, int classId) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<ClassVO> selectParentClassList(int parentId) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ClassVO selectParentClassDetail(int parentId, int classId) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//}