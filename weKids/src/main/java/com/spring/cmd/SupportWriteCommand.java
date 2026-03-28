package com.spring.cmd;

import com.spring.dto.SupportVO;

public class SupportWriteCommand {
    
    private String category;    // 문의 유형
    private String title;       // 제목
    private String content;     // 내용
    
    // ── Getter & Setter ──────────────────────────────────
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    // ── Command를 VO로 변환 ──────────────────────────────
    public SupportVO toSupportVO(int writerId) {
        return SupportVO.builder()
            .writerId(writerId)
            .category(this.category)
            .title(this.title)
            .content(this.content)
            .status("대기중")  // 초기 상태
            .build();
    }
}