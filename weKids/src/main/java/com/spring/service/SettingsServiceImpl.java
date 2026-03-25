package com.spring.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.SettingsDAO;
import com.spring.dto.ChildLinkVO;
import com.spring.dto.ParentChildVO;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final SettingsDAO settingsDAO;

    @Autowired
    public SettingsServiceImpl(SettingsDAO settingsDAO) {
        this.settingsDAO = settingsDAO;
    }

    @Override
    public ChildLinkVO getStudentLinkInfo(int memberId) throws SQLException {
        return settingsDAO.selectStudentLinkInfoByMemberId(memberId);
    }

    @Override
    public String generateParentLinkCode(int memberId) throws SQLException {
        ChildLinkVO info = settingsDAO.selectStudentLinkInfoByMemberId(memberId);

        if (info == null) {
            throw new SQLException("학생 정보를 찾을 수 없습니다.");
        }

        String code = createRandomCode(8);
        settingsDAO.updateStudentLinkCode(info.getStudentId(), code);

        return code;
    }

    @Override
    public boolean connectStudentToParent(int parentMemberId, String linkCode) throws SQLException {
        ChildLinkVO studentInfo = settingsDAO.selectStudentByLinkCode(linkCode);

        if (studentInfo == null) {
            return false;
        }

        int exists = settingsDAO.countActiveLink(studentInfo.getStudentId(), parentMemberId);
        if (exists > 0) {
            return false;
        }

        settingsDAO.insertStudentParentLink(studentInfo.getStudentId(), parentMemberId, "PARENT");
        return true;
    }

    @Override
    public List<ParentChildVO> getLinkedChildren(int parentMemberId) throws SQLException {
        return settingsDAO.selectLinkedChildrenByParentMemberId(parentMemberId);
    }

    @Override
    public ParentChildVO getChildDetail(int parentId, int studentId) throws SQLException {
        return settingsDAO.selectChildDetail(parentId, studentId);
    }

    @Override
    public boolean removeChildLink(int parentId, int studentId) throws SQLException {
        int result = settingsDAO.updateUnlinkChild(parentId, studentId);
        return result > 0;
    }

    private String createRandomCode(int length) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}