package com.spring.controller.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClassPageController {

	@GetMapping("/classes")
	public String classList(Authentication authentication, Model model) {

	    String role = getRole(authentication);

	    model.addAttribute("pageTitle", "클래스 목록");
	    model.addAttribute("currentUri", "/classes");
	    model.addAttribute("contentPage", "/WEB-INF/views/class/listContent.jsp");

	    if ("ROLE_STUDENT".equals(role)) {
	        setStudentLayoutBase(model);
	        model.addAttribute("roleType", "student");
	        model.addAttribute("showJoinButton", true);
	        model.addAttribute("showCreateButton", false);
	        model.addAttribute("classList", createStudentClassList());

	        return "common/layout/studentLayout";
	    }

	    if ("ROLE_PARENT".equals(role)) {
	        setParentLayoutBase(model);
	        model.addAttribute("roleType", "parent");
	        model.addAttribute("showJoinButton", false);
	        model.addAttribute("showCreateButton", false);
	        model.addAttribute("classList", createParentClassList());

	        return "common/layout/parentLayout";
	    }

	    return "redirect:/";
	}

    private String getRole(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return "";
        }

        return authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("");
    }

    private void setStudentLayoutBase(Model model) {
        model.addAttribute("userName", "김학생");
        model.addAttribute("roleName", "학생 계정");
        model.addAttribute("isClassDetail", false);
        model.addAttribute("classId", null);
        model.addAttribute("className", null);
    }

    private void setParentLayoutBase(Model model) {
        model.addAttribute("userName", "김학부모");
        model.addAttribute("roleName", "학부모 계정");
        model.addAttribute("isClassDetail", false);
        model.addAttribute("classId", null);
        model.addAttribute("className", null);
    }

    private List<Map<String, Object>> createStudentClassList() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(createClassItem(1, "2026학년도", "3학년 2반", "김교사", 28, "green",
                null, null, true, "/student/classes/1"));
        return list;
    }

    private List<Map<String, Object>> createParentClassList() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(createClassItem(1, "2026학년도", "3학년 2반", "이선생", 24, "brand-blue",
                "김학생", "초등 3학년", false, "/parent/classes/1"));
        return list;
    }

    private Map<String, Object> createClassItem(int classId, String yearLabel, String className,
            String teacherName, int memberCount, String coverType,
            String childName, String childGrade, boolean joinAvailable,
            String detailUrl) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("classId", classId);
		map.put("yearLabel", yearLabel);
		map.put("className", className);
		map.put("teacherName", teacherName);
		map.put("memberCount", memberCount);
		map.put("coverType", coverType);
		map.put("childName", childName);
		map.put("childGrade", childGrade);
		map.put("joinAvailable", joinAvailable);
		map.put("detailUrl", detailUrl);
		return map;
		}
}