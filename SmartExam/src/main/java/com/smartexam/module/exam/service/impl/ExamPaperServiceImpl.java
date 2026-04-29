package com.smartexam.module.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartexam.module.exam.dto.RandomPaperReq;
import com.smartexam.module.exam.dto.RandomPaperResp;
import com.smartexam.module.exam.service.ExamPaperService;
import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamPaperServiceImpl implements ExamPaperService {

    private final QuestionMapper questionMapper;

    @Override
    public RandomPaperResp generateRandomPaper(RandomPaperReq req) {
        List<Question> all = questionMapper.selectList(
                new LambdaQueryWrapper<Question>()
                        .eq(req.getSubjectId() != null, Question::getSubjectId, req.getSubjectId())
                        .eq(Question::getStatus, 1)
        );

        List<Question> single = filterByType(all, 1);
        List<Question> multi = filterByType(all, 2);
        List<Question> judge = filterByType(all, 3);

        List<Question> selected = new ArrayList<>();
        selected.addAll(pickRandom(single, req.getSingleChoiceCount()));
        selected.addAll(pickRandom(multi, req.getMultipleChoiceCount()));
        selected.addAll(pickRandom(judge, req.getJudgeCount()));

        int totalScore = selected.stream()
                .map(Question::getScore)
                .filter(java.util.Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        RandomPaperResp resp = new RandomPaperResp();
        resp.setTitle("随机试卷");
        resp.setTotalScore(totalScore);

        List<RandomPaperResp.QuestionItem> items = new ArrayList<>();
        for (Question q : selected) {
            RandomPaperResp.QuestionItem item = new RandomPaperResp.QuestionItem();
            item.setId(q.getId());
            item.setQuestionType(q.getQuestionType());
            item.setStem(q.getStem());
            item.setScore(q.getScore());
            item.setAnswer(q.getAnswer());
            if (q.getOptionsJson() != null && !q.getOptionsJson().isEmpty()) {
                try {
                    // very simple JSON array parse: ["A","B"] -> split
                    String json = q.getOptionsJson().trim();
                    json = json.substring(1, json.length() - 1); // remove [ ]
                    String[] parts = json.split(",");
                    List<String> opts = new ArrayList<>();
                    for (String p : parts) {
                        opts.add(p.trim().replaceAll("^\"|\"$", ""));
                    }
                    item.setOptions(opts);
                } catch (Exception ignore) {
                    item.setOptions(Collections.emptyList());
                }
            } else {
                item.setOptions(Collections.emptyList());
            }
            items.add(item);
        }
        resp.setQuestions(items);
        return resp;
    }

    private List<Question> filterByType(List<Question> list, Integer type) {
        List<Question> res = new ArrayList<>();
        for (Question q : list) {
            if (q.getQuestionType() != null && q.getQuestionType().equals(type)) {
                res.add(q);
            }
        }
        return res;
    }

    private List<Question> pickRandom(List<Question> source, Integer count) {
        if (count == null || count <= 0 || source.isEmpty()) {
            return Collections.emptyList();
        }
        List<Question> copy = new ArrayList<>(source);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(count, copy.size()));
    }
}

