package com.spring.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.SupportVO;
import com.spring.dto.SupportAnswerVO;

public interface SupportDAO {

    void insertSupport(SupportVO support) throws SQLException;

    List<SupportVO> selectSupportListByWriterId(@Param("writerId") int writerId) throws SQLException;

    SupportVO selectSupportById(@Param("supportId") int supportId) throws SQLException;

    void updateSupportStatus(SupportVO support) throws SQLException;

    void deleteSupport(@Param("supportId") int supportId) throws SQLException;

    SupportAnswerVO selectAnswerBySupportNo(@Param("supportNo") int supportNo) throws SQLException;
    
    
}