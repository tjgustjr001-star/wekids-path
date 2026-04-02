package com.spring.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.SupportDAO;
import com.spring.dto.SupportAnswerVO;
import com.spring.dto.SupportFileVO;
import com.spring.dto.SupportVO;

@Service
public class SupportServiceImpl implements SupportService {

    @Autowired
    private SupportDAO supportDAO;

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

    @Override
    public void saveFile(SupportFileVO file) throws SQLException {
        supportDAO.insertSupportFile(file);
    }

    @Override
    public List<SupportFileVO> getFilesBySupportNo(int supportNo) throws SQLException {
        return supportDAO.selectFilesBySupportNo(supportNo);
    }
}
