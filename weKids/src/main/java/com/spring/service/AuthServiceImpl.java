
package com.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.cmd.ParentRegisterCommand;
import com.spring.dao.AuthorityDAO;
import com.spring.dao.MemberDAO;
import com.spring.dto.AuthorityVO;
import com.spring.dto.MemberVO;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private AuthorityDAO authorityDAO;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public MemberVO login(String login_id, String pwd) throws Exception {
        MemberVO member = memberDAO.selectMemberByLoginId(login_id);

        if (member == null) {
            return null;
        }

        if (!"ACTIVE".equals(member.getAccount_status())) {
            return null;
        }

        if (!member.getPwd().equals(pwd)) {
            return null;
        }

        return member;
    }

    @Override
    public boolean isLoginIdDuplicated(String loginId) throws Exception {
        if (loginId == null || loginId.trim().isEmpty()) {
            return true;
        }
        return memberDAO.selectMemberByLoginId(loginId.trim()) != null;
    }

    @Override
    public boolean isValidParentLinkCode(String parentLinkCode) throws Exception {
        if (parentLinkCode == null || parentLinkCode.trim().isEmpty()) {
            return false;
        }
        return settingsService.getStudentLinkInfoByCode(parentLinkCode.trim()) != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int registerParent(ParentRegisterCommand regCommand) throws Exception {
        String loginId = regCommand.getLogin_id() == null ? "" : regCommand.getLogin_id().trim();
        String pwd = regCommand.getPwd() == null ? "" : regCommand.getPwd();
        String pwdConfirm = regCommand.getPwd_confirm() == null ? "" : regCommand.getPwd_confirm();
        String parentName = regCommand.getParent_name() == null ? "" : regCommand.getParent_name().trim();
        String phone = regCommand.getPhone() == null ? "" : regCommand.getPhone().trim();
        String email = regCommand.getEmail() == null ? "" : regCommand.getEmail().trim();
        String linkCode = regCommand.getParent_link_code() == null ? "" : regCommand.getParent_link_code().trim();

        if (loginId.isEmpty() || pwd.isEmpty() || parentName.isEmpty() || phone.isEmpty() || linkCode.isEmpty()) {
            throw new IllegalArgumentException("필수 항목을 모두 입력해주세요.");
        }
        if (!pwd.equals(pwdConfirm)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (isLoginIdDuplicated(loginId)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        if (!isValidParentLinkCode(linkCode)) {
            throw new IllegalArgumentException("유효하지 않은 보호자 연결 코드입니다.");
        }

        int memberId = memberDAO.selectNextMemberId();

        MemberVO member = new MemberVO();
        member.setMember_id(memberId);
        member.setLogin_id(loginId);
        member.setPwd(passwordEncoder.encode(pwd));
        member.setEmail(email);
        member.setPhone(phone);
        member.setName(parentName);
        member.setRole_code("ROLE_PARENT");
        member.setRole_name("학부모");
        member.setAccount_status("ACTIVE");

        memberDAO.insertMember(member);
        memberDAO.insertParent(member);

        AuthorityVO authority = new AuthorityVO();
        authority.setAuthorityId(authorityDAO.selectNextAuthorityId());
        authority.setMemberId(memberId);
        authority.setRoleCode("ROLE_PARENT");
        authority.setRoleName("학부모");
        authorityDAO.insertAuthority(authority);

        boolean linked = settingsService.connectStudentToParent(memberId, linkCode);
        if (!linked) {
            throw new IllegalStateException("학생 연결에 실패했습니다.");
        }

        return memberId;
    }
}
