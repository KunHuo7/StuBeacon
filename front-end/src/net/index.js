//net/index.js
import axios from "axios";
import { ElMessage } from "element-plus";
function getAuthToken() {
    return localStorage.getItem('authToken') || '';
}
const defaultError = () => ElMessage.error('发生错误，请联系管理员。')
const defaultFailure = (message) => ElMessage.warning(message)

function post(url, data, success, failure = defaultFailure, error = defaultError) {
    axios.post(url, data, {
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            "Authorization": getAuthToken()
        },
        withCredentials: true
    }).then(({data: responseData}) => {
        if (responseData.success)
            success(responseData.message,responseData.data)
        else
            failure(responseData.message)
    }).catch(error)
}
function jsonpost(url, data, success, failure = defaultFailure, error = defaultError) {
    axios.post(url, data, {
        headers: {
            "Content-Type": "application/json",
            "Authorization": getAuthToken()
        },
        withCredentials: true
    }).then(({data: responseData}) => {
        if (responseData.success)
            success(responseData.message,responseData.data)
        else
            failure(responseData.message)
    }).catch(error)
}

function get(url, data = null, success, failure = defaultFailure, error = defaultError) {
    const config = {
        withCredentials: true,
        params: data,
        headers:{
            "Authorization": getAuthToken()
        }
    };

    axios.get(url, config)
        .then(({data: responseData}) => {
            if (responseData.success)
                success(responseData.message,responseData.data)
            else
                failure(responseData.message)
        })
        .catch(error)
}

// 添加DELETE请求函数
function del(url, data = null, success, failure = defaultFailure, error = defaultError) {
    const config = {
        withCredentials: true,
        params: data,
        headers: {
            "Authorization": getAuthToken()
        }
    };

    axios.delete(url, config)
        .then(({data: responseData}) => {
            if (responseData.success)
                success(responseData.message, responseData.data)
            else
                failure(responseData.message)
        })
        .catch(error)
}

// 添加PUT请求函数
function put(url, data, success, failure = defaultFailure, error = defaultError) {
    axios.put(url, data, {
        headers: {
            "Content-Type": "application/json",
            "Authorization": getAuthToken()
        },
        withCredentials: true
    }).then(({data: responseData}) => {
        if (responseData.success)
            success(responseData.message, responseData.data)
        else
            failure(responseData.message)
    }).catch(error)
}

// 自定义请求函数替代导入的request
function request(config) {
    return axios({
        ...config,
        headers: {
            ...config.headers,
            "Authorization": getAuthToken()
        },
        withCredentials: true
    });
}

// 个人信息相关接口
export const getPersonalInfo = () => {
  return request({
    url: '/api/personal/get',
    method: 'get'
  });
};

export const addPersonalInfo = (data) => {
  return request({
    url: '/api/personal/add',
    method: 'post',
    data
  });
};

export const updatePersonalInfo = (data) => {
  return request({
    url: '/api/personal/update',
    method: 'put',
    data
  });
};

export { get, post, jsonpost, del, put, request }