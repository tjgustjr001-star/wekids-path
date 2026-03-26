package com.spring.dao;

import java.util.List;
import java.util.Map;

import com.spring.dto.AttachVO;
import com.spring.dto.NoticeVO;

public interface NoticeDAO {
    List<NoticeVO> selectNoticeList(Map<String, Object> param) throws Exception;
    List<NoticeVO> selectUnreadRequiredNoticeList(Map<String, Object> param) throws Exception;
    NoticeVO selectNoticeDetail(Map<String, Object> param) throws Exception;
    int selectTeacherClassId(int memberId) throws Exception;
    int selectNoticeSeqNext() throws Exception;
    void insertNotice(NoticeVO notice) throws Exception;
    void updateNotice(NoticeVO notice) throws Exception;
    void deleteNotice(Map<String, Object> param) throws Exception;
    void mergeConfirm(Map<String, Object> param) throws Exception;
    void insertAttach(AttachVO attach) throws Exception;
    List<AttachVO> selectAttachList(int noticeId) throws Exception;
    AttachVO selectAttachByAno(int ano) throws Exception;
    void deleteAttachByNoticeId(int noticeId) throws Exception;
}
