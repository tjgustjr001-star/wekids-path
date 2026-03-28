package com.spring.service;

import com.spring.dto.FaqVO;

import java.sql.SQLException;
import java.util.List;

public interface FaqService {

    /** 전체 FAQ 목록 조회 */
    List<FaqVO> getFaqList() throws SQLException;

    /** 카테고리별 FAQ 목록 조회 */
    List<FaqVO> getFaqListByCategory(String category) throws SQLException;

    /** 카테고리 목록 조회 */
    List<String> getCategoryList() throws SQLException;
}