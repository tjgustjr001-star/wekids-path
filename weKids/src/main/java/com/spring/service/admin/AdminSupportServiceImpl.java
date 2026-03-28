package com.spring.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.admin.AdminSupportDAO;
import com.spring.dto.SupportVO;

@Service
public class AdminSupportServiceImpl implements AdminSupportService {

    @Autowired
    private AdminSupportDAO adminSupportDAO;

    @Override
    public List<SupportVO> getSupportList() {
        return adminSupportDAO.selectAllSupports();
    }
}