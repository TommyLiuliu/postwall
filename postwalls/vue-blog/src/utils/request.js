import axios from "axios";
import router from '@/router'
import store from '@/store'

const request = axios.create({
  baseURL: "/api/",
  timeout: 10000,
});

request.interceptors.request.use((config) => {
  if (store.getters.accessToken) {
    config.headers['Authorization'] = store.getters.accessToken
  }
  return config;
})

request.interceptors.response.use((response) => {
  const { data } = response
  if (data.code == 401) {
    router.push('/401')
  }
  return response.data;
});

export default request;
