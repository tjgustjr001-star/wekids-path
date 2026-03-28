package com.spring.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.dao.FaqDAO;
import com.spring.dto.FaqVO;

@Service
public class FaqServiceImpl implements FaqService {

    private FaqDAO faqDAO;

    public FaqServiceImpl(FaqDAO faqDAO) {
        this.faqDAO = faqDAO;
    }

    @Override
    public List<FaqVO> getFaqList() throws SQLException {
        return faqDAO.selectFaqList();
    }

    @Override
    public List<FaqVO> getFaqListByCategory(String category) throws SQLException {
        return faqDAO.selectFaqListByCategory(category);
    }

    @Override
    public List<String> getCategoryList() throws SQLException {
        return faqDAO.selectCategoryList();
    }
}