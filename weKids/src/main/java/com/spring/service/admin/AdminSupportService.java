package com.spring.service.admin;

import java.util.List;
import java.util.Map;

import com.spring.dto.SupportAnswerVO;
import com.spring.dto.SupportFileVO;
import com.spring.dto.SupportVO;

public interface AdminSupportService {

    // ── 전체 문의 목록 ───────────────────────────────────
    List<SupportVO> getSupportList();

    // ── 문의 단건 조회 ───────────────────────────────────
    SupportVO getSupportByNo(int supportNo);

    // ── 답변 조회 ────────────────────────────────────────
    SupportAnswerVO getAnswerBySupportNo(int supportNo);

    // ── 답변 등록 + 상태 업데이트 (트랜잭션) ─────────────
    void registerAnswer(int supportNo, int adminId, String answerContent);
    
    //주간 차트용 통계 데이터 가져오기 메서드 추가
    List<Map<String, Object>> selectWeeklyStats();
    
    List<SupportFileVO> getFileList(int supportNo);

    int getPendingSupportCount();
}
