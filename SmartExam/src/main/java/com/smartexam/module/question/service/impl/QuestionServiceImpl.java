package com.smartexam.module.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartexam.module.question.dto.QuestionCreateReq;
import com.smartexam.module.question.dto.QuestionQueryReq;
import com.smartexam.module.question.dto.QuestionStatsResp;
import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.entity.Subject;
import com.smartexam.module.question.mapper.QuestionMapper;
import com.smartexam.module.question.mapper.SubjectMapper;
import com.smartexam.module.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;
    private final SubjectMapper subjectMapper;

    @Override
    public List<Question> listQuestions(QuestionQueryReq req) {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(req.getSubjectId() != null, Question::getSubjectId, req.getSubjectId());
        wrapper.eq(req.getQuestionType() != null, Question::getQuestionType, req.getQuestionType());
        wrapper.eq(Question::getStatus, req.getStatus() == null ? 1 : req.getStatus());
        wrapper.like(StringUtils.hasText(req.getKeyword()), Question::getStem, req.getKeyword());
        wrapper.orderByDesc(Question::getId);
        return questionMapper.selectList(wrapper);
    }

    @Override
    public void createQuestion(QuestionCreateReq req) {
        Question q = new Question();
        q.setSubjectId(req.getSubjectId());
        q.setQuestionType(req.getQuestionType());
        q.setStem(req.getStem());
        q.setOptionsJson(req.getOptionsJson());
        q.setAnswer(req.getAnswer());
        q.setAnalysis(req.getAnalysis());
        q.setDifficulty(req.getDifficulty());
        q.setScore(req.getScore());
        q.setStatus(1);
        questionMapper.insert(q);
    }

    @Override
    public void updateQuestion(Long id, QuestionCreateReq req) {
        Question exists = questionMapper.selectById(id);
        if (exists == null) {
            return;
        }
        exists.setSubjectId(req.getSubjectId());
        exists.setQuestionType(req.getQuestionType());
        exists.setStem(req.getStem());
        exists.setOptionsJson(req.getOptionsJson());
        exists.setAnswer(req.getAnswer());
        exists.setAnalysis(req.getAnalysis());
        exists.setDifficulty(req.getDifficulty());
        exists.setScore(req.getScore());
        questionMapper.updateById(exists);
    }

    @Override
    public void deleteQuestion(Long id) {
        Question exists = questionMapper.selectById(id);
        if (exists == null) {
            return;
        }
        exists.setStatus(0);
        questionMapper.updateById(exists);
    }

    @Override
    public QuestionStatsResp getStats() {
        List<Question> questions = questionMapper.selectList(
                new LambdaQueryWrapper<Question>().eq(Question::getStatus, 1)
        );
        List<Subject> subjects = subjectMapper.selectList(
                new LambdaQueryWrapper<Subject>().eq(Subject::getStatus, 1)
        );

        Map<Long, String> subjectNameMap = new HashMap<>();
        for (Subject s : subjects) {
            subjectNameMap.put(s.getId(), s.getSubjectName());
        }

        int single = 0;
        int multiple = 0;
        int judge = 0;
        Map<Long, Integer> subjectCountMap = new HashMap<>();

        for (Question q : questions) {
            if (q.getQuestionType() != null) {
                if (q.getQuestionType() == 1) single++;
                else if (q.getQuestionType() == 2) multiple++;
                else if (q.getQuestionType() == 3) judge++;
            }
            if (q.getSubjectId() != null) {
                subjectCountMap.put(q.getSubjectId(), subjectCountMap.getOrDefault(q.getSubjectId(), 0) + 1);
            }
        }

        List<QuestionStatsResp.TypeStat> typeStats = new ArrayList<>();
        typeStats.add(buildType("单选题", single));
        typeStats.add(buildType("多选题", multiple));
        typeStats.add(buildType("判断题", judge));

        List<QuestionStatsResp.SubjectStat> subjectStats = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : subjectCountMap.entrySet()) {
            QuestionStatsResp.SubjectStat stat = new QuestionStatsResp.SubjectStat();
            stat.setName(subjectNameMap.getOrDefault(entry.getKey(), "未知课程"));
            stat.setCount(entry.getValue());
            subjectStats.add(stat);
        }

        QuestionStatsResp resp = new QuestionStatsResp();
        resp.setTypeStats(typeStats);
        resp.setSubjectStats(subjectStats);
        return resp;
    }

    private QuestionStatsResp.TypeStat buildType(String name, Integer value) {
        QuestionStatsResp.TypeStat typeStat = new QuestionStatsResp.TypeStat();
        typeStat.setName(name);
        typeStat.setValue(value);
        return typeStat;
    }
}
