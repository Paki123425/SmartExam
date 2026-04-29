package com.smartexam.module.question.service;

import com.smartexam.module.question.dto.QuestionCreateReq;
import com.smartexam.module.question.dto.QuestionQueryReq;
import com.smartexam.module.question.dto.QuestionStatsResp;
import com.smartexam.module.question.entity.Question;

import java.util.List;

public interface QuestionService {
    List<Question> listQuestions(QuestionQueryReq req);

    void createQuestion(QuestionCreateReq req);

    void updateQuestion(Long id, QuestionCreateReq req);

    void deleteQuestion(Long id);

    QuestionStatsResp getStats();
}
