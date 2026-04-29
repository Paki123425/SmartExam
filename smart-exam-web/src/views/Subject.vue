<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const loading = ref(false)
const subjectList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentId = ref(null)

const form = ref({
  subjectCode: '',
  subjectName: '',
  description: '',
  status: 1,
})

const fetchSubjects = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/api/subjects', method: 'get' })
    subjectList.value = res.data || res || []
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  currentId.value = null
  isEdit.value = false
  form.value = {
    subjectCode: '',
    subjectName: '',
    description: '',
    status: 1,
  }
}

const openCreate = () => {
  resetForm()
  dialogVisible.value = true
}

const openEdit = (row) => {
  currentId.value = row.id
  isEdit.value = true
  form.value = {
    subjectCode: row.subjectCode || '',
    subjectName: row.subjectName || '',
    description: row.description || '',
    status: row.status ?? 1,
  }
  dialogVisible.value = true
}

const saveSubject = async () => {
  if (!form.value.subjectCode || !form.value.subjectName) {
    ElMessage.warning('课程编号和课程名称不能为空')
    return
  }
  const payload = { ...form.value }
  if (isEdit.value) {
    await request({
      url: `/api/subjects/${currentId.value}`,
      method: 'put',
      data: payload,
    })
    ElMessage.success('课程更新成功')
  } else {
    await request({
      url: '/api/subjects',
      method: 'post',
      data: payload,
    })
    ElMessage.success('课程新增成功')
  }
  dialogVisible.value = false
  await fetchSubjects()
}

const removeSubject = async (row) => {
  await ElMessageBox.confirm(`确认停用课程「${row.subjectName}」吗？`, '提示', {
    type: 'warning',
  })
  await request({
    url: `/api/subjects/${row.id}`,
    method: 'delete',
  })
  ElMessage.success('课程已停用')
  await fetchSubjects()
}

onMounted(fetchSubjects)
</script>

<template>
  <el-card class="page-card" shadow="never">
    <template #header>
      <div class="card-header">
        <span>课程管理</span>
        <el-button type="primary" @click="openCreate">新增课程</el-button>
      </div>
    </template>

    <el-table v-loading="loading" :data="subjectList" border stripe>
      <el-table-column type="index" label="#" width="60" />
      <el-table-column prop="subjectCode" label="课程编号" width="160" />
      <el-table-column prop="subjectName" label="课程名称" min-width="160" />
      <el-table-column prop="description" label="课程描述" min-width="220" show-overflow-tooltip />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" min-width="160" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="removeSubject(row)">停用</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑课程' : '新增课程'" width="520px">
    <el-form :model="form" label-width="90px">
      <el-form-item label="课程编号">
        <el-input v-model="form.subjectCode" />
      </el-form-item>
      <el-form-item label="课程名称">
        <el-input v-model="form.subjectName" />
      </el-form-item>
      <el-form-item label="课程描述">
        <el-input v-model="form.description" type="textarea" :rows="3" />
      </el-form-item>
      <el-form-item label="状态">
        <el-radio-group v-model="form.status">
          <el-radio :value="1">启用</el-radio>
          <el-radio :value="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="saveSubject">保存</el-button>
    </template>
  </el-dialog>
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
</style>

