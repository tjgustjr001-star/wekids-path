package com.spring.dao.admin;

import java.util.List;

import com.spring.dto.SupportVO;

public interface AdminSupportDAO {
    List<SupportVO> selectAllSupports();
}