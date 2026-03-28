package com.spring.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.spring.dto.SupportAnswerVO;
import com.spring.dto.SupportVO;

public class SupportDAOImpl implements SupportDAO {

    private SqlSession session;

    public SupportDAOImpl(SqlSession session) {
        this.session = session;
    }

    @Override
    public void insertSupport(SupportVO support) throws SQLException {
        session.insert("supportMapper.insertSupport", support);
    }

    @Override
    public List<SupportVO> selectSupportListByWriterId(int writerId) throws SQLException {
        return session.selectList("supportMapper.selectSupportListByWriterId", writerId);
    }

    @Override
    public SupportVO selectSupportById(int supportId) throws SQLException {
        return session.selectOne("supportMapper.selectSupportById", supportId);
    }

    @Override
    public void updateSupportStatus(SupportVO support) throws SQLException {
        session.update("supportMapper.updateSupportStatus", support);
    }

    @Override
    public void deleteSupport(int supportId) throws SQLException {
        session.delete("supportMapper.deleteSupport", supportId);
    }

    @Override
    public SupportAnswerVO selectAnswerBySupportNo(int supportNo) throws SQLException {
        return session.selectOne("supportMapper.selectAnswerBySupportNo", supportNo);
    }
}