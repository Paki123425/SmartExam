package com.smartexam.module.exam.controller;

import com.smartexam.common.web.ApiResponse;
import com.smartexam.module.exam.dto.RandomPaperReq;
import com.smartexam.module.exam.dto.RandomPaperResp;
import com.smartexam.module.exam.service.ExamPaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exam/papers")
@RequiredArgsConstructor
public class ExamPaperController {

    private final ExamPaperService examPaperService;

    @PostMapping("/random")
    public ApiResponse<RandomPaperResp> random(@RequestBody RandomPaperReq req) {
        return ApiResponse.ok(examPaperService.generateRandomPaper(req));
    }
}

