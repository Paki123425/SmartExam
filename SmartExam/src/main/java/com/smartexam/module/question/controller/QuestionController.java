package com.smartexam.module.question.controller;

import com.smartexam.common.web.ApiResponse;
import com.smartexam.module.question.dto.QuestionCreateReq;
import com.smartexam.module.question.dto.QuestionQueryReq;
import com.smartexam.module.question.dto.QuestionStatsResp;
import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ApiResponse<List<Question>> list(@ModelAttribute QuestionQueryReq req) {
        return ApiResponse.ok(questionService.listQuestions(req));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody QuestionCreateReq req) {
        questionService.createQuestion(req);
        return ApiResponse.ok(null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody QuestionCreateReq req) {
        questionService.updateQuestion(id, req);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/stats")
    public ApiResponse<QuestionStatsResp> stats() {
        return ApiResponse.ok(questionService.getStats());
    }
}
