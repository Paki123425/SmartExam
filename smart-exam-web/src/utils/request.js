import axios from 'axios'

// 基础 Axios 实例：指向后端网关
const service = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
})

// 请求拦截器（预留 Token 等扩展点）
service.interceptors.request.use(
  (config) => {
    // 这里可以统一挂载认证信息等
    return config
  },
  (error) => Promise.reject(error),
)

// 响应拦截器：统一抽取 data 并处理错误
service.interceptors.response.use(
  (response) => response.data,
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  },
)

export default service

