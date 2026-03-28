package com.spring.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupportAnswerVO {
    private int answerId;        // 답변 번호 (PK)
    private int supportNo;       // 문의 번호 (FK)
    private int memberId;        // 관리자/답변자 ID
    private String answerContent; // 답변 내용
    private Date createdAt;      // 생성일
    private Date updatedAt;      // 수정일
}