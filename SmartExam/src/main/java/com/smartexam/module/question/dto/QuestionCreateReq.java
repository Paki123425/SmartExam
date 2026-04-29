package com.smartexam.module.question.dto;

import lombok.Data;

@Data
public class QuestionCreateReq {
    private Long subjectId;
    /**
     * 题型：例如 1=单选，2=多选，3=判断
     */
    private Integer questionType;
    /**
     * 题干
     */
    private String stem;
    /**
     * 选项 JSON（前端会传 JSON 字符串或由后端组装）
     */
    private String optionsJson;
    /**
     * 正确答案（如 "A" 或 "A,B"）
     */
    private String answer;
    /**
     * 解析
     */
    private String analysis;
    /**
     * 难度（1-5）
     */
    private Integer difficulty;
    /**
     * 分值
     */
    private Integer score;
}

