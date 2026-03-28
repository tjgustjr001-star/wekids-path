package com.spring.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.dao.SupportDAO;
import com.spring.dto.SupportAnswerVO;
import com.spring.dto.SupportVO;

@Service
public class SupportServiceImpl implements SupportService {

    private SupportDAO supportDAO;

    public SupportServiceImpl(SupportDAO supportDAO) {
        this.supportDAO = supportDAO;
    }

    @Override
    public void write(SupportVO support) throws SQLException {
        supportDAO.insertSupport(support);
    }

    @Override
    public List<SupportVO> getSupportList(int writerId) throws SQLException {
        return supportDAO.selectSupportListByWriterId(writerId);
    }

    @Override
    public SupportVO getSupportById(int supportId) throws SQLException {
        return supportDAO.selectSupportById(supportId);
    }

    @Override
    public SupportAnswerVO getAnswerBySupportNo(int supportNo) throws SQLException {
        return supportDAO.selectAnswerBySupportNo(supportNo);
    }

    @Override
    public void removeSupportById(int supportId) throws SQLException {
        supportDAO.deleteSupport(supportId);
    }
}