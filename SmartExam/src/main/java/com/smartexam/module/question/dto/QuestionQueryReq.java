package com.smartexam.module.question.dto;

import lombok.Data;

@Data
public class QuestionQueryReq {
    private Long subjectId;
    private Integer questionType;
    private Integer status;
    private String keyword;
}
