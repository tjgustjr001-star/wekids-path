package com.spring.dao.admin;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.admin.AdminStudentRegistDTO;
import com.spring.dto.admin.AdminUserDetailDTO;
import com.spring.dto.admin.AdminUserListDTO;
import com.spring.dto.admin.WeeklyLoginTrendDTO;

public interface AdminUserDAO {

    List<AdminUserListDTO> selectUserList() throws SQLException;

    AdminUserDetailDTO selectUserDetailById(@Param("memberId") int memberId) throws SQLException;

    void updateUserStatus(@Param("memberId") int memberId,
                          @Param("accountStatus") String accountStatus) throws SQLException;
    
    int selectNextMemberId() throws Exception;

    int selectNextAuthorityId() throws Exception;

    void insertStudentMember(@Param("memberId") int memberId,
                             @Param("registDTO") AdminStudentRegistDTO registDTO) throws Exception;

    void insertStudentAuthority(@Param("authorityId") int authorityId,
                                @Param("memberId") int memberId) throws Exception;

    void insertStudent(@Param("memberId") int memberId,
                       @Param("registDTO") AdminStudentRegistDTO registDTO) throws Exception;
    
    int selectTotalUserCount() throws SQLException;
    
    List<WeeklyLoginTrendDTO> selectWeeklyLoginTrend() throws SQLException;
    
    List<WeeklyLoginTrendDTO> selectUserWeeklyLoginTrend(@Param("memberId") int memberId) throws SQLException;	
}