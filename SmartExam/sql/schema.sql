CREATE DATABASE IF NOT EXISTS smartexam
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

USE smartexam;

CREATE TABLE IF NOT EXISTS subject (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  subject_code VARCHAR(64) NOT NULL COMMENT '科目编码',
  subject_name VARCHAR(128) NOT NULL COMMENT '科目名称',
  description VARCHAR(512) DEFAULT NULL COMMENT '描述',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用 0停用',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY uk_subject_code (subject_code),
  KEY idx_subject_name (subject_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科目表';

CREATE TABLE IF NOT EXISTS question (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  subject_id BIGINT NOT NULL COMMENT '科目ID',
  question_type TINYINT NOT NULL COMMENT '题型:1单选 2多选 3判断 4填空 5简答',
  stem TEXT NOT NULL COMMENT '题干',
  options_json JSON DEFAULT NULL COMMENT '选项JSON',
  answer TEXT NOT NULL COMMENT '答案',
  analysis TEXT DEFAULT NULL COMMENT '解析',
  difficulty TINYINT NOT NULL DEFAULT 3 COMMENT '难度:1-5',
  score INT NOT NULL DEFAULT 1 COMMENT '分值',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用 0停用',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY idx_question_subject (subject_id),
  KEY idx_question_type (question_type),
  KEY idx_question_status (status),
  CONSTRAINT fk_question_subject FOREIGN KEY (subject_id) REFERENCES subject(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目表';

CREATE TABLE IF NOT EXISTS exam_paper (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  paper_code VARCHAR(64) NOT NULL COMMENT '试卷编码',
  paper_name VARCHAR(128) NOT NULL COMMENT '试卷名称',
  subject_id BIGINT NOT NULL COMMENT '科目ID',
  total_score INT NOT NULL DEFAULT 100 COMMENT '总分',
  duration_minutes INT NOT NULL DEFAULT 60 COMMENT '考试时长(分钟)',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用 0停用',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY uk_paper_code (paper_code),
  KEY idx_exam_paper_subject (subject_id),
  CONSTRAINT fk_exam_paper_subject FOREIGN KEY (subject_id) REFERENCES subject(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷表';

CREATE TABLE IF NOT EXISTS exam_paper_question (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  paper_id BIGINT NOT NULL COMMENT '试卷ID',
  question_id BIGINT NOT NULL COMMENT '题目ID',
  sort_no INT NOT NULL DEFAULT 1 COMMENT '排序',
  score INT NOT NULL DEFAULT 1 COMMENT '该题分值',
  UNIQUE KEY uk_paper_question (paper_id, question_id),
  KEY idx_epq_question (question_id),
  CONSTRAINT fk_epq_paper FOREIGN KEY (paper_id) REFERENCES exam_paper(id),
  CONSTRAINT fk_epq_question FOREIGN KEY (question_id) REFERENCES question(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷题目关联表';

CREATE TABLE IF NOT EXISTS exam_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  paper_id BIGINT NOT NULL COMMENT '试卷ID',
  user_id BIGINT NOT NULL COMMENT '考生ID',
  start_time DATETIME NOT NULL COMMENT '开始时间',
  submit_time DATETIME DEFAULT NULL COMMENT '交卷时间',
  objective_score INT NOT NULL DEFAULT 0 COMMENT '客观题得分',
  subjective_score INT NOT NULL DEFAULT 0 COMMENT '主观题得分',
  total_score INT NOT NULL DEFAULT 0 COMMENT '总分',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '状态:0进行中 1已交卷 2已判分',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY idx_record_user (user_id),
  KEY idx_record_paper (paper_id),
  KEY idx_record_status (status),
  CONSTRAINT fk_record_paper FOREIGN KEY (paper_id) REFERENCES exam_paper(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试记录表';
