package com.spring.dao;

import java.sql.SQLException;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.spring.dao.FaqDAO;
import com.spring.dto.FaqVO;

public class FaqDAOImpl implements FaqDAO {

    private SqlSessionTemplate sqlSession;

    public FaqDAOImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<FaqVO> selectFaqList() throws SQLException {
        return sqlSession.selectList("faq.selectFaqList");
    }

    @Override
    public List<FaqVO> selectFaqListByCategory(String category) throws SQLException {
        return sqlSession.selectList("faq.selectFaqListByCategory", category);
    }

    @Override
    public List<String> selectCategoryList() throws SQLException {
        return sqlSession.selectList("faq.selectCategoryList");
    }
}