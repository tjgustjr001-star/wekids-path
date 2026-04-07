package com.spring.dao.admin;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.dto.SupportAnswerVO;
import com.spring.dto.SupportFileVO;
import com.spring.dto.SupportVO;

@Repository
public class AdminSupportDAOImpl implements AdminSupportDAO {

    @Autowired
    private SqlSession sqlSession;

    private static final String NAMESPACE = "com.spring.dao.admin.AdminSupportDAO";

    @Override
    public List<SupportVO> selectAllSupports() {
        return sqlSession.selectList(NAMESPACE + ".selectAllSupports");
    }

    @Override
    public List<Map<String, Object>> selectWeeklyStats() {
        return sqlSession.selectList(NAMESPACE + ".selectWeeklyStats");
    }

    @Override
    public SupportVO selectSupportByNo(int supportNo) {
        return sqlSession.selectOne(NAMESPACE + ".selectSupportByNo", supportNo);
    }

    @Override
    public int insertAnswer(SupportAnswerVO answer) {
        return sqlSession.insert(NAMESPACE + ".insertAnswer", answer);
    }

    @Override
    public int updateSupportStatus(int supportNo) {
        return sqlSession.update(NAMESPACE + ".updateSupportStatus", supportNo);
    }

    @Override
    public SupportAnswerVO selectAnswerBySupportNo(int supportNo) {
        return sqlSession.selectOne(NAMESPACE + ".selectAnswerBySupportNo", supportNo);
    }

    @Override
    public List<SupportFileVO> selectFilesBySupportNo(int supportNo) {
        return sqlSession.selectList(NAMESPACE + ".selectFilesBySupportNo", supportNo);
    }

    @Override
    public void deleteFaq(int faqId) throws SQLException {
        sqlSession.delete(NAMESPACE + ".deleteFaq", faqId);
    }

	@Override
	public int selectPendingSupportCount() {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(NAMESPACE + ".selectPendingSupportCount");
	}
}
