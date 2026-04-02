package com.spring.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.spring.dto.SupportAnswerVO;
import com.spring.dto.SupportFileVO;
import com.spring.dto.SupportVO;

public class SupportDAOImpl implements SupportDAO {

    private SqlSession session;

    public void setSession(SqlSession session) {
        this.session = session;
    }

    @Override
    public void insertSupport(SupportVO support) throws SQLException {
        session.insert("com.spring.dao.SupportDAO.insertSupport", support);
    }

    @Override
    public List<SupportVO> selectSupportListByWriterId(int writerId) throws SQLException {
        return session.selectList("com.spring.dao.SupportDAO.selectSupportListByWriterId", writerId);
    }

    @Override
    public SupportVO selectSupportById(int supportId) throws SQLException {
        return session.selectOne("com.spring.dao.SupportDAO.selectSupportById", supportId);
    }

    @Override
    public void updateSupportStatus(SupportVO support) throws SQLException {
        session.update("com.spring.dao.SupportDAO.updateSupportStatus", support);
    }

    @Override
    public void deleteSupport(int supportId) throws SQLException {
        session.update("com.spring.dao.SupportDAO.deleteSupport", supportId);
    }

    @Override
    public SupportAnswerVO selectAnswerBySupportNo(int supportNo) throws SQLException {
        return session.selectOne("com.spring.dao.SupportDAO.selectAnswerBySupportNo", supportNo);
    }

    @Override
    public void insertSupportFile(SupportFileVO file) throws SQLException {
        session.insert("com.spring.dao.SupportDAO.insertSupportFile", file);
    }

    @Override
    public List<SupportFileVO> selectFilesBySupportNo(int supportNo) throws SQLException {
        return session.selectList("com.spring.dao.SupportDAO.selectFilesBySupportNo", supportNo);
    }
}
