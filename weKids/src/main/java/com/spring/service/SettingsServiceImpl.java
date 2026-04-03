
package com.spring.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import com.spring.dao.ReportAggregateDAO;
import com.spring.dto.ClassVO;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.dao.SettingsDAO;
import com.spring.dto.ChildLinkVO;
import com.spring.dto.MemberVO;
import com.spring.dto.ParentChildVO;

@Service
public class SettingsServiceImpl implements SettingsService {

    private static final String AGG_START_DATE = "2000-01-01";
    private static final String AGG_END_DATE = "2099-12-31";

    private final SettingsDAO settingsDAO;
    private final PasswordEncoder passwordEncoder;
    private final ReportAggregateDAO reportAggregateDAO;

    public SettingsServiceImpl(SettingsDAO settingsDAO, PasswordEncoder passwordEncoder, ReportAggregateDAO reportAggregateDAO) {
        this.settingsDAO = settingsDAO;
        this.passwordEncoder = passwordEncoder;
        this.reportAggregateDAO = reportAggregateDAO;
    }

    @Override
    public ChildLinkVO getStudentLinkInfo(int memberId) throws SQLException {
        return settingsDAO.selectStudentLinkInfoByMemberId(memberId);
    }

    @Override
    public ChildLinkVO getStudentLinkInfoByCode(String linkCode) throws SQLException {
        if (linkCode == null || linkCode.trim().isEmpty()) {
            return null;
        }
        return settingsDAO.selectStudentByLinkCode(linkCode.trim());
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
        if (linkCode == null || linkCode.trim().isEmpty()) {
            return false;
        }

        ChildLinkVO studentInfo = settingsDAO.selectStudentByLinkCode(linkCode.trim());

        if (studentInfo == null) {
            return false;
        }

        Integer linkStatus = settingsDAO.selectLinkStatus(studentInfo.getStudentId(), parentMemberId);

        if (linkStatus == null) {
            settingsDAO.insertStudentParentLink(studentInfo.getStudentId(), parentMemberId, "PARENT");
            return true;
        }

        if (linkStatus == 1) {
            return false;
        }

        int result = settingsDAO.reactivateChildLink(parentMemberId, studentInfo.getStudentId());
        return result > 0;
    }

    @Override
    public List<ParentChildVO> getLinkedChildren(int parentMemberId) throws SQLException {
        List<ParentChildVO> childList = settingsDAO.selectLinkedChildrenByParentMemberId(parentMemberId);
        if (childList == null || childList.isEmpty()) {
            return childList;
        }

        for (ParentChildVO child : childList) {
            if (child == null) {
                continue;
            }

            List<ClassVO> classList = settingsDAO.selectChildClassList(parentMemberId, child.getStudentId());
            applyAggregateProgress(child, classList);

            if (classList != null && !classList.isEmpty()) {
                ClassVO firstClass = classList.get(0);
                child.setYear(firstClass.getYear());
                child.setGrade(firstClass.getGrade());
                child.setClassNo(firstClass.getClassNo());
                child.setClassName(firstClass.getClassName());
            }

            if (child.getTeacherComment() == null || child.getTeacherComment().trim().isEmpty()) {
                child.setTeacherComment("선생님 코멘트 없음");
            }
        }

        return childList;
    }

    @Override
    public List<ParentChildVO> getLinkedParents(int studentId) throws SQLException {
        return settingsDAO.selectLinkedParentsByStudentId(studentId);
    }

    @Override
    public ParentChildVO getChildDetail(int parentId, int studentId, Integer classId) throws SQLException {
        Integer selectedClassId = classId;
        List<ClassVO> classList = settingsDAO.selectChildClassList(parentId, studentId);

        if ((selectedClassId == null || selectedClassId.intValue() <= 0) && classList != null && !classList.isEmpty()) {
            selectedClassId = classList.get(0).getClassId();
        }

        ParentChildVO child = settingsDAO.selectChildDetail(parentId, studentId, selectedClassId);
        if (child == null) {
            return null;
        }

        if (classList != null && !classList.isEmpty()) {
            ClassVO matchedClass = null;
            if (selectedClassId != null) {
                for (ClassVO classVO : classList) {
                    if (classVO != null && classVO.getClassId() == selectedClassId.intValue()) {
                        matchedClass = classVO;
                        break;
                    }
                }
            }
            if (matchedClass == null) {
                matchedClass = classList.get(0);
            }

            child.setYear(matchedClass.getYear());
            child.setGrade(matchedClass.getGrade());
            child.setClassNo(matchedClass.getClassNo());
            child.setClassName(matchedClass.getClassName());
            applySingleClassProgress(child, matchedClass.getClassId());
        }

        if (child.getTeacherComment() == null || child.getTeacherComment().trim().isEmpty()) {
            child.setTeacherComment("선생님 코멘트 없음");
        }

        return child;
    }

    @Override
    public List<ClassVO> getChildClassList(int parentId, int studentId) throws SQLException {
        return settingsDAO.selectChildClassList(parentId, studentId);
    }

    @Override
    public boolean removeChildLink(int parentId, int studentId) throws SQLException {
        int result = settingsDAO.updateUnlinkChild(parentId, studentId);
        return result > 0;
    }

    @Override
    public MemberVO getMyProfile(int memberId, String roleCode) throws SQLException {

        if ("STUDENT".equalsIgnoreCase(roleCode) || "ROLE_STUDENT".equalsIgnoreCase(roleCode)) {
            return settingsDAO.selectStudentProfile(memberId);
        }

        if ("PARENT".equalsIgnoreCase(roleCode) || "ROLE_PARENT".equalsIgnoreCase(roleCode)) {
            return settingsDAO.selectParentProfile(memberId);
        }

        if ("TEACHER".equalsIgnoreCase(roleCode) || "ROLE_TEACHER".equalsIgnoreCase(roleCode)) {
            return settingsDAO.selectTeacherProfile(memberId);
        }

        throw new SQLException("지원하지 않는 권한입니다. roleCode = " + roleCode);
    }

    @Override
    public void modifyMyProfile(MemberVO member) throws SQLException {

        if ("STUDENT".equalsIgnoreCase(member.getRole_code()) || "ROLE_STUDENT".equalsIgnoreCase(member.getRole_code())) {
            settingsDAO.updateStudentProfile(member);
            return;
        }

        if ("PARENT".equalsIgnoreCase(member.getRole_code()) || "ROLE_PARENT".equalsIgnoreCase(member.getRole_code())) {
            settingsDAO.updateParentProfile(member);
            return;
        }

        if ("TEACHER".equalsIgnoreCase(member.getRole_code()) || "ROLE_TEACHER".equalsIgnoreCase(member.getRole_code())) {
            settingsDAO.updateTeacherProfile(member);
            return;
        }

        throw new SQLException("지원하지 않는 권한입니다. roleCode = " + member.getRole_code());
    }

    @Override
    public MemberVO getMyAccountInfo(int memberId, String roleCode) throws SQLException {

        if ("STUDENT".equalsIgnoreCase(roleCode) || "ROLE_STUDENT".equalsIgnoreCase(roleCode)) {
            return settingsDAO.selectStudentAccountInfo(memberId);
        }

        if ("PARENT".equalsIgnoreCase(roleCode) || "ROLE_PARENT".equalsIgnoreCase(roleCode)) {
            return settingsDAO.selectParentAccountInfo(memberId);
        }

        if ("TEACHER".equalsIgnoreCase(roleCode) || "ROLE_TEACHER".equalsIgnoreCase(roleCode)) {
            return settingsDAO.selectTeacherAccountInfo(memberId);
        }

        throw new SQLException("지원하지 않는 권한입니다. roleCode = " + roleCode);
    }

    @Override
    public void modifyMyAccountInfo(MemberVO member) throws SQLException {

        settingsDAO.updateMemberEmail(member);

        if ("STUDENT".equalsIgnoreCase(member.getRole_code()) || "ROLE_STUDENT".equalsIgnoreCase(member.getRole_code())) {
            settingsDAO.updateStudentAccountInfo(member);
            return;
        }

        if ("PARENT".equalsIgnoreCase(member.getRole_code()) || "ROLE_PARENT".equalsIgnoreCase(member.getRole_code())) {
            settingsDAO.updateParentAccountInfo(member);
            return;
        }

        if ("TEACHER".equalsIgnoreCase(member.getRole_code()) || "ROLE_TEACHER".equalsIgnoreCase(member.getRole_code())) {
            settingsDAO.updateTeacherAccountInfo(member);
            return;
        }

        throw new SQLException("지원하지 않는 권한입니다. roleCode = " + member.getRole_code());
    }

    @Override
    public boolean changeMyPassword(int memberId, String roleCode, String currentPwd, String newPwd) throws SQLException {
        MemberVO account = getMyAccountInfo(memberId, roleCode);

        if (account == null || account.getPwd() == null) {
            throw new SQLException("계정 정보를 찾을 수 없습니다.");
        }

        String savedPwd = account.getPwd();

        boolean matched;
        if (isEncodedPassword(savedPwd)) {
            matched = passwordEncoder.matches(currentPwd, savedPwd);
        } else {
            matched = savedPwd.equals(currentPwd);
        }

        if (!matched) {
            return false;
        }

        String encodedPwd = passwordEncoder.encode(newPwd);
        settingsDAO.updateMemberPassword(memberId, encodedPwd);
        return true;
    }

    @Override
    public void deleteMyAccount(int memberId) throws SQLException {
        int updated = settingsDAO.updateMemberAccountStatusDeleted(memberId);
        if (updated < 1) {
            throw new SQLException("계정 삭제 처리에 실패했습니다.");
        }
    }

    private boolean isEncodedPassword(String pwd) {
        if (pwd == null) {
            return false;
        }
        return pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$");
    }

    private void applyAggregateProgress(ParentChildVO child, List<ClassVO> classList) throws SQLException {
        int totalLearningCount = 0;
        int completedLearningCount = 0;
        int totalAssignmentCount = 0;
        int submittedAssignmentCount = 0;

        try {
            if (classList != null) {
                for (ClassVO classVO : classList) {
                    if (classVO == null || classVO.getClassId() <= 0) {
                        continue;
                    }

                    totalLearningCount += reportAggregateDAO.countTotalLearning(classVO.getClassId(), child.getStudentId(), AGG_START_DATE, AGG_END_DATE);
                    completedLearningCount += reportAggregateDAO.countCompletedLearning(classVO.getClassId(), child.getStudentId(), AGG_START_DATE, AGG_END_DATE);
                    totalAssignmentCount += reportAggregateDAO.countTotalAssignment(classVO.getClassId(), child.getStudentId(), AGG_START_DATE, AGG_END_DATE);
                    submittedAssignmentCount += reportAggregateDAO.countSubmittedAssignment(classVO.getClassId(), child.getStudentId(), AGG_START_DATE, AGG_END_DATE);
                }
            }
        } catch (Exception e) {
            throw new SQLException("자녀 학습/과제 집계 조회 중 오류가 발생했습니다.", e);
        }

        child.setTotalLearningCount(totalLearningCount);
        child.setCompletedLearningCount(completedLearningCount);
        child.setLearningProgressRate(calculateRate(completedLearningCount, totalLearningCount));
        child.setTotalAssignmentCount(totalAssignmentCount);
        child.setSubmittedAssignmentCount(submittedAssignmentCount);
        child.setAssignmentRate(calculateRate(submittedAssignmentCount, totalAssignmentCount));
    }

    private void applySingleClassProgress(ParentChildVO child, int classId) throws SQLException {
        try {
            int totalLearningCount = reportAggregateDAO.countTotalLearning(classId, child.getStudentId(), AGG_START_DATE, AGG_END_DATE);
            int completedLearningCount = reportAggregateDAO.countCompletedLearning(classId, child.getStudentId(), AGG_START_DATE, AGG_END_DATE);
            int totalAssignmentCount = reportAggregateDAO.countTotalAssignment(classId, child.getStudentId(), AGG_START_DATE, AGG_END_DATE);
            int submittedAssignmentCount = reportAggregateDAO.countSubmittedAssignment(classId, child.getStudentId(), AGG_START_DATE, AGG_END_DATE);

            child.setTotalLearningCount(totalLearningCount);
            child.setCompletedLearningCount(completedLearningCount);
            child.setLearningProgressRate(calculateRate(completedLearningCount, totalLearningCount));
            child.setTotalAssignmentCount(totalAssignmentCount);
            child.setSubmittedAssignmentCount(submittedAssignmentCount);
            child.setAssignmentRate(calculateRate(submittedAssignmentCount, totalAssignmentCount));
        } catch (Exception e) {
            throw new SQLException("자녀 클래스 상세 집계 조회 중 오류가 발생했습니다.", e);
        }
    }

    private int calculateRate(int completedCount, int totalCount) {
        if (totalCount <= 0) {
            return 0;
        }
        return Math.round((completedCount * 100.0f) / totalCount);
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