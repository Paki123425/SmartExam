package com.smartexam.module.question.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("question")
public class Question {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long subjectId;
    private Integer questionType;
    private String stem;
    private String optionsJson;
    private String answer;
    private String analysis;
    private Integer difficulty;
    private Integer score;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
