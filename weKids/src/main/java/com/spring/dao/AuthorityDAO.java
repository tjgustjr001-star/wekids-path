package com.spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.spring.dto.AuthorityVO;

@Mapper
public interface AuthorityDAO {
    List<AuthorityVO> selectAuthoritiesByMemberId(@Param("memberId") int memberId) throws Exception;
}