
package com.spring.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.dto.ChildLinkVO;
import com.spring.dto.MemberVO;
import com.spring.dto.ParentChildVO;
import com.spring.security.CustomUser;
import com.spring.service.SettingsService;

@Controller
@RequestMapping({"/settings", "/student/settings", "/parent/settings", "/teacher/settings"})
public class SettingsController {

    private final SettingsService settingsService;
    private final ServletContext servletContext;

    public SettingsController(SettingsService settingsService, ServletContext servletContext) {
        this.settingsService = settingsService;
        this.servletContext = servletContext;
    }

    @GetMapping("")
    public String settingsMain(Authentication authentication, Model model) throws SQLException {

        CustomUser user = (CustomUser) authentication.getPrincipal();
        MemberVO loginUser = user.getMember();

        int memberId = loginUser.getMember_id();
        String roleCode = normalizeRoleCode(loginUser.getRole_code());

        model.addAttribute("role", roleCode);
        model.addAttribute("baseSettingsPath", getBaseSettingsPath(roleCode));
        model.addAttribute("contentPage", "/WEB-INF/views/settings/settings.jsp");
        model.addAttribute("pageTitle", "설정");

        MemberVO profile = settingsService.getMyProfile(memberId, roleCode);
        model.addAttribute("member", profile);
        model.addAttribute("profile", profile);

        if ("STUDENT".equalsIgnoreCase(roleCode)) {
            ChildLinkVO linkInfo = settingsService.getStudentLinkInfo(memberId);
            List<ParentChildVO> parentList = settingsService.getLinkedParents(memberId);
            model.addAttribute("linkInfo", linkInfo);
            model.addAttribute("parentList", parentList);
        }

        if ("PARENT".equalsIgnoreCase(roleCode)) {
            List<ParentChildVO> childList = settingsService.getLinkedChildren(memberId);
            model.addAttribute("childList", childList);
        }

        return resolveLayout(roleCode);
    }

    @GetMapping("/profile")
    public String myProfile(Authentication authentication, Model model) throws SQLException {

        CustomUser user = (CustomUser) authentication.getPrincipal();
        MemberVO loginUser = user.getMember();

        int memberId = loginUser.getMember_id();
        String roleCode = normalizeRoleCode(loginUser.getRole_code());

        MemberVO profile = settingsService.getMyProfile(memberId, roleCode);

        model.addAttribute("role", roleCode);
        model.addAttribute("member", profile);
        model.addAttribute("profile", profile);
        model.addAttribute("baseSettingsPath", getBaseSettingsPath(roleCode));

        return "settings/profile";
    }

    @PostMapping(value = "/profile/modify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> modifyProfileAjax(
            Authentication authentication,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "birth", required = false) String birth,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "intro", required = false) String intro,
            @RequestParam(value = "profileFile", required = false) MultipartFile profileFile,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomUser)) {
                result.put("success", false);
                result.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(result);
            }

            CustomUser user = (CustomUser) authentication.getPrincipal();
            MemberVO loginUser = user.getMember();

            String roleCode = normalizeRoleCode(loginUser.getRole_code());
            MemberVO currentProfile = settingsService.getMyProfile(loginUser.getMember_id(), roleCode);

            String safeName = name == null ? "" : name.trim();
            String safeIntro = intro == null ? "" : intro.trim();
            String safeGender = gender == null ? "" : gender.trim();
            String safeBirth = birth == null ? "" : birth.trim();

            if (safeName.isEmpty()) {
                result.put("success", false);
                result.put("message", "이름을 입력해주세요.");
                return ResponseEntity.badRequest().body(result);
            }

            if (safeName.length() > 20) {
                result.put("success", false);
                result.put("message", "이름은 20자 이하로 입력해주세요.");
                return ResponseEntity.badRequest().body(result);
            }

            if (safeIntro.length() > 200) {
                result.put("success", false);
                result.put("message", "자기소개는 200자 이하로 입력해주세요.");
                return ResponseEntity.badRequest().body(result);
            }

            MemberVO member = new MemberVO();
            member.setMember_id(loginUser.getMember_id());
            member.setRole_code(roleCode);
            member.setName(safeName);
            member.setIntro(safeIntro);

            if ("M".equalsIgnoreCase(safeGender) || "F".equalsIgnoreCase(safeGender)) {
                member.setGender(safeGender.toUpperCase());
            } else if (currentProfile != null) {
                member.setGender(currentProfile.getGender());
            }

            if (!safeBirth.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    member.setBirth(sdf.parse(safeBirth));
                } catch (Exception e) {
                    result.put("success", false);
                    result.put("message", "생년월일 형식이 올바르지 않습니다.");
                    return ResponseEntity.badRequest().body(result);
                }
            } else if (currentProfile != null) {
                member.setBirth(currentProfile.getBirth());
            }

            if (currentProfile != null) {
                member.setProfile_image(currentProfile.getProfile_image());
            }

            if (profileFile != null && !profileFile.isEmpty()) {
                String savedFileName = saveProfileImage(profileFile);
                member.setProfile_image(savedFileName);
            }

            settingsService.modifyMyProfile(member);

            MemberVO savedProfile = settingsService.getMyProfile(loginUser.getMember_id(), roleCode);
            MemberVO refreshedUser = mergeMemberForSession(loginUser, savedProfile, roleCode);

            if (refreshedUser.getAuthorities() == null) {
                refreshedUser.setAuthorities(new ArrayList<>());
            }

            user.setMember(refreshedUser);
            session.setAttribute("loginUser", refreshedUser);

            result.put("success", true);
            result.put("message", "프로필이 성공적으로 저장되었습니다.");
            result.put("member", toResponseMember(savedProfile));

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "서버 처리 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @GetMapping("/child-link")
    public String childLinkPage(Authentication authentication, Model model) throws SQLException {

        CustomUser user = (CustomUser) authentication.getPrincipal();
        MemberVO loginUser = user.getMember();
        String roleCode = normalizeRoleCode(loginUser.getRole_code());

        model.addAttribute("role", roleCode);
        model.addAttribute("baseSettingsPath", getBaseSettingsPath(roleCode));

        if ("STUDENT".equalsIgnoreCase(roleCode)) {
            ChildLinkVO linkInfo = settingsService.getStudentLinkInfo(loginUser.getMember_id());
            List<ParentChildVO> parentList = settingsService.getLinkedParents(loginUser.getMember_id());
            model.addAttribute("linkInfo", linkInfo);
            model.addAttribute("parentList", parentList);
            return "settings/childLinkStudent";
        }

        if ("PARENT".equalsIgnoreCase(roleCode)) {
            return "redirect:/parent/children";
        }

        return "redirect:" + getBaseSettingsPath(roleCode);
    }

    @PostMapping(value = "/child-link/generate", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<String> generateLinkCode(Authentication authentication) throws SQLException {

        CustomUser user = (CustomUser) authentication.getPrincipal();
        MemberVO loginUser = user.getMember();
        String roleCode = normalizeRoleCode(loginUser.getRole_code());

        if (!"STUDENT".equalsIgnoreCase(roleCode)) {
            return ResponseEntity.badRequest().body("only_student");
        }

        String linkCode = settingsService.generateParentLinkCode(loginUser.getMember_id());
        return ResponseEntity.ok(linkCode);
    }

    @PostMapping("/child-link/connect")
    public String connectChild(Authentication authentication,
                               @RequestParam("linkCode") String linkCode,
                               RedirectAttributes rttr) throws SQLException {

        CustomUser user = (CustomUser) authentication.getPrincipal();
        MemberVO loginUser = user.getMember();
        String roleCode = normalizeRoleCode(loginUser.getRole_code());

        if (!"PARENT".equalsIgnoreCase(roleCode)) {
            rttr.addFlashAttribute("msg", "only_parent");
            return "redirect:/parent/children";
        }

        boolean success = settingsService.connectStudentToParent(loginUser.getMember_id(), linkCode);

        if (success) {
            rttr.addFlashAttribute("msg", "success");
        } else {
            rttr.addFlashAttribute("msg", "fail");
        }

        return "redirect:/parent/children";
    }

    @GetMapping("/link")
    public String oldLinkPage() {
        return "redirect:/settings/child-link";
    }

    @PostMapping(value = "/link/generate", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<String> oldGenerateLinkCode(Authentication authentication) throws SQLException {
        return generateLinkCode(authentication);
    }

    @PostMapping("/children/connect")
    public String oldConnectChild(Authentication authentication,
                                  @RequestParam("linkCode") String linkCode,
                                  RedirectAttributes rttr) throws SQLException {
        return connectChild(authentication, linkCode, rttr);
    }

    @PostMapping("/child-link/remove")
    public String removeChild(@RequestParam("studentId") int studentId,
                              Authentication authentication,
                              RedirectAttributes rttr) throws SQLException {

        CustomUser user = (CustomUser) authentication.getPrincipal();
        MemberVO loginUser = user.getMember();
        int parentId = loginUser.getMember_id();

        boolean result = settingsService.removeChildLink(parentId, studentId);

        if (result) {
            rttr.addFlashAttribute("msg", "removed");
        } else {
            rttr.addFlashAttribute("msg", "remove_fail");
        }

        return "redirect:/parent/settings/child-link";
    }

    @GetMapping("/info")
    public String accountInfo(Authentication authentication, Model model) throws SQLException {

        CustomUser user = (CustomUser) authentication.getPrincipal();
        MemberVO loginUser = user.getMember();

        int memberId = loginUser.getMember_id();
        String roleCode = normalizeRoleCode(loginUser.getRole_code());

        MemberVO account = settingsService.getMyAccountInfo(memberId, roleCode);

        model.addAttribute("member", account);
        model.addAttribute("role", roleCode);
        model.addAttribute("baseSettingsPath", getBaseSettingsPath(roleCode));

        return "settings/accountInfo";
    }

    @PostMapping(value = "/info/modify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> modifyAccountInfoAjax(
            Authentication authentication,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email) {

        Map<String, Object> result = new HashMap<>();

        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomUser)) {
                result.put("success", false);
                result.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(result);
            }

            CustomUser user = (CustomUser) authentication.getPrincipal();
            MemberVO loginUser = user.getMember();

            String roleCode = normalizeRoleCode(loginUser.getRole_code());

            String safePhone = phone == null ? "" : phone.trim();
            String safeEmail = email == null ? "" : email.trim();

            if (safePhone.isEmpty()) {
                result.put("success", false);
                result.put("message", "휴대폰 번호를 입력해주세요.");
                return ResponseEntity.badRequest().body(result);
            }

            if (!safePhone.matches("^[0-9-]{9,20}$")) {
                result.put("success", false);
                result.put("message", "휴대폰 번호 형식이 올바르지 않습니다.");
                return ResponseEntity.badRequest().body(result);
            }

            if (safeEmail.isEmpty()) {
                result.put("success", false);
                result.put("message", "이메일을 입력해주세요.");
                return ResponseEntity.badRequest().body(result);
            }

            MemberVO member = new MemberVO();
            member.setMember_id(loginUser.getMember_id());
            member.setRole_code(roleCode);
            member.setPhone(safePhone);
            member.setEmail(safeEmail);

            settingsService.modifyMyAccountInfo(member);

            result.put("success", true);
            result.put("message", "저장 완료");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "서버 오류");
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @PostMapping(value = "/info/password", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> changePassword(
            Authentication authentication,
            @RequestParam(value = "currentPwd", required = false) String currentPwd,
            @RequestParam(value = "newPwd", required = false) String newPwd,
            @RequestParam(value = "confirmPwd", required = false) String confirmPwd) {

        Map<String, Object> result = new HashMap<>();

        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomUser)) {
                result.put("success", false);
                result.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(result);
            }

            String safeCurrentPwd = currentPwd == null ? "" : currentPwd.trim();
            String safeNewPwd = newPwd == null ? "" : newPwd.trim();
            String safeConfirmPwd = confirmPwd == null ? "" : confirmPwd.trim();

            if (safeCurrentPwd.isEmpty()) {
                result.put("success", false);
                result.put("message", "현재 비밀번호를 입력해주세요.");
                return ResponseEntity.badRequest().body(result);
            }

            if (safeNewPwd.isEmpty()) {
                result.put("success", false);
                result.put("message", "새 비밀번호를 입력해주세요.");
                return ResponseEntity.badRequest().body(result);
            }

            if (safeNewPwd.length() < 4) {
                result.put("success", false);
                result.put("message", "새 비밀번호는 4자 이상 입력해주세요.");
                return ResponseEntity.badRequest().body(result);
            }

            if (!safeConfirmPwd.isEmpty() && !safeNewPwd.equals(safeConfirmPwd)) {
                result.put("success", false);
                result.put("message", "새 비밀번호 확인이 일치하지 않습니다.");
                return ResponseEntity.badRequest().body(result);
            }

            CustomUser user = (CustomUser) authentication.getPrincipal();
            MemberVO loginUser = user.getMember();
            String roleCode = normalizeRoleCode(loginUser.getRole_code());

            boolean success = settingsService.changeMyPassword(
                    loginUser.getMember_id(),
                    roleCode,
                    safeCurrentPwd,
                    safeNewPwd
            );

            if (!success) {
                result.put("success", false);
                result.put("message", "현재 비밀번호가 일치하지 않습니다.");
                return ResponseEntity.badRequest().body(result);
            }

            result.put("success", true);
            result.put("message", "비밀번호가 변경되었습니다.");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "서버 처리 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @PostMapping(value = "/info/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteAccount(Authentication authentication) {

        Map<String, Object> result = new HashMap<>();

        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomUser)) {
                result.put("success", false);
                result.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(result);
            }

            CustomUser user = (CustomUser) authentication.getPrincipal();
            MemberVO loginUser = user.getMember();

            settingsService.deleteMyAccount(loginUser.getMember_id());

            result.put("success", true);
            result.put("message", "계정 삭제가 완료되었습니다.");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "계정 삭제 처리 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(result);
        }
    }


    private MemberVO mergeMemberForSession(MemberVO baseUser, MemberVO savedProfile, String roleCode) {
        MemberVO merged = new MemberVO();

        if (baseUser != null) {
            merged.setMember_id(baseUser.getMember_id());
            merged.setLogin_id(baseUser.getLogin_id());
            merged.setPwd(baseUser.getPwd());
            merged.setEmail(baseUser.getEmail());
            merged.setRole_code(baseUser.getRole_code());
            merged.setRole_name(baseUser.getRole_name());
            merged.setAccount_status(baseUser.getAccount_status());
            merged.setLogin_fail_count(baseUser.getLogin_fail_count());
            merged.setLast_login_at(baseUser.getLast_login_at());
            merged.setLocked_at(baseUser.getLocked_at());
            merged.setPwd_changed_at(baseUser.getPwd_changed_at());
            merged.setCreated_at(baseUser.getCreated_at());
            merged.setUpdated_at(baseUser.getUpdated_at());
            merged.setAuthorities(baseUser.getAuthorities());
        }

        if (savedProfile != null) {
            merged.setName(savedProfile.getName());
            merged.setBirth(savedProfile.getBirth());
            merged.setGender(savedProfile.getGender());
            merged.setIntro(savedProfile.getIntro());
            merged.setProfile_image(savedProfile.getProfile_image());

            if (savedProfile.getRole_code() != null) {
                merged.setRole_code(savedProfile.getRole_code());
            } else if (merged.getRole_code() == null) {
                merged.setRole_code(roleCode);
            }
        }

        if (merged.getRole_code() == null) {
            merged.setRole_code(roleCode);
        }

        return merged;
    }

    private String resolveLayout(String roleCode) {
        String normalizedRoleCode = normalizeRoleCode(roleCode);

        if ("TEACHER".equalsIgnoreCase(normalizedRoleCode)) {
            return "common/layout/teacherLayout";
        }
        if ("PARENT".equalsIgnoreCase(normalizedRoleCode)) {
            return "common/layout/parentLayout";
        }
        return "common/layout/studentLayout";
    }

    private String normalizeRoleCode(String roleCode) {
        if (roleCode == null) {
            return "";
        }
        if (roleCode.startsWith("ROLE_")) {
            return roleCode.substring(5);
        }
        return roleCode;
    }

    private String getBaseSettingsPath(String roleCode) {
        if ("STUDENT".equalsIgnoreCase(roleCode)) {
            return "/student/settings";
        }
        if ("PARENT".equalsIgnoreCase(roleCode)) {
            return "/parent/settings";
        }
        if ("TEACHER".equalsIgnoreCase(roleCode)) {
            return "/teacher/settings";
        }
        return "/settings";
    }

    private String saveProfileImage(MultipartFile profileFile) throws IOException {
        String contentType = profileFile.getContentType();

        if (contentType == null) {
            throw new IllegalArgumentException("파일 형식을 확인할 수 없습니다.");
        }

        if (!contentType.equals("image/jpeg")
                && !contentType.equals("image/png")
                && !contentType.equals("image/webp")) {
            throw new IllegalArgumentException("JPG, PNG, WEBP 파일만 업로드할 수 있습니다.");
        }

        if (profileFile.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("파일 크기는 5MB 이하여야 합니다.");
        }

        String originalName = profileFile.getOriginalFilename();
        String ext = "png";

        if (originalName != null && originalName.lastIndexOf(".") > -1) {
            ext = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
        } else if ("image/jpeg".equals(contentType)) {
            ext = "jpg";
        } else if ("image/webp".equals(contentType)) {
            ext = "webp";
        }

        String fileName = UUID.randomUUID().toString() + "." + ext;

        String uploadPath = servletContext.getRealPath("/resources/upload/profile");
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File dest = new File(uploadDir, fileName);
        profileFile.transferTo(dest);

        return fileName;
    }

    private Map<String, Object> toResponseMember(MemberVO savedProfile) {
        Map<String, Object> member = new HashMap<>();

        if (savedProfile == null) {
            return member;
        }

        member.put("name", savedProfile.getName());
        member.put("gender", savedProfile.getGender());
        member.put("intro", savedProfile.getIntro());
        member.put("profile_image", savedProfile.getProfile_image());

        if (savedProfile.getBirth() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            member.put("birth", sdf.format(savedProfile.getBirth()));
        } else {
            member.put("birth", "");
        }

        return member;
    }
}