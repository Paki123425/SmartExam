package com.smartexam.module.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exam_paper")
public class ExamPaper {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String paperCode;
    private String paperName;
    private Long subjectId;
    private Integer totalScore;
    private Integer durationMinutes;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
