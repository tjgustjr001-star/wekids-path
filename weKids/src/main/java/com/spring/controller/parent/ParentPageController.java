
package com.spring.controller.parent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spring.dto.ClassVO;
import com.spring.dto.MemberVO;
import com.spring.dto.NoticeVO;
import com.spring.dto.ParentChildVO;
import com.spring.security.CustomUser;
import com.spring.service.ClassService;
import com.spring.service.NoticeService;
import com.spring.service.SettingsService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ParentPageController {
	
    @Autowired
    private NoticeService noticeService;
	
    @Autowired
    private ClassService classService;

    @Autowired
    private SettingsService settingsService;
	
    @GetMapping("/parent")
    public String parentHome(Model model) {
        model.addAttribute("pageTitle", "학부모 홈");
        model.addAttribute("currentUri", "/parent");
        model.addAttribute("contentPage", "/WEB-INF/views/parent/homeContent.jsp");

        setParentLayoutBase(model);

        model.addAttribute("greetingTitle", "안녕하세요, 김학부모님!");
        model.addAttribute("greetingMessage", "자녀의 학습과 클래스 소식을 확인해보세요.");

        Map<String, Object> childSummary = new LinkedHashMap<>();
        childSummary.put("childName", "김학생");
        childSummary.put("childGrade", "초등 3학년");
        childSummary.put("classCount", 2);
        childSummary.put("pendingAssignments", 1);
        childSummary.put("newBulletins", 2);
        model.addAttribute("childSummary", childSummary);

        List<Map<String, Object>> children = createChildrenData();
        model.addAttribute("childrenData", children);
        model.addAttribute("selectedChildId", "child1");
        model.addAttribute("selectedChild", children.get(0));
        model.addAttribute("progressPercent", 63);
        model.addAttribute("notices", createNoticeList());

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/classes")
    public String parentClassList(Model model, HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "클래스 목록");
        model.addAttribute("currentUri", "/parent/classes");
        model.addAttribute("contentPage", "/WEB-INF/views/class/listContent.jsp");

        setParentLayoutBase(model);

        MemberVO loginUser = getLoginUser(session);

        model.addAttribute("roleType", "parent");
        model.addAttribute("showJoinButton", false);
        model.addAttribute("showCreateButton", false);

        if (loginUser != null) {
            model.addAttribute("classList", classService.getParentClassList(loginUser.getMember_id()));
        } else {
            model.addAttribute("classList", List.of());
        }

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/children")
    public String parentChildrenList(Model model, HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "자녀 목록");
        model.addAttribute("currentUri", "/parent/children");
        model.addAttribute("contentPage", "/WEB-INF/views/settings/childLinkParent.jsp");

        setParentLayoutBase(model);

        MemberVO loginUser = getLoginUser(session);

        if (loginUser != null) {
            List<ParentChildVO> childList = settingsService.getLinkedChildren(loginUser.getMember_id());
            model.addAttribute("childList", childList);
        } else {
            model.addAttribute("childList", List.of());
        }

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/children/{childId}")
    public String parentChildDetail(@PathVariable("childId") String childId, Model model) {
        model.addAttribute("pageTitle", "자녀 상세");
        model.addAttribute("currentUri", "/parent/children");
        model.addAttribute("contentPage", "/WEB-INF/views/parent/children/detailContent.jsp");

        setParentLayoutBase(model);

        List<Map<String, Object>> allChildren = createChildDetailList();
        Map<String, Object> currentChild = allChildren.get(0);

        for (Map<String, Object> child : allChildren) {
            if (childId.equals(String.valueOf(child.get("id")))) {
                currentChild = child;
                break;
            }
        }

        model.addAttribute("allChildren", allChildren);
        model.addAttribute("currentChild", currentChild);

        int learningDone = (Integer) currentChild.get("learningDone");
        int learningTotal = (Integer) currentChild.get("learningTotal");
        int assignmentDone = (Integer) currentChild.get("assignmentDone");
        int assignmentTotal = (Integer) currentChild.get("assignmentTotal");

        int overallProgress = learningTotal > 0 && assignmentTotal > 0
                ? Math.round(((float) learningDone / learningTotal) * 50
                + ((float) assignmentDone / assignmentTotal) * 50)
                : 0;
        model.addAttribute("overallProgress", overallProgress);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> weeklySubjects =
                (List<Map<String, Object>>) currentChild.get("weeklySubjects");

        int sum = 0;
        for (Map<String, Object> subject : weeklySubjects) {
            sum += (Integer) subject.get("progress");
        }
        int weeklyAverage = weeklySubjects.isEmpty() ? 0 : Math.round((float) sum / weeklySubjects.size());
        model.addAttribute("weeklyAverage", weeklyAverage);

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/classes/{classId}")
    public String parentClassHome(@PathVariable("classId") int classId,
                                  Model model,
                                  HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "클래스 홈");
        model.addAttribute("currentUri", "/parent/classes/" + classId);
        model.addAttribute("contentPage", "/WEB-INF/views/parent/class/homeContent.jsp");

        setParentLayoutBase(model);
        setParentClassDetailBase(model, classId, session);

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/classes/{classId}/bulletins")
    public String parentBulletinList(@PathVariable("classId") int classId,
                                     Model model,
                                     HttpSession session) throws Exception {

        MemberVO loginUser = getLoginUser(session);

        List<NoticeVO> noticeList = noticeService.getNoticeList(classId, loginUser);

        long totalCount = noticeList.size();
        long studentCount = noticeList.stream()
                .filter(notice -> "STUDENT".equals(notice.getTarget()))
                .count();
        long parentCount = noticeList.stream()
                .filter(notice -> "PARENT".equals(notice.getTarget()))
                .count();
        
        model.addAttribute("classId", classId);
        model.addAttribute("pageTitle", "가정통신문");
        model.addAttribute("currentUri", "/parent/classes/" + classId + "/bulletins");
        model.addAttribute("contentPage", "/WEB-INF/views/notice/list.jsp");

        model.addAttribute("isTeacher", false);
        model.addAttribute("isStudentOrParent", true);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("parentCount", parentCount);

        setParentLayoutBase(model);
        setParentClassDetailBase(model, classId, session);

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/classes/{classId}/assignments")
    public String parentAssignmentList(@PathVariable("classId") int classId,
                                       Model model,
                                       HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "과제");
        model.addAttribute("currentUri", "/parent/classes/" + classId + "/assignments");
        model.addAttribute("contentPage", "/WEB-INF/views/parent/assignment/listContent.jsp");

        setParentLayoutBase(model);
        setParentClassDetailBase(model, classId, session);

        List<Map<String, Object>> children = new ArrayList<>();
        children.add(createParentAssignmentChild(1, "김학생", "초등 3학년"));
        children.add(createParentAssignmentChild(2, "김동생", "초등 1학년"));
        model.addAttribute("assignmentChildren", children);
        model.addAttribute("selectedChildId", 1);

        model.addAttribute("child1Assignments", Arrays.asList(
                createParentAssignmentItem(1, "과학 실험 관찰 보고서", "과학", "진행", "오늘 18:00", "파일 제출", false, "",
                        "", "오늘 수업시간에 진행한 강낭콩 싹틔우기 관찰 결과를 보고서 양식에 맞추어 작성해 제출하세요.", ""),
                createParentAssignmentItem(2, "수학 3단원 문제풀이 (p.45-48)", "수학", "마감임박", "내일 09:00", "텍스트 작성", false, "",
                        "", "교과서 45쪽부터 48쪽까지의 문제를 풀고, 어려운 문제의 번호와 이유를 텍스트로 남겨주세요.", ""),
                createParentAssignmentItem(3, "국어 독서록 작성", "국어", "제출완료", "어제 18:00", "파일 제출", true, "어제 16:30",
                        "", "이번 달 추천 도서를 읽고 독서록을 작성하여 제출하세요.", "홍길동전 독서록을 작성하여 첨부합니다."),
                createParentAssignmentItem(4, "영어 단어 쓰기", "영어", "반려", "2일 전", "텍스트 작성", true, "",
                        "글씨를 조금 더 바르게 써주세요. 재제출 바랍니다.", "알파벳 A부터 Z까지 5번씩 쓰고 사진을 찍어 제출하세요.", "영어 단어를 써서 제출했습니다.")
        ));

        model.addAttribute("child2Assignments", Arrays.asList(
                createParentAssignmentItem(5, "국어 받아쓰기 연습", "국어", "진행", "내일 15:00", "텍스트 작성", false, "",
                        "", "받아쓰기 문장을 공책에 연습하고 작성 내용을 제출하세요.", ""),
                createParentAssignmentItem(6, "수학 덧셈 문제풀이", "수학", "제출완료", "어제 12:00", "파일 제출", true, "어제 10:20",
                        "", "덧셈 문제풀이 활동지를 작성하여 제출하세요.", "")
        ));

        return "common/layout/parentLayout";
    }

    @GetMapping("/parent/classes/{classId}/reports")
    public String parentReportList(@PathVariable("classId") int classId,
                                   Model model,
                                   HttpSession session) throws Exception {
        model.addAttribute("pageTitle", "리포트");
        model.addAttribute("currentUri", "/parent/classes/" + classId + "/reports");
        model.addAttribute("contentPage", "/WEB-INF/views/parent/report/listContent.jsp");

        setParentLayoutBase(model);
        setParentClassDetailBase(model, classId, session);

        List<Map<String, Object>> allChildren = new ArrayList<>();
        allChildren.add(createParentAssignmentChild(1, "김학생", "초등 3학년"));
        allChildren.add(createParentAssignmentChild(2, "김동생", "초등 1학년"));

        List<Map<String, Object>> reportChildren = (classId == 1)
                ? Arrays.asList(allChildren.get(0))
                : allChildren;

        model.addAttribute("reportChildren", reportChildren);
        model.addAttribute("selectedChildId", reportChildren.get(0).get("id"));

        model.addAttribute("child1WeeklyReport", createParentReportData(
                createParentReportChart(
                        createParentReportSubject(1, "국어", 3, 3, "blue"),
                        createParentReportSubject(2, "수학", 4, 5, "yellow"),
                        createParentReportSubject(3, "사회", 2, 3, "green"),
                        createParentReportSubject(4, "과학", 3, 3, "dark"),
                        createParentReportSubject(5, "영어", 2, 2, "red")
                ),
                12, 5, 8,
                "이번 주에는 수학 과목에서의 성장이 특히 돋보였습니다!\n스스로 질문을 많이 하고 끝까지 이해하려는 모습이 훌륭해요."
        ));

        model.addAttribute("child1MonthlyReport", createParentReportData(
                createParentReportChart(
                        createParentReportSubject(1, "국어", 10, 12, "blue"),
                        createParentReportSubject(2, "수학", 18, 20, "yellow"),
                        createParentReportSubject(3, "사회", 8, 10, "green"),
                        createParentReportSubject(4, "과학", 9, 10, "dark"),
                        createParentReportSubject(5, "영어", 7, 8, "red")
                ),
                45, 20, 32,
                "이번 달은 전반적으로 꾸준한 학습 태도를 보여주었습니다.\n다음 달에는 과학 실험 보고서 작성에 조금 더 집중해보면 좋겠습니다."
        ));

        model.addAttribute("child1SemesterReport", createParentReportData(
                createParentReportChart(
                        createParentReportSubject(1, "국어", 28, 30, "blue"),
                        createParentReportSubject(2, "수학", 48, 50, "yellow"),
                        createParentReportSubject(3, "사회", 22, 25, "green"),
                        createParentReportSubject(4, "과학", 24, 25, "dark"),
                        createParentReportSubject(5, "영어", 19, 20, "red")
                ),
                120, 55, 96,
                "1학기 동안 정말 고생 많았습니다!\n모든 과목에서 눈에 띄는 성장을 이뤄냈고 스스로 학습 계획을 세우는 능력이 향상되었습니다."
        ));

        model.addAttribute("child2WeeklyReport", createParentReportData(
                createParentReportChart(
                        createParentReportSubject(1, "국어", 2, 3, "blue"),
                        createParentReportSubject(2, "수학", 3, 4, "yellow"),
                        createParentReportSubject(3, "사회", 3, 3, "green"),
                        createParentReportSubject(4, "과학", 3, 3, "dark")
                ),
                8, 4, 6,
                "사회 과목에 대한 흥미가 매우 높습니다!\n국어 독해력 향상을 위해 매일 조금씩 책 읽는 습관을 기르면 더욱 좋을 것 같습니다."
        ));

        model.addAttribute("child2MonthlyReport", createParentReportData(
                createParentReportChart(
                        createParentReportSubject(1, "국어", 8, 10, "blue"),
                        createParentReportSubject(2, "수학", 14, 16, "yellow"),
                        createParentReportSubject(3, "사회", 10, 10, "green"),
                        createParentReportSubject(4, "과학", 8, 10, "dark")
                ),
                35, 16, 25,
                "이번 달은 지난 달보다 수학 문제 풀이 속도가 눈에 띄게 빨라졌습니다.\n가정에서도 꾸준히 연습할 수 있도록 지도 부탁드립니다."
        ));

        model.addAttribute("child2SemesterReport", createParentReportData(
                createParentReportChart(
                        createParentReportSubject(1, "국어", 25, 30, "blue"),
                        createParentReportSubject(2, "수학", 40, 45, "yellow"),
                        createParentReportSubject(3, "사회", 28, 30, "green"),
                        createParentReportSubject(4, "과학", 22, 25, "dark")
                ),
                90, 42, 75,
                "1학기 동안 학교 생활에 잘 적응하며 건강하게 학습을 진행했습니다.\n2학기에는 국어 어휘력 확장에 초점을 맞추면 좋겠습니다."
        ));

        return "common/layout/parentLayout";
    }

    private void setParentLayoutBase(Model model) {
        model.addAttribute("roleKey", "parent");
        model.addAttribute("roleLabel", "학부모");
        model.addAttribute("homeUrl", "/parent");
        model.addAttribute("classMenuUrl", "/parent/classes");
        model.addAttribute("childrenMenuUrl", "/parent/children");
        model.addAttribute("notificationUrl", "/parent/notifications");
        model.addAttribute("settingsUrl", "/parent/settings");
        model.addAttribute("isClassDetail", false);
        model.addAttribute("classId", null);
        model.addAttribute("className", null);
    }

    private void setParentClassDetailBase(Model model, int classId, HttpSession session) throws Exception {
        MemberVO loginUser = getLoginUser(session);
        ClassVO classInfo = null;

        if (loginUser != null) {
            classInfo = classService.getParentClassDetail(loginUser.getMember_id(), classId);
        }

        model.addAttribute("isClassDetail", true);
        model.addAttribute("classId", classId);
        model.addAttribute("className", classInfo != null ? classInfo.getClassName() : "클래스");

        model.addAttribute("classHomeUrl", "/parent/classes/" + classId);
        model.addAttribute("bulletinUrl", "/parent/classes/" + classId + "/bulletins");
        model.addAttribute("assignmentUrl", "/parent/classes/" + classId + "/assignments");
        model.addAttribute("reportUrl", "/parent/classes/" + classId + "/reports");
    }

    private List<Map<String, Object>> createChildrenData() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> child1 = new LinkedHashMap<>();
        child1.put("id", "child1");
        child1.put("name", "김학생");
        child1.put("grade", "3학년 2반");
        child1.put("school", "서울초등학교");
        child1.put("teacher", "이선생");
        child1.put("phone", "010-1111-2222");
        child1.put("learningStats", createLearningStats(8, 3, 5));
        child1.put("assignments", Arrays.asList(
                createAssignment(1, "수학 3단원 문제풀이", "수학", "오늘 23:59", "미진행"),
                createAssignment(2, "과학 실험 관찰 보고서", "과학", "내일 18:00", "진행중"),
                createAssignment(3, "국어 독서록 작성", "국어", "금요일 18:00", "완료")
        ));
        child1.put("weeklyStudyData", Arrays.asList(
                createStudyDay("월", 2.5, 63),
                createStudyDay("화", 3.2, 80),
                createStudyDay("수", 1.8, 45),
                createStudyDay("목", 4.0, 100),
                createStudyDay("금", 2.1, 53),
                createStudyDay("토", 1.0, 25),
                createStudyDay("일", 0.0, 8)
        ));

        Map<String, Object> child2 = new LinkedHashMap<>();
        child2.put("id", "child2");
        child2.put("name", "김동생");
        child2.put("grade", "1학년 1반");
        child2.put("school", "서울초등학교");
        child2.put("teacher", "박선생");
        child2.put("phone", "없음");
        child2.put("learningStats", createLearningStats(5, 1, 4));
        child2.put("assignments", Arrays.asList(
                createAssignment(4, "그림일기 쓰기", "국어", "오늘 18:00", "완료"),
                createAssignment(5, "가족 얼굴 그리기", "미술", "내일 18:00", "진행중")
        ));
        child2.put("weeklyStudyData", Arrays.asList(
                createStudyDay("월", 1.5, 38),
                createStudyDay("화", 1.0, 25),
                createStudyDay("수", 2.0, 50),
                createStudyDay("목", 1.5, 38),
                createStudyDay("금", 1.0, 25),
                createStudyDay("토", 0.5, 13),
                createStudyDay("일", 0.0, 8)
        ));

        list.add(child1);
        list.add(child2);
        return list;
    }

    private List<Map<String, Object>> createNoticeList() {
        return Arrays.asList(
                createNotice("yellow", "체험학습 동의서 제출 안내", "2026학년도 1학기 현장체험학습 관련 안내문입니다.", "2시간 전"),
                createNotice("blue", "수학 단원평가 결과 확인", "3단원 평가 결과 및 오답노트 과제가 등록되었습니다.", "어제")
        );
    }

    private List<Map<String, Object>> createChildDetailList() {
        return Arrays.asList(
                createChildDetail("1", "김지훈", "2026학년도 3학년 2반", "3학년", 1, "김", "오늘 08:30",
                        "김선생",
                        "지훈이가 최근 수학 시간에 질문도 많이 하고 발표도 잘하고 있습니다. 가정에서도 많은 칭찬 부탁드립니다!",
                        "어제",
                        8, 8, 4, 3,
                        Arrays.asList(
                                createPendingItem(1, "수학 3단원 오답노트 과제 미제출", "assignment", "오늘 23:59까지", true),
                                createPendingItem(2, "가정통신문 동의서 회신 필요", "notice", "내일 18:00까지", false)
                        ),
                        Arrays.asList(
                                createSubjectProgress("국어", 100, "blue"),
                                createSubjectProgress("수학", 85, "green"),
                                createSubjectProgress("과학", 100, "purple"),
                                createSubjectProgress("사회", 60, "amber")
                        )),
                createChildDetail("2", "김서연", "2026학년도 1학년 3반", "1학년", 15, "김", "어제 16:42",
                        "박선생",
                        "서연이가 미술 시간에 창의적인 작품을 많이 만들어서 칭찬합니다. 읽기 연습도 꾸준히 잘 하고 있어요!",
                        "3일 전",
                        6, 5, 3, 3,
                        new ArrayList<>(),
                        Arrays.asList(
                                createSubjectProgress("국어", 80, "blue"),
                                createSubjectProgress("수학", 100, "green"),
                                createSubjectProgress("통합(봄)", 70, "pink")
                        ))
        );
    }

    private Map<String, Object> createChildDetail(String id, String name, String className, String grade, int number,
                                                  String initial, String lastAccess, String teacherName,
                                                  String teacherComment, String teacherCommentDate,
                                                  int learningTotal, int learningDone,
                                                  int assignmentTotal, int assignmentDone,
                                                  List<Map<String, Object>> pendingItems,
                                                  List<Map<String, Object>> weeklySubjects) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("className", className);
        map.put("grade", grade);
        map.put("number", number);
        map.put("initial", initial);
        map.put("lastAccess", lastAccess);
        map.put("teacherName", teacherName);
        map.put("teacherComment", teacherComment);
        map.put("teacherCommentDate", teacherCommentDate);
        map.put("learningTotal", learningTotal);
        map.put("learningDone", learningDone);
        map.put("assignmentTotal", assignmentTotal);
        map.put("assignmentDone", assignmentDone);
        map.put("pendingItems", pendingItems);
        map.put("weeklySubjects", weeklySubjects);
        return map;
    }

    private Map<String, Object> createPendingItem(int id, String title, String type, String deadline, boolean urgent) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("type", type);
        map.put("deadline", deadline);
        map.put("urgent", urgent);
        return map;
    }

    private Map<String, Object> createSubjectProgress(String subject, int progress, String colorClass) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("subject", subject);
        map.put("progress", progress);
        map.put("colorClass", colorClass);
        return map;
    }

    private Map<String, Object> createLearningStats(int total, int inProgress, int completed) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("total", total);
        map.put("inProgress", inProgress);
        map.put("completed", completed);
        return map;
    }

    private Map<String, Object> createAssignment(int id, String title, String subject, String deadline, String status) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("subject", subject);
        map.put("deadline", deadline);
        map.put("status", status);
        return map;
    }

    private Map<String, Object> createStudyDay(String day, double hours, int heightPercent) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("day", day);
        map.put("hours", hours);
        map.put("heightPercent", heightPercent);
        return map;
    }

    private Map<String, Object> createNotice(String dotColor, String title, String content, String timeText) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("dotColor", dotColor);
        map.put("title", title);
        map.put("content", content);
        map.put("timeText", timeText);
        return map;
    }

    private Map<String, Object> createParentAssignmentChild(int id, String name, String grade) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("grade", grade);
        return map;
    }

    private Map<String, Object> createParentAssignmentItem(int id, String title, String subject, String status,
                                                           String deadline, String type, boolean submitted,
                                                           String submittedAt, String feedback,
                                                           String content, String mySubmission) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("subject", subject);
        map.put("status", status);
        map.put("deadline", deadline);
        map.put("type", type);
        map.put("submitted", submitted);
        map.put("submittedAt", submittedAt);
        map.put("feedback", feedback);
        map.put("content", content);
        map.put("mySubmission", mySubmission);
        return map;
    }

    private Map<String, Object> createParentReportData(List<Map<String, Object>> data,
                                                       int completedLearning,
                                                       int submittedAssignments,
                                                       int learningHours,
                                                       String comment) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("data", data);
        map.put("completedLearning", completedLearning);
        map.put("submittedAssignments", submittedAssignments);
        map.put("learningHours", learningHours);
        map.put("comment", comment);

        int totalSubmitted = 0;
        int totalAll = 0;
        for (Map<String, Object> item : data) {
            totalSubmitted += (Integer) item.get("submitted");
            totalAll += (Integer) item.get("total");
        }
        int overallRate = totalAll > 0 ? Math.round((float) totalSubmitted * 100 / totalAll) : 0;
        map.put("overallRate", overallRate);

        return map;
    }

    private List<Map<String, Object>> createParentReportChart(Map<String, Object>... items) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> item : items) {
            list.add(item);
        }
        return list;
    }

    private Map<String, Object> createParentReportSubject(int id, String subject, int submitted, int total, String colorClass) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("subject", subject);
        map.put("submitted", submitted);
        map.put("total", total);
        map.put("colorClass", colorClass);
        map.put("submittedHeight", total > 0 ? Math.round((float) submitted * 100 / total) : 0);
        map.put("totalHeight", 100);
        return map;
    }

    private MemberVO getLoginUser(HttpSession session) {
        Object sessionUser = session.getAttribute("loginUser");
        if (sessionUser instanceof MemberVO) {
            return (MemberVO) sessionUser;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUser) {
            return ((CustomUser) principal).getMember();
        }

        if (principal instanceof MemberVO) {
            return (MemberVO) principal;
        }

        return null;
    }
}