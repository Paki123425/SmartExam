<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const loading = ref(false)

const ruleForm = ref({
  subjectId: null,
  singleChoiceCount: 10,
  multipleChoiceCount: 0,
  judgeCount: 0,
})

const subjectOptions = ref([])

const resultPaper = ref(null)

const loadSubjects = async () => {
  try {
    const res = await request({
      url: '/api/subjects',
      method: 'get',
    })
    subjectOptions.value = (res.data || res || []).map((subject) => ({
      label: subject.subjectName,
      value: subject.id,
    }))
  } catch (e) {
    console.error('加载课程失败', e)
  }
}

loadSubjects()

const handleGeneratePaper = async () => {
  if (!ruleForm.value.subjectId) {
    ElMessage.warning('请先选择课程')
    return
  }
  loading.value = true
  try {
    // 根据你的后端实际路由调整此地址
    const res = await request({
      url: '/api/exam/papers/random',
      method: 'post',
      data: {
        subjectId: ruleForm.value.subjectId,
        singleChoiceCount: ruleForm.value.singleChoiceCount,
        multipleChoiceCount: ruleForm.value.multipleChoiceCount,
        judgeCount: ruleForm.value.judgeCount,
      },
    })
    resultPaper.value = res.data || res || null
    ElMessage.success('随机组卷成功')
  } catch (e) {
    console.error('随机组卷失败', e)
    ElMessage.error('组卷失败，请检查后端接口')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <el-card class="page-card" shadow="never">
    <template #header>
      <div class="card-header">
        <span>组卷中心</span>
      </div>
    </template>

    <el-row :gutter="24">
      <el-col :span="10">
        <el-form
          label-width="120px"
          :model="ruleForm"
          class="rule-form"
        >
          <el-form-item label="课程">
            <el-select
              v-model="ruleForm.subjectId"
              filterable
              clearable
              style="width: 260px"
              placeholder="请选择课程"
            >
              <el-option
                v-for="item in subjectOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="单选题数量">
            <el-input-number v-model="ruleForm.singleChoiceCount" :min="0" :max="100" />
          </el-form-item>
          <el-form-item label="多选题数量">
            <el-input-number v-model="ruleForm.multipleChoiceCount" :min="0" :max="100" />
          </el-form-item>
          <el-form-item label="判断题数量">
            <el-input-number v-model="ruleForm.judgeCount" :min="0" :max="100" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="loading" @click="handleGeneratePaper">
              随机组卷
            </el-button>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="14">
        <el-card v-if="resultPaper" shadow="never" class="result-card">
          <template #header>
            <div class="result-header">
              <span>试卷预览</span>
            </div>
          </template>
          <div class="result-body">
            <p><strong>试卷名称：</strong>{{ resultPaper.title || resultPaper.name }}</p>
            <p><strong>总分：</strong>{{ resultPaper.totalScore }}</p>
            <p><strong>题目数量：</strong>{{ (resultPaper.questions || []).length }}</p>
            <el-divider />
            <el-scrollbar height="360px">
              <div
                v-for="(q, index) in resultPaper.questions || []"
                :key="q.id || index"
                class="question-item"
              >
                <div class="question-title">
                  {{ index + 1 }}. {{ q.stem }}
                </div>
                <div v-if="q.options && q.options.length" class="question-options">
                  <div
                    v-for="(opt, optIndex) in q.options"
                    :key="optIndex"
                    class="question-option"
                  >
                    {{ String.fromCharCode(65 + optIndex) }}. {{ opt }}
                  </div>
                </div>
              </div>
            </el-scrollbar>
          </div>
        </el-card>
        <el-empty
          v-else
          description="请先配置抽题规则并点击“随机组卷”生成试卷"
        />
      </el-col>
    </el-row>
  </el-card>
</template>

<style scoped>
.page-card {
  border-radius: 10px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}

.rule-form {
  padding-right: 16px;
}

.result-card {
  border-radius: 8px;
}

.result-header {
  font-weight: 600;
}

.result-body {
  font-size: 14px;
}

.question-item {
  margin-bottom: 12px;
}

.question-title {
  margin-bottom: 4px;
}

.question-options {
  padding-left: 12px;
  color: #606266;
}

.question-option {
  line-height: 1.6;
}
</style>

