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
public class SupportFileVO {
    private int fileId;
    private int supportNo;
    private String fileName;
    private String fileOriName;
    private String filePath;
    private long fileSize;
    private Date createdAt;
}
