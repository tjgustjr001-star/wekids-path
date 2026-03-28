package com.spring.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupportVO {
    private int supportNo;      // 문의 번호 (PK)
    private int writerId;       // 작성자 ID
    private String category;    // 카테고리 (결제/수업/계정/기타)
    private String title;       // 제목
    private String content;     // 내용
    private String status;      // 상태 (대기중 / 답변완료)
    private Date createdAt;     // 작성일
    private Date updatedAt;		// 업데이트 = default는 작성일
}