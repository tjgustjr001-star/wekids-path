package com.spring.service;

import java.sql.SQLException;
import java.util.List;

import com.spring.dto.SupportAnswerVO;
import com.spring.dto.SupportVO;

public interface SupportService {

    // 문의 작성
    void write(SupportVO support) throws SQLException;

    // 내 문의 목록
    List<SupportVO> getSupportList(int writerId) throws SQLException;

    // 문의 상세 조회
    SupportVO getSupportById(int supportId) throws SQLException;

    // 답변 조회
    SupportAnswerVO getAnswerBySupportNo(int supportNo) throws SQLException;

    // 문의 삭제
    void removeSupportById(int supportId) throws SQLException;
}