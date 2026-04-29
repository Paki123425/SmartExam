package com.smartexam.module.question.init;

import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.entity.Subject;
import com.smartexam.module.question.mapper.QuestionMapper;
import com.smartexam.module.question.mapper.SubjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionDataInitializer implements CommandLineRunner {

    private final SubjectMapper subjectMapper;
    private final QuestionMapper questionMapper;

    @Override
    public void run(String... args) {
        // 如果已经有科目数据，则认为已经初始化过
        if (subjectMapper.selectCount(null) > 0) {
            return;
        }

        // 1. 创建一个示例课程
        Subject subject = new Subject();
        subject.setSubjectCode("JAVA_BASIC");
        subject.setSubjectName("Java 基础");
        subject.setDescription("Java 程序设计基础课程示例数据");
        subject.setStatus(1);
        subjectMapper.insert(subject);

        Long subjectId = subject.getId();

        // 2. 插入几道示例题目
        Question q1 = new Question();
        q1.setSubjectId(subjectId);
        q1.setQuestionType(1);
        q1.setStem("下列哪一个关键字用于定义 Java 类？");
        q1.setOptionsJson("[\"class\",\"def\",\"function\",\"struct\"]");
        q1.setAnswer("A");
        q1.setAnalysis("Java 使用 class 关键字定义类。");
        q1.setDifficulty(1);
        q1.setScore(2);
        q1.setStatus(1);
        questionMapper.insert(q1);

        Question q2 = new Question();
        q2.setSubjectId(subjectId);
        q2.setQuestionType(3);
        q2.setStem("Java 源文件的扩展名是 .java。");
        q2.setAnswer("T");
        q2.setAnalysis("Java 源文件统一以 .java 作为扩展名。");
        q2.setDifficulty(1);
        q2.setScore(1);
        q2.setStatus(1);
        questionMapper.insert(q2);
    }
}

