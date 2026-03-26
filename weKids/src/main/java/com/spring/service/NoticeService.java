package com.spring.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.dto.AttachVO;
import com.spring.dto.MemberVO;
import com.spring.dto.NoticeVO;

public interface NoticeService {
    List<NoticeVO> getNoticeList(int classId, MemberVO loginUser) throws Exception;
    List<NoticeVO> getUnreadRequiredNoticeList(int classId, MemberVO loginUser) throws Exception;
    NoticeVO getNoticeDetail(int classId, int noticeId, MemberVO loginUser) throws Exception;
    void registNotice(int classId, NoticeVO notice, MultipartFile[] uploadFiles, MemberVO loginUser, String saveDir) throws Exception;
    void modifyNotice(int classId, NoticeVO notice, MultipartFile[] uploadFiles, MemberVO loginUser, String saveDir) throws Exception;
    void removeNotice(int classId, int noticeId, MemberVO loginUser) throws Exception;
    void confirmNotice(int classId, int noticeId, MemberVO loginUser) throws Exception;
    AttachVO getAttach(int ano) throws Exception;
}