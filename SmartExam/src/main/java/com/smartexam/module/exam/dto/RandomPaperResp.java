package com.smartexam.module.exam.dto;

import lombok.Data;

import java.util.List;

@Data
public class RandomPaperResp {
    private String title;
    private Integer totalScore;
    private List<QuestionItem> questions;

    @Data
    public static class QuestionItem {
        private Long id;
        private Integer questionType;
        private String stem;
        private List<String> options;
        private String answer;
        private Integer score;
    }
}

