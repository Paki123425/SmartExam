<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const loading = ref(false)
const subjectList = ref([])
const tableData = ref([])
const keyword = ref('')
const filterType = ref(null)
const subjectChapter = ref([])

const dialogVisible = ref(false)
const editId = ref(null)
const form = ref({
  subjectId: null,
  questionType: 1,
  stem: '',
  options: ['', '', '', ''],
  answer: 'A',
  analysis: '',
  difficulty: 1,
  score: 2,
})

const subjectOptions = computed(() =>
  subjectList.value
    .filter((s) => s.status === 1)
    .map((s) => ({
      label: s.subjectName,
      value: s.id,
      // 当前系统还没有章节表，先用占位子节点保留“课程-章节”级联交互
      children: [{ label: '全部题目', value: 'ALL' }],
    })),
)

const subjectNameMap = computed(() => {
  const map = {}
  for (const s of subjectList.value) {
    map[s.id] = s.subjectName
  }
  return map
})

const fetchSubjects = async () => {
  const res = await request({ url: '/api/subjects', method: 'get' })
  subjectList.value = res.data || res || []
}

const fetchQuestions = async () => {
  loading.value = true
  try {
    const params = {
      keyword: keyword.value || undefined,
      questionType: filterType.value || undefined,
      subjectId: subjectChapter.value[0] || undefined,
    }
    const res = await request({
      url: '/api/questions',
      method: 'get',
      params,
    })
    tableData.value = res.data || res || []
  } finally {
    loading.value = false
  }
}

const typeText = (type) => {
  if (type === 1) return '单选题'
  if (type === 2) return '多选题'
  if (type === 3) return '判断题'
  return '-'
}

const resetForm = () => {
  editId.value = null
  form.value = {
    subjectId: null,
    questionType: 1,
    stem: '',
    options: ['', '', '', ''],
    answer: 'A',
    analysis: '',
    difficulty: 1,
    score: 2,
  }
}

const openCreate = () => {
  resetForm()
  dialogVisible.value = true
}

const openEdit = (row) => {
  editId.value = row.id
  let options = ['', '', '', '']
  if (row.optionsJson) {
    try {
      const parsed = JSON.parse(row.optionsJson)
      if (Array.isArray(parsed)) {
        options = [...parsed, '', '', '', ''].slice(0, 4)
      }
    } catch (e) {
      console.warn('选项JSON解析失败', e)
    }
  }
  form.value = {
    subjectId: row.subjectId,
    questionType: row.questionType,
    stem: row.stem || '',
    options,
    answer: row.answer || '',
    analysis: row.analysis || '',
    difficulty: row.difficulty || 1,
    score: row.score || 2,
  }
  dialogVisible.value = true
}

const saveQuestion = async () => {
  if (!form.value.subjectId) {
    ElMessage.warning('请选择课程')
    return
  }
  if (!form.value.stem) {
    ElMessage.warning('请输入题干')
    return
  }

  const payload = {
    subjectId: form.value.subjectId,
    questionType: form.value.questionType,
    stem: form.value.stem,
    optionsJson:
      form.value.questionType === 3 ? null : JSON.stringify(form.value.options.filter((x) => x)),
    answer: form.value.answer,
    analysis: form.value.analysis,
    difficulty: form.value.difficulty,
    score: form.value.score,
  }

  if (editId.value) {
    await request({
      url: `/api/questions/${editId.value}`,
      method: 'put',
      data: payload,
    })
    ElMessage.success('题目更新成功')
  } else {
    await request({
      url: '/api/questions',
      method: 'post',
      data: payload,
    })
    ElMessage.success('题目新增成功')
  }
  dialogVisible.value = false
  await fetchQuestions()
}

const removeQuestion = async (row) => {
  await ElMessageBox.confirm(`确认删除题目「${row.stem}」吗？`, '提示', { type: 'warning' })
  await request({
    url: `/api/questions/${row.id}`,
    method: 'delete',
  })
  ElMessage.success('题目已删除')
  await fetchQuestions()
}

const resetFilters = async () => {
  keyword.value = ''
  filterType.value = null
  subjectChapter.value = []
  await fetchQuestions()
}

onMounted(async () => {
  await fetchSubjects()
  await fetchQuestions()
})
</script>

<template>
  <el-card class="page-card" shadow="never">
    <template #header>
      <div class="card-header">
        <span>题库管理</span>
        <el-button type="primary" @click="openCreate">新增试题</el-button>
      </div>
    </template>

    <el-form inline class="filter-bar">
      <el-form-item label="课程 / 章节">
        <el-cascader
          v-model="subjectChapter"
          :options="subjectOptions"
          clearable
          filterable
          style="width: 220px"
          placeholder="请选择课程与章节"
        />
      </el-form-item>
      <el-form-item label="题型">
        <el-select v-model="filterType" clearable placeholder="全部题型" style="width: 120px">
          <el-option :value="1" label="单选题" />
          <el-option :value="2" label="多选题" />
          <el-option :value="3" label="判断题" />
        </el-select>
      </el-form-item>
      <el-form-item label="关键字">
        <el-input v-model="keyword" clearable placeholder="搜索题干关键字" style="width: 220px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchQuestions">筛选</el-button>
        <el-button @click="resetFilters">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="tableData"
      border
      stripe
      style="width: 100%"
      max-height="560"
    >
      <el-table-column type="index" label="#" width="60" />
      <el-table-column label="课程" min-width="140">
        <template #default="{ row }">
          {{ subjectNameMap[row.subjectId] || `课程${row.subjectId}` }}
        </template>
      </el-table-column>
      <el-table-column label="题型" width="100">
        <template #default="{ row }">
          {{ typeText(row.questionType) }}
        </template>
      </el-table-column>
      <el-table-column prop="stem" label="题目内容" min-width="260" show-overflow-tooltip />
      <el-table-column prop="difficulty" label="难度" width="100" />
      <el-table-column prop="score" label="分值" width="80" />
      <el-table-column prop="createdAt" label="创建时间" min-width="160" />
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="removeQuestion(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑试题' : '新增试题'" width="640px">
      <el-form label-width="90px" :model="form">
        <el-form-item label="课程">
          <el-select v-model="form.subjectId" placeholder="请选择课程">
            <el-option
              v-for="item in subjectList.filter((x) => x.status === 1)"
              :key="item.id"
              :label="item.subjectName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="题型">
          <el-select v-model="form.questionType">
            <el-option :value="1" label="单选题" />
            <el-option :value="2" label="多选题" />
            <el-option :value="3" label="判断题" />
          </el-select>
        </el-form-item>
        <el-form-item label="题目内容">
          <el-input
            v-model="form.stem"
            type="textarea"
            :rows="3"
            placeholder="请输入题干"
          />
        </el-form-item>
        <el-form-item v-if="form.questionType === 1 || form.questionType === 2" label="选项">
          <div style="width: 100%">
            <el-input
              v-for="(opt, index) in form.options"
              :key="index"
              v-model="form.options[index]"
              :placeholder="`选项 ${String.fromCharCode(65 + index)}`"
              style="margin-bottom: 8px"
            />
          </div>
        </el-form-item>
        <el-form-item label="答案">
          <el-input v-model="form.answer" placeholder="如：A / A,B / T / F" />
        </el-form-item>
        <el-form-item label="解析">
          <el-input
            v-model="form.analysis"
            type="textarea"
            :rows="2"
          />
        </el-form-item>
        <el-form-item label="难度">
          <el-input-number v-model="form.difficulty" :min="1" :max="5" />
        </el-form-item>
        <el-form-item label="分值">
          <el-input-number v-model="form.score" :min="1" :max="100" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveQuestion">保存</el-button>
      </template>
    </el-dialog>
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

.filter-bar {
  margin-bottom: 16px;
}
</style>

