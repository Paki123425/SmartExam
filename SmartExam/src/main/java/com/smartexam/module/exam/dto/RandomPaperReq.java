package com.smartexam.module.exam.dto;

import lombok.Data;

@Data
public class RandomPaperReq {
    private Long subjectId;
    private Integer singleChoiceCount;
    private Integer multipleChoiceCount;
    private Integer judgeCount;
}

