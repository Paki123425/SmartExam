package com.smartexam.module.question.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionStatsResp {
    private List<TypeStat> typeStats;
    private List<SubjectStat> subjectStats;

    @Data
    public static class TypeStat {
        private String name;
        private Integer value;
    }

    @Data
    public static class SubjectStat {
        private String name;
        private Integer count;
    }
}

