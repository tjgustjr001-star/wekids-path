package com.spring.service;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.spring.dao.NoticeDAO;
import com.spring.dto.AttachVO;
import com.spring.dto.MemberVO;
import com.spring.dto.NoticeVO;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeDAO noticeDAO;

    @Override
    public List<NoticeVO> getNoticeList(int classId, MemberVO loginUser) throws Exception {
        return noticeDAO.selectNoticeList(makeRoleParam(classId, loginUser));
    }

    @Override
    public List<NoticeVO> getUnreadRequiredNoticeList(int classId, MemberVO loginUser) throws Exception {
        return noticeDAO.selectUnreadRequiredNoticeList(makeRoleParam(classId, loginUser));
    }

    @Override
    public NoticeVO getNoticeDetail(int classId, int noticeId, MemberVO loginUser) throws Exception {
        Map<String, Object> param = makeRoleParam(classId, loginUser);
        param.put("noticeId", noticeId);

        NoticeVO notice = noticeDAO.selectNoticeDetail(param);
        if (notice != null) {
            notice.setAttachList(noticeDAO.selectAttachList(noticeId));
        }
        return notice;
    }

    @Override
    public void registNotice(int classId, NoticeVO notice, MultipartFile[] uploadFiles,
                             MemberVO loginUser, String saveDir) throws Exception {

        notice.setNoticeId(noticeDAO.selectNoticeSeqNext());
        notice.setMemberId(loginUser.getMember_id());
        notice.setClassId(classId);
        notice.setStatus("Y");

        if (notice.getConfirmYn() != 1) {
            notice.setConfirmYn(0);
        }
        if (notice.getTarget() == null || notice.getTarget().isBlank()) {
            notice.setTarget("ALL");
        }

        noticeDAO.insertNotice(notice);
        saveAttachments(notice.getNoticeId(), loginUser, uploadFiles, saveDir);
    }

    @Override
    public void modifyNotice(int classId, NoticeVO notice, MultipartFile[] uploadFiles,
                             MemberVO loginUser, String saveDir) throws Exception {

        NoticeVO old = getNoticeDetail(classId, notice.getNoticeId(), loginUser);
        if (old == null || old.getMemberId() != loginUser.getMember_id()) {
            throw new IllegalAccessException("수정 권한이 없습니다.");
        }

        notice.setMemberId(loginUser.getMember_id());
        notice.setClassId(classId);
        notice.setStatus(old.getStatus());

        if (notice.getConfirmYn() != 1) {
            notice.setConfirmYn(0);
        }
        if (notice.getTarget() == null || notice.getTarget().isBlank()) {
            notice.setTarget("ALL");
        }

        noticeDAO.updateNotice(notice);

        boolean hasNewFile = false;
        if (uploadFiles != null) {
            for (MultipartFile file : uploadFiles) {
                if (file != null && !file.isEmpty()) {
                    hasNewFile = true;
                    break;
                }
            }
        }

        if (hasNewFile) {
            List<AttachVO> oldFiles = noticeDAO.selectAttachList(notice.getNoticeId());
            for (AttachVO attach : oldFiles) {
                File physical = new File(attach.getUploadPath(), attach.getFileName());
                if (physical.exists()) {
                    physical.delete();
                }
            }
            noticeDAO.deleteAttachByNoticeId(notice.getNoticeId());
            saveAttachments(notice.getNoticeId(), loginUser, uploadFiles, saveDir);
        }
    }

    @Override
    public void removeNotice(int classId, int noticeId, MemberVO loginUser) throws Exception {
        NoticeVO old = getNoticeDetail(classId, noticeId, loginUser);
        if (old == null || old.getMemberId() != loginUser.getMember_id()) {
            throw new IllegalAccessException("삭제 권한이 없습니다.");
        }

        Map<String, Object> param = new HashMap<>();
        param.put("noticeId", noticeId);
        param.put("classId", classId);
        noticeDAO.deleteNotice(param);
    }

    @Override
    public void confirmNotice(int classId, int noticeId, MemberVO loginUser) throws Exception {
        NoticeVO notice = getNoticeDetail(classId, noticeId, loginUser);
        if (notice == null) {
            throw new IllegalAccessException("해당 클래스의 가정통신문이 아닙니다.");
        }

        Map<String, Object> param = new HashMap<>();
        param.put("noticeId", noticeId);
        param.put("memberId", loginUser.getMember_id());
        noticeDAO.mergeConfirm(param);
    }

    @Override
    public AttachVO getAttach(int ano) throws Exception {
        return noticeDAO.selectAttachByAno(ano);
    }

    private void saveAttachments(int noticeId, MemberVO loginUser, MultipartFile[] uploadFiles, String saveDir) throws Exception {
        if (uploadFiles == null || uploadFiles.length == 0) return;

        File dir = new File(saveDir);
        if (!dir.exists()) dir.mkdirs();

        for (MultipartFile file : uploadFiles) {
            if (file == null || file.isEmpty()) continue;

            String original = StringUtils.cleanPath(file.getOriginalFilename());
            String savedName = UUID.randomUUID() + "$$" + original;
            File saveFile = new File(dir, savedName);
            file.transferTo(saveFile);

            AttachVO attach = new AttachVO();
            attach.setPno(noticeId);
            attach.setUploadPath(dir.getAbsolutePath());
            attach.setFileName(savedName);
            attach.setFileType(file.getContentType());
            attach.setAttacher(String.valueOf(loginUser.getMember_id()));
            noticeDAO.insertAttach(attach);
        }
    }

    @SuppressWarnings("unused")
    private String decodeFileName(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private Map<String, Object> makeRoleParam(int classId, MemberVO loginUser) {
        Map<String, Object> param = new HashMap<>();
        param.put("classId", classId);
        param.put("memberId", loginUser.getMember_id());
        param.put("roleCode", loginUser.getRole_code());
        return param;
    }
}