
package com.spring.service;

import java.sql.SQLException;
import java.util.List;

import com.spring.dto.ChildLinkVO;
import com.spring.dto.MemberVO;
import com.spring.dto.ParentChildVO;

public interface SettingsService {

    ChildLinkVO getStudentLinkInfo(int memberId) throws SQLException;

    String generateParentLinkCode(int memberId) throws SQLException;

    boolean connectStudentToParent(int parentMemberId, String linkCode) throws SQLException;

    List<ParentChildVO> getLinkedChildren(int parentMemberId) throws SQLException;

    List<ParentChildVO> getLinkedParents(int studentId) throws SQLException;

    ParentChildVO getChildDetail(int parentId, int studentId) throws SQLException;

    boolean removeChildLink(int parentId, int studentId) throws SQLException;

    MemberVO getMyProfile(int memberId, String roleCode) throws SQLException;

    void modifyMyProfile(MemberVO member) throws SQLException;

    MemberVO getMyAccountInfo(int memberId, String roleCode) throws SQLException;

    void modifyMyAccountInfo(MemberVO member) throws SQLException;

    boolean changeMyPassword(int memberId, String roleCode, String currentPwd, String newPwd) throws SQLException;

    void deleteMyAccount(int memberId) throws SQLException;

}