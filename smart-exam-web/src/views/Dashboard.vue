<script setup>
import { onMounted, onBeforeUnmount, ref } from 'vue'
import * as echarts from 'echarts'
import request from '../utils/request'

const pieChartRef = ref(null)
const barChartRef = ref(null)

let pieInstance = null
let barInstance = null

const typeStats = ref([
  { name: '单选题', value: 0 },
  { name: '多选题', value: 0 },
  { name: '判断题', value: 0 },
])

const subjectStats = ref([])

const buildPieOption = () => ({
  tooltip: {
    trigger: 'item',
  },
  legend: {
    orient: 'vertical',
    left: 'left',
  },
  series: [
    {
      name: '题型占比',
      type: 'pie',
      radius: '65%',
      data: typeStats.value,
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)',
        },
      },
    },
  ],
})

const buildBarOption = () => ({
  tooltip: {
    trigger: 'axis',
  },
  xAxis: {
    type: 'category',
    data: subjectStats.value.map((s) => s.name),
    axisLabel: {
      interval: 0,
      rotate: 20,
    },
  },
  yAxis: {
    type: 'value',
    name: '题目数量',
  },
  series: [
    {
      name: '题量',
      type: 'bar',
      data: subjectStats.value.map((s) => s.count),
      itemStyle: {
        color: '#409EFF',
      },
      barMaxWidth: 40,
    },
  ],
})

const initCharts = () => {
  if (pieChartRef.value && !pieInstance) {
    pieInstance = echarts.init(pieChartRef.value)
  }
  if (barChartRef.value && !barInstance) {
    barInstance = echarts.init(barChartRef.value)
  }
  if (pieInstance) {
    pieInstance.setOption(buildPieOption())
  }
  if (barInstance) {
    barInstance.setOption(buildBarOption())
  }
}

const resizeHandler = () => {
  pieInstance && pieInstance.resize()
  barInstance && barInstance.resize()
}

const fetchStats = async () => {
  try {
    // 假设后端提供统计接口，你可以按实际调整路径和字段
    const res = await request({
      url: '/api/questions/stats',
      method: 'get',
    })
    const data = res.data || res || {}
    if (data.typeStats) {
      typeStats.value = data.typeStats
    }
    if (data.subjectStats) {
      subjectStats.value = data.subjectStats
    }
  } catch (e) {
    console.error('加载统计数据失败', e)
  } finally {
    initCharts()
  }
}

onMounted(async () => {
  await fetchStats()
  window.addEventListener('resize', resizeHandler)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeHandler)
  pieInstance && pieInstance.dispose()
  barInstance && barInstance.dispose()
})
</script>

<template>
  <el-card class="page-card" shadow="never">
    <template #header>
      <div class="card-header">
        <span>可视化大屏</span>
      </div>
    </template>

    <el-row :gutter="24">
      <el-col :xs="24" :md="12">
        <div class="chart-wrapper">
          <div class="chart-title">题型占比</div>
          <div ref="pieChartRef" class="chart-container" />
        </div>
      </el-col>
      <el-col :xs="24" :md="12">
        <div class="chart-wrapper">
          <div class="chart-title">各课程题量统计</div>
          <div ref="barChartRef" class="chart-container" />
        </div>
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

.chart-wrapper {
  background: #fff;
  border-radius: 8px;
  padding: 12px 16px 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.chart-title {
  font-size: 14px;
  margin-bottom: 8px;
}

.chart-container {
  width: 100%;
  height: 320px;
}
</style>

