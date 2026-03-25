package com.spring.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.ChildLinkVO;
import com.spring.dto.ParentChildVO;

public interface SettingsDAO {

    ChildLinkVO selectStudentLinkInfoByMemberId(int memberId) throws SQLException;

    void updateStudentLinkCode(@Param("studentId") int studentId,
                               @Param("parentLinkCode") String parentLinkCode) throws SQLException;

    ChildLinkVO selectStudentByLinkCode(String parentLinkCode) throws SQLException;

    int countActiveLink(@Param("studentId") int studentId,
                        @Param("parentId") int parentId) throws SQLException;

    void insertStudentParentLink(@Param("studentId") int studentId,
                                 @Param("parentId") int parentId,
                                 @Param("relationType") String relationType) throws SQLException;

    List<ParentChildVO> selectLinkedChildrenByParentMemberId(int memberId) throws SQLException;

    ParentChildVO selectChildDetail(@Param("parentId") int parentId,
                                    @Param("studentId") int studentId) throws SQLException;

    int updateUnlinkChild(@Param("parentId") int parentId,
                          @Param("studentId") int studentId) throws SQLException;
}