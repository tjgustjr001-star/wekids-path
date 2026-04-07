package com.spring.controller.admin;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.dto.SupportAnswerVO;
import com.spring.dto.SupportFileVO;
import com.spring.dto.SupportVO;
import com.spring.dto.admin.AdminStudentRegistDTO;
import com.spring.dto.admin.AdminTeacherListDTO;
import com.spring.dto.admin.AdminTeacherRegistDTO;
import com.spring.dto.admin.MonthlyJoinCountDTO;
import com.spring.service.admin.AdminClassService;
import com.spring.service.admin.AdminStatsService;
import com.spring.service.admin.AdminSupportService;
import com.spring.service.admin.AdminTeacherService;
import com.spring.service.admin.AdminUserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminTeacherService adminTeacherService;
	@Autowired
	private AdminClassService adminClassService;
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private AdminStatsService adminStatsService;
	@Autowired
	private AdminSupportService adminSupportService;

	@GetMapping({ "", "/", "/home" })
	public String home(Model model) throws SQLException {
		model.addAttribute("totalUserCount", adminUserService.getTotalUserCount());
		model.addAttribute("activeClassCount", adminClassService.getActiveClassCount());
		model.addAttribute("newTeacherCount", adminTeacherService.getNewTeacherCount());
		model.addAttribute("weeklyLoginTrend", adminUserService.getWeeklyLoginTrend());
		model.addAttribute("pendingSupportCount", adminSupportService.getPendingSupportCount());
		model.addAttribute("contentPage", "/WEB-INF/views/admin/home.jsp");
		return "admin/layout/adminLayout";
	}

	@GetMapping("/teachers")
	public String teachers(Model model) throws SQLException {
		List<AdminTeacherListDTO> teacherList = adminTeacherService.getTeacherList();
		List<MonthlyJoinCountDTO> trendList = adminTeacherService.getTeacherJoinTrend();

		model.addAttribute("teacherList", teacherList);
		model.addAttribute("contentPage", "/WEB-INF/views/admin/teachers.jsp");
		model.addAttribute("trendList", trendList);
		return "admin/layout/adminLayout";
	}

	@PostMapping("/teachers/regist")
	public String registTeacher(AdminTeacherRegistDTO registDTO) throws Exception {
		adminTeacherService.registTeacher(registDTO);
		return "redirect:/admin/teachers";
	}

	@PostMapping("/teachers/status")
	public String updateTeacherStatus(@RequestParam("teacherId") int teacherId,
			@RequestParam("accountStatus") String accountStatus,
			@RequestParam(value = "redirectPage", required = false, defaultValue = "list") String redirectPage)
			throws Exception {

		adminTeacherService.modifyTeacherStatus(teacherId, accountStatus);

		if ("detail".equals(redirectPage)) {
			return "redirect:/admin/teachers/" + teacherId;
		}

		return "redirect:/admin/teachers";
	}

	@GetMapping("/teachers/{id}")
	public String teacherDetail(@PathVariable("id") int id, Model model) throws SQLException {
		model.addAttribute("teacher", adminTeacherService.getTeacherDetailById(id));
		model.addAttribute("classList", adminTeacherService.getTeacherClassListById(id));
		model.addAttribute("teacherActivityChart", adminTeacherService.getTeacherActivityChart(id));
		model.addAttribute("contentPage", "/WEB-INF/views/admin/teacherDetail.jsp");
		return "admin/layout/adminLayout";
	}

	@GetMapping("/classes")
	public String classes(Model model) throws SQLException {
		model.addAttribute("classList", adminClassService.getClassList());
		model.addAttribute("contentPage", "/WEB-INF/views/admin/classes.jsp");
		return "admin/layout/adminLayout";
	}

	@PostMapping("/classes/status")
	public String updateClassStatus(@RequestParam("classId") int classId,
			@RequestParam("classStatus") String classStatus) throws Exception {

		adminClassService.modifyClassStatus(classId, classStatus);
		return "redirect:/admin/classes";
	}

	@GetMapping("/users")
	public String users(Model model) throws Exception {
		model.addAttribute("userList", adminUserService.getUserList());
		model.addAttribute("userRoleLoginTrend", adminUserService.getUserRoleLoginTrend());
		model.addAttribute("totalUserCount", adminUserService.getTotalUserCount());
		model.addAttribute("inspectionNeedCount", adminUserService.getInspectionNeedCount());
		model.addAttribute("inspectionNeedList", adminUserService.getInspectionNeedAccountList());
		model.addAttribute("contentPage", "/WEB-INF/views/admin/users.jsp");
		return "admin/layout/adminLayout";
	}

	@PostMapping("/users/students/regist")
	public String registStudent(AdminStudentRegistDTO registDTO) throws Exception {
		adminUserService.registStudent(registDTO);
		return "redirect:/admin/users";
	}

	@GetMapping("/users/{id}")
	public String userDetail(@PathVariable("id") int id, Model model) throws Exception {
		model.addAttribute("user", adminUserService.getUserDetailById(id));
		model.addAttribute("weeklyLoginTrend", adminUserService.getUserWeeklyLoginTrend(id));
		model.addAttribute("contentPage", "/WEB-INF/views/admin/userDetail.jsp");
		return "admin/layout/adminLayout";
	}

	@PostMapping("/users/status")
	public String updateUserStatus(@RequestParam("memberId") int memberId,
			@RequestParam("accountStatus") String accountStatus,
			@RequestParam(value = "redirectPage", required = false, defaultValue = "list") String redirectPage)
			throws Exception {

		adminUserService.modifyUserStatus(memberId, accountStatus);

		if ("detail".equals(redirectPage)) {
			return "redirect:/admin/users/" + memberId;
		}
		return "redirect:/admin/users";
	}

	@GetMapping("/support")
	public String support(Model model) throws Exception {
		model.addAttribute("supportList", adminSupportService.getSupportList());
		List<Map<String, Object>> weeklyStats = adminSupportService.selectWeeklyStats();
		model.addAttribute("weeklyStats", weeklyStats);
		model.addAttribute("contentPage", "/WEB-INF/views/admin/support.jsp");
		return "admin/layout/adminLayout";
	}

	@GetMapping("/support/detail")
	public String supportDetail(@RequestParam("supportNo") int supportNo, Model model) {
		SupportVO support = adminSupportService.getSupportByNo(supportNo);
		SupportAnswerVO answer = adminSupportService.getAnswerBySupportNo(supportNo);
		List<SupportFileVO> fileList = adminSupportService.getFileList(supportNo);
		model.addAttribute("support", support);
		model.addAttribute("answer", answer);
		model.addAttribute("fileList", fileList);
		model.addAttribute("contentPage", "/WEB-INF/views/admin/supportDetail.jsp");
		return "admin/layout/adminLayout";
	}

	@GetMapping("/support/answer")
	public String answerForm(@RequestParam("supportNo") int supportNo, Model model) {
		SupportVO support = adminSupportService.getSupportByNo(supportNo);
		List<SupportFileVO> fileList = adminSupportService.getFileList(supportNo);
		model.addAttribute("support", support);
		model.addAttribute("fileList", fileList);
		model.addAttribute("contentPage", "/WEB-INF/views/admin/supportAnswer.jsp");
		return "admin/layout/adminLayout";
	}

	@GetMapping("/logs")
	public String logs(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/admin/logs.jsp");
		return "admin/layout/adminLayout";
	}

	@GetMapping("/stats")
	public String stats(@RequestParam(value = "period", required = false, defaultValue = "daily") String period,
			Model model) throws Exception {

		model.addAllAttributes(adminStatsService.getStatsPageData(period));
		model.addAttribute("contentPage", "/WEB-INF/views/admin/stats.jsp");
		return "admin/layout/adminLayout";
	}

	@GetMapping("/stats/classes/{classId}")
	public String statsDetail(@PathVariable("classId") int classId,
			@RequestParam(value = "period", required = false, defaultValue = "daily") String period, Model model)
			throws Exception {

		model.addAllAttributes(adminStatsService.getStatsDetailPageData(classId, period));
		model.addAttribute("contentPage", "/WEB-INF/views/admin/statsDetail.jsp");
		return "admin/layout/adminLayout";
	}
}
