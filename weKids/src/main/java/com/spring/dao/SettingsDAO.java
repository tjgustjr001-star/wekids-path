package com.spring.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.ChildLinkVO;
import com.spring.dto.MemberVO;
import com.spring.dto.ParentChildVO;

public interface SettingsDAO {

    ChildLinkVO selectStudentLinkInfoByMemberId(int memberId) throws SQLException;

    void updateStudentLinkCode(@Param("studentId") int studentId,
                               @Param("parentLinkCode") String parentLinkCode) throws SQLException;

    ChildLinkVO selectStudentByLinkCode(String parentLinkCode) throws SQLException;

    int countActiveLink(@Param("studentId") int studentId,
                        @Param("parentId") int parentId) throws SQLException;

    Integer selectLinkStatus(@Param("studentId") int studentId,
                             @Param("parentId") int parentId) throws SQLException;

    void insertStudentParentLink(@Param("studentId") int studentId,
                                 @Param("parentId") int parentId,
                                 @Param("relationType") String relationType) throws SQLException;

    int reactivateChildLink(@Param("parentId") int parentId,
                            @Param("studentId") int studentId) throws SQLException;

    List<ParentChildVO> selectLinkedChildrenByParentMemberId(@Param("parentMemberId") int parentMemberId) throws SQLException;

    List<ParentChildVO> selectLinkedParentsByStudentId(@Param("studentId") int studentId) throws SQLException;

    ParentChildVO selectChildDetail(@Param("parentId") int parentId,
                                    @Param("studentId") int studentId) throws SQLException;

    int updateUnlinkChild(@Param("parentId") int parentId,
                          @Param("studentId") int studentId) throws SQLException;

    // =========================
    // 내 프로필
    // =========================
    MemberVO selectStudentProfile(int memberId) throws SQLException;
    MemberVO selectParentProfile(int memberId) throws SQLException;
    MemberVO selectTeacherProfile(int memberId) throws SQLException;

    int updateStudentProfile(MemberVO member) throws SQLException;
    int updateParentProfile(MemberVO member) throws SQLException;
    int updateTeacherProfile(MemberVO member) throws SQLException;

    // =========================
    // 계정정보
    // =========================
    MemberVO selectStudentAccountInfo(int memberId) throws SQLException;
    MemberVO selectParentAccountInfo(int memberId) throws SQLException;
    MemberVO selectTeacherAccountInfo(int memberId) throws SQLException;

    int updateStudentAccountInfo(MemberVO member) throws SQLException;
    int updateParentAccountInfo(MemberVO member) throws SQLException;
    int updateTeacherAccountInfo(MemberVO member) throws SQLException;

    int updateMemberEmail(MemberVO member) throws SQLException;

    int updateMemberPassword(@Param("memberId") int memberId,
                             @Param("pwd") String pwd) throws SQLException;

    int updateMemberAccountStatusDeleted(@Param("memberId") int memberId) throws SQLException;
}