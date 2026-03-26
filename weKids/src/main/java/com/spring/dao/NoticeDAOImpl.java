package com.spring.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.dto.AttachVO;
import com.spring.dto.NoticeVO;

@Repository
public class NoticeDAOImpl implements NoticeDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<NoticeVO> selectNoticeList(Map<String, Object> param) throws Exception {
        return sqlSession.selectList("Notice-Mapper.selectNoticeList", param);
    }

    @Override
    public List<NoticeVO> selectUnreadRequiredNoticeList(Map<String, Object> param) throws Exception {
        return sqlSession.selectList("Notice-Mapper.selectUnreadRequiredNoticeList", param);
    }

    @Override
    public NoticeVO selectNoticeDetail(Map<String, Object> param) throws Exception {
        return sqlSession.selectOne("Notice-Mapper.selectNoticeDetail", param);
    }

    @Override
    public int selectTeacherClassId(int memberId) throws Exception {
        Integer classId = sqlSession.selectOne("Notice-Mapper.selectTeacherClassId", memberId);
        return classId == null ? 0 : classId;
    }

    @Override
    public int selectNoticeSeqNext() throws Exception {
        return sqlSession.selectOne("Notice-Mapper.selectNoticeSeqNext");
    }

    @Override
    public void insertNotice(NoticeVO notice) throws Exception {
        sqlSession.insert("Notice-Mapper.insertNotice", notice);
    }

    @Override
    public void updateNotice(NoticeVO notice) throws Exception {
        sqlSession.update("Notice-Mapper.updateNotice", notice);
    }

    @Override
    public void deleteNotice(Map<String, Object> param) throws Exception {
        sqlSession.update("Notice-Mapper.deleteNotice", param);
    }

    @Override
    public void mergeConfirm(Map<String, Object> param) throws Exception {
        sqlSession.insert("Notice-Mapper.mergeConfirm", param);
    }

    @Override
    public void insertAttach(AttachVO attach) throws Exception {
        sqlSession.insert("Notice-Mapper.insertAttach", attach);
    }

    @Override
    public List<AttachVO> selectAttachList(int noticeId) throws Exception {
        return sqlSession.selectList("Notice-Mapper.selectAttachList", noticeId);
    }

    @Override
    public AttachVO selectAttachByAno(int ano) throws Exception {
        return sqlSession.selectOne("Notice-Mapper.selectAttachByAno", ano);
    }

    @Override
    public void deleteAttachByNoticeId(int noticeId) throws Exception {
        sqlSession.delete("Notice-Mapper.deleteAttachByNoticeId", noticeId);
    }
}
