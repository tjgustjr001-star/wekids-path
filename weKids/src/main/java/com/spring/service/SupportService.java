package com.spring.service;

import java.sql.SQLException;
import java.util.List;

import com.spring.dto.SupportAnswerVO;
import com.spring.dto.SupportFileVO;
import com.spring.dto.SupportVO;

public interface SupportService {

    void write(SupportVO support) throws SQLException;

    List<SupportVO> getSupportList(int writerId) throws SQLException;

    SupportVO getSupportById(int supportId) throws SQLException;

    SupportAnswerVO getAnswerBySupportNo(int supportNo) throws SQLException;

    void removeSupportById(int supportId) throws SQLException;

    void saveFile(SupportFileVO file) throws SQLException;

    List<SupportFileVO> getFilesBySupportNo(int supportNo) throws SQLException;
}
