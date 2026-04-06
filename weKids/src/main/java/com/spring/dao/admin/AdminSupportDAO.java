package com.spring.dao.admin;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.spring.dto.SupportAnswerVO;
import com.spring.dto.SupportFileVO;
import com.spring.dto.SupportVO;

public interface AdminSupportDAO {

    // ── 문의 전체 목록 조회 ──────────────────────────────
    List<SupportVO> selectAllSupports();

    // ── 문의 단건 조회 ───────────────────────────────────
    SupportVO selectSupportByNo(int supportNo);

    // ── 답변 조회 ────────────────────────────────────────
    SupportAnswerVO selectAnswerBySupportNo(int supportNo);

    // ── 답변 등록 ────────────────────────────────────────
    int insertAnswer(SupportAnswerVO answer);

    // ── 문의 상태 업데이트 (대기중/긴급 → 답변완료) ───────
    int updateSupportStatus(int supportNo);
    
    public List<Map<String, Object>> selectWeeklyStats();
 // 메서드 추가
    List<SupportFileVO> selectFilesBySupportNo(int supportNo);
    
    void deleteFaq(int faqId)throws SQLException;
    
    int selectPendingSupportCount();
    
}