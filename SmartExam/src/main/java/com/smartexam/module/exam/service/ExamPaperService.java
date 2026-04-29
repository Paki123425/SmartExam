package com.smartexam.module.exam.service;

import com.smartexam.module.exam.dto.RandomPaperReq;
import com.smartexam.module.exam.dto.RandomPaperResp;

public interface ExamPaperService {

    RandomPaperResp generateRandomPaper(RandomPaperReq req);
}

