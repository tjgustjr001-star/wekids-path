package com.spring.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.admin.AdminSupportDAO;
import com.spring.dto.SupportAnswerVO;
import com.spring.dto.SupportFileVO;
import com.spring.dto.SupportVO;

@Service
public class AdminSupportServiceImpl implements AdminSupportService {

    @Autowired
    private AdminSupportDAO adminSupportDAO;

    @Override
    public List<SupportVO> getSupportList() {
        return adminSupportDAO.selectAllSupports();
    }

    @Override
    public SupportVO getSupportByNo(int supportNo) {
        return adminSupportDAO.selectSupportByNo(supportNo);
    }

    @Override
    public SupportAnswerVO getAnswerBySupportNo(int supportNo) {
        return adminSupportDAO.selectAnswerBySupportNo(supportNo);
    }

    @Override
    public List<Map<String, Object>> selectWeeklyStats() {
        // Service는 직접 sqlSession을 부르지 않고 DAO에게 일을 시킵니다.
        return adminSupportDAO.selectWeeklyStats();
    }
    
    @Override
    @Transactional  // ← 답변 INSERT + 상태 UPDATE 둘 다 성공해야 커밋
    public void registerAnswer(int supportNo, int adminId, String answerContent) {
        // 1. support_answer 테이블에 답변 INSERT
        SupportAnswerVO answer = SupportAnswerVO.builder()
                .supportNo(supportNo)
                .memberId(adminId)
                .answerContent(answerContent)
                .build();

        adminSupportDAO.insertAnswer(answer);

        // 2. support 테이블 status → '답변완료' UPDATE
        adminSupportDAO.updateSupportStatus(supportNo);
    }
    
    
    @Override
    public List<SupportFileVO> getFileList(int supportNo) {
        return adminSupportDAO.selectFilesBySupportNo(supportNo);
    }
    
    @Override
    public int getPendingSupportCount() {
        return adminSupportDAO.selectPendingSupportCount();

	}
}