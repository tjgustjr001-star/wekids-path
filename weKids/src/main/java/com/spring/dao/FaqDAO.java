package com.spring.dao;

import com.spring.dto.FaqVO;

import java.sql.SQLException;
import java.util.List;

public interface FaqDAO {

    /** 전체 FAQ 목록 조회 */
    List<FaqVO> selectFaqList() throws SQLException;

    /** 카테고리별 FAQ 목록 조회 */
    List<FaqVO> selectFaqListByCategory(String category) throws SQLException;

    /** 카테고리 목록 조회 (중복 제거) */
    List<String> selectCategoryList() throws SQLException;
}