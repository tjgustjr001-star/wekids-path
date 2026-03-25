package com.spring.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.dto.ChildLinkVO;
import com.spring.dto.ParentChildVO;
import com.spring.security.CustomUser;
import com.spring.service.SettingsService;

@Controller
@RequestMapping({"/student/settings", "/parent/settings", "/teacher/settings"})
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("")
    public String settingsMain(Authentication authentication, Model model) {

        CustomUser user = (CustomUser) authentication.getPrincipal();
        String roleCode = user.getMember().getRole_code();

        model.addAttribute("role", roleCode);

        return "settings/settings";
    }

    @GetMapping("/child-link")
    public String childLinkPage(Authentication authentication, Model model) throws SQLException {

        CustomUser user = (CustomUser) authentication.getPrincipal();

        int memberId = user.getMember().getMember_id();
        String roleCode = user.getMember().getRole_code();

        model.addAttribute("role", roleCode);

        if (isStudent(roleCode)) {
            ChildLinkVO linkInfo = settingsService.getStudentLinkInfo(memberId);
            model.addAttribute("linkInfo", linkInfo);
            return "settings/childLinkStudent";
        }

        if (isParent(roleCode)) {
            List<ParentChildVO> childList = settingsService.getLinkedChildren(memberId);
            model.addAttribute("childList", childList);
            return "settings/childLinkParent";
        }

        if (isTeacher(roleCode)) {
            return "redirect:/teacher/settings";
        }

        return "redirect:/student/settings";
    }

    @PostMapping("/child-link/generate")
    @ResponseBody
    public ResponseEntity<String> generateLinkCode(Authentication authentication) throws SQLException {

        CustomUser user = (CustomUser) authentication.getPrincipal();

        int memberId = user.getMember().getMember_id();
        String roleCode = user.getMember().getRole_code();

        if (!isStudent(roleCode)) {
            return ResponseEntity.badRequest().body("only_student");
        }

        String code = settingsService.generateParentLinkCode(memberId);
        return ResponseEntity.ok(code);
    }

    @PostMapping("/child-link/connect")
    public String connectChild(@RequestParam("code") String code,
                               Authentication authentication,
                               RedirectAttributes rttr) throws SQLException {

        CustomUser user = (CustomUser) authentication.getPrincipal();

        int parentMemberId = user.getMember().getMember_id();
        String roleCode = user.getMember().getRole_code();

        if (!isParent(roleCode)) {
            rttr.addFlashAttribute("msg", "only_parent");
            return "redirect:/parent/settings/child-link";
        }

        boolean result = settingsService.connectStudentToParent(parentMemberId, code);

        if (result) {
            rttr.addFlashAttribute("msg", "success");
        } else {
            rttr.addFlashAttribute("msg", "fail");
        }

        return "redirect:/parent/settings/child-link";
    }

    @GetMapping("/child-link/detail")
    public String childDetail(@RequestParam("studentId") int studentId,
                              Authentication authentication,
                              RedirectAttributes rttr,
                              Model model) throws SQLException {

        CustomUser user = (CustomUser) authentication.getPrincipal();
        int parentId = user.getMember().getMember_id();
        String roleCode = user.getMember().getRole_code();

        if (!isParent(roleCode)) {
            rttr.addFlashAttribute("msg", "only_parent");
            return "redirect:/parent/settings/child-link";
        }

        ParentChildVO child = settingsService.getChildDetail(parentId, studentId);

        if (child == null) {
            rttr.addFlashAttribute("msg", "not_found_child");
            return "redirect:/parent/settings/child-link";
        }

        model.addAttribute("role", roleCode);
        model.addAttribute("child", child);
        return "settings/childDetailParent";
    }

    @PostMapping("/child-link/remove")
    public String removeChild(@RequestParam("studentId") int studentId,
                              Authentication authentication,
                              RedirectAttributes rttr) throws SQLException {

        CustomUser user = (CustomUser) authentication.getPrincipal();
        int parentId = user.getMember().getMember_id();
        String roleCode = user.getMember().getRole_code();

        if (!isParent(roleCode)) {
            rttr.addFlashAttribute("msg", "only_parent");
            return "redirect:/parent/settings/child-link";
        }

        boolean result = settingsService.removeChildLink(parentId, studentId);

        if (result) {
            rttr.addFlashAttribute("msg", "removed");
        } else {
            rttr.addFlashAttribute("msg", "remove_fail");
        }

        return "redirect:/parent/settings/child-link";
    }

    private boolean isStudent(String roleCode) {
        return "STUDENT".equals(roleCode) || "ROLE_STUDENT".equals(roleCode);
    }

    private boolean isParent(String roleCode) {
        return "PARENT".equals(roleCode) || "ROLE_PARENT".equals(roleCode);
    }

    private boolean isTeacher(String roleCode) {
        return "TEACHER".equals(roleCode) || "ROLE_TEACHER".equals(roleCode);
    }
}