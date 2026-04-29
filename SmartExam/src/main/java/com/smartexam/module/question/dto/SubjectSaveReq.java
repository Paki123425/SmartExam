package com.smartexam.module.question.dto;

import lombok.Data;

@Data
public class SubjectSaveReq {
    private String subjectCode;
    private String subjectName;
    private String description;
    private Integer status;
}

