package com.spring.dto;
 
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
 
/**
 * FAQ VO (Oracle FAQ 테이블 기준)
 */

@Builder
 @Getter
 @Setter
 @AllArgsConstructor
 @NoArgsConstructor
 @ToString
public class FaqVO {
 
    private int    faqId;      // FAQ_ID      NUMBER        FAQ 고유번호
    private int    adminId;    // ADMIN_ID    NUMBER        관리자 고유아이디
    private String category;  // CATEGORY    VARCHAR2(50)  카테고리
    private String question;  // QUESTION    VARCHAR2(500) 질문
    private String answer;    // ANSWER      CLOB          답변
    private Date   createdAt; // CREATED_AT  DATE          생성일
    private Date   updatedAt; // UPDATED_AT  DATE          수정일
}