package com.smartexam.module.question.controller;

import com.smartexam.common.web.ApiResponse;
import com.smartexam.module.question.dto.SubjectSaveReq;
import com.smartexam.module.question.entity.Subject;
import com.smartexam.module.question.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectMapper subjectMapper;

    @GetMapping
    public ApiResponse<List<Subject>> list() {
        List<Subject> list = subjectMapper.selectList(
                new LambdaQueryWrapper<Subject>().orderByDesc(Subject::getId)
        );
        return ApiResponse.ok(list);
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody SubjectSaveReq req) {
        Subject subject = new Subject();
        subject.setSubjectCode(req.getSubjectCode());
        subject.setSubjectName(req.getSubjectName());
        subject.setDescription(req.getDescription());
        subject.setStatus(req.getStatus() == null ? 1 : req.getStatus());
        subjectMapper.insert(subject);
        return ApiResponse.ok(null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody SubjectSaveReq req) {
        Subject subject = subjectMapper.selectById(id);
        if (subject == null) {
            return ApiResponse.fail(404, "课程不存在");
        }
        subject.setSubjectCode(req.getSubjectCode());
        subject.setSubjectName(req.getSubjectName());
        subject.setDescription(req.getDescription());
        if (req.getStatus() != null) {
            subject.setStatus(req.getStatus());
        }
        subjectMapper.updateById(subject);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Subject subject = subjectMapper.selectById(id);
        if (subject == null) {
            return ApiResponse.fail(404, "课程不存在");
        }
        subject.setStatus(0);
        subjectMapper.updateById(subject);
        return ApiResponse.ok(null);
    }
}

