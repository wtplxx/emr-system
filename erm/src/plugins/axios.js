import Vue from 'vue'
import axios from "axios";

const Plugin = axios.create({
  baseURL: 'http://localhost:8181',
  timeout: 10000
});

// 请求拦截器：自动附加 Token
Plugin.interceptors.request.use(
  function(config) {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
  },
  function(error) {
    return Promise.reject(error);
  }
);

// 响应拦截器：统一处理 401
Plugin.interceptors.response.use(
  function(response) {
    return response;
  },
  function(error) {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

Plugin.install = function(Vue, options) {
  Vue.axios = Plugin;
  window.axios = Plugin;
  Object.defineProperties(Vue.prototype, {
    axios: { get() { return Plugin; } },
    $axios: { get() { return Plugin; } }
  });
};

Vue.use(Plugin)
export default Plugin;
