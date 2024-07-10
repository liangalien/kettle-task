import axios from 'axios';
import { Message as message } from 'element-ui';
import Common from '@utils/common';

const service = axios.create({
  timeout: 600 * 1000
  // headers: {
  //     'Content-Type': 'application/json;charset=UTF-8'
  // }
});

var exceptMessage = (error) => {
  const response = error.response;
  message({
    type: 'error',
    message: '服务异常(' + response ? response.status : 'error' + '): ' + response ? response.statusText : error,
    showClose: true,
    duration: 0
  });
  if (error.response.status === 401) { // 跳转登录页
    const next = '?next=' + Common.getCurPath();
    // 根据返回的data判断是跳转本地登录页还是验证中心登录页
    window.location.href = error.response.data + (error.response.data == '/common/login' && next || encodeURIComponent(next));
  }
};

var errorMessage = (msg) => {
  message({
    type: 'error',
    message: msg,
    showClose: true,
    duration: 0
  });
};

service.error = (msg) => {
  errorMessage(msg);
};

service.except = (error) => {
  exceptMessage(error);
};

service.easyRequest = (url, method, params, data, success, error, except, final) => {
  service.request({
    url: url,
    method: method,
    data: data,
    params: params
  }).then((res) => {
    if (res.data && res.data.success) {
      if (success) success(res.data);
    } else {
      error ? error(res.data.message) : errorMessage(res.data.message);
    }
  }, except || exceptMessage).finally(() => {
    if (final) {
      final();
    }
  });
};

service.easyGet = (url, params, success, error = null, except = null, final = null) => {
  service.easyRequest(url, 'GET', params, null, success, error, except, final);
};

service.easyPost = (url, data, success, error = null, except = null, final = null) => {
  service.easyRequest(url, 'POST', null, data, success, error, except, final);
};

service.easyDelete = (url, data, success, error = null, except = null, final = null) => {
  service.easyRequest(url, 'DELETE', null, data, success, error, except, final);
};

service.easyPut = (url, data, success, error = null, except = null, final = null) => {
  service.easyRequest(url, 'PUT', null, data, success, error, except, final);
};

service.easyUpload = (url, filekey, fileValue, data = null, success, error = null, except = null, final = null) => {
  var formData = new FormData();
  formData.append(filekey, fileValue);
  if (data) Object.keys(data).map(key => formData.append(key, data[key]));

  service.request({
    url: url,
    method: 'POST',
    data: formData,
    headers: { 'Content-type': 'multipart/form-data' }
  }).then((res) => {
    if (res.data && res.data.success) {
      success(res.data);
    } else {
      error ? error(res.data.message) : errorMessage(res.data.message);
    }
    if (final) {
      final();
    }
  }, except || exceptMessage);
};

export default service;
