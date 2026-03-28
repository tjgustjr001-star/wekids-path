
package com.spring.service;

import com.spring.cmd.ParentRegisterCommand;
import com.spring.dto.MemberVO;

public interface AuthService {

    MemberVO login(String login_id, String pwd) throws Exception;

    boolean isLoginIdDuplicated(String loginId) throws Exception;

    boolean isValidParentLinkCode(String parentLinkCode) throws Exception;

    int registerParent(ParentRegisterCommand regCommand) throws Exception;
}
