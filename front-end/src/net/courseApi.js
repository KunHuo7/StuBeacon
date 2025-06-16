import { get, post, jsonpost, del } from './index';

// 获取所有学院信息
export function getAllColleges(success, failure) {
  return get('/api/course/colleges', null, success, failure);
}

// 根据ID获取学院信息
export function getCollegeById(id, success, failure) {
  return get(`/api/course/college/${id}`, null, success, failure);
}

// 获取所有专业信息
export function getAllMajors(success, failure) {
  return get('/api/course/majors', null, success, failure);
}

// 根据学院ID获取专业列表
export function getMajorsByCollegeId(collegeId, success, failure) {
  return get(`/api/course/majors/byCollege/${collegeId}`, null, success, failure);
}

// 根据ID获取专业信息
export function getMajorById(id, success, failure) {
  return get(`/api/course/major/${id}`, null, success, failure);
}

// 获取专业完整信息（包含图片）
export function getMajorFullInfo(id, success, failure) {
  return get(`/api/course/major/${id}/fullInfo`, null, success, failure);
}

// 根据专业ID获取课程
export function getCoursesByMajorId(majorId, success, failure) {
  return get(`/api/course/courses/byMajor/${majorId}`, null, success, failure);
}

// 根据专业ID和课程分类获取课程
export function getCoursesByMajorIdAndCategory(majorId, category, success, failure) {
  return get(`/api/course/courses/byMajor/${majorId}/category/${category}`, null, success, failure);
}

// 更新专业学分要求和主要课程内容
export function updateMajorContent(majorId, data, success, failure) {
  return jsonpost(`/api/course/major/${majorId}/content`, data, success, failure);
}

// 上传专业图片
export function uploadMajorImage(majorId, imageType, formData, success, failure) {
  return post(`/api/course/major/${majorId}/uploadImage`, formData, success, failure);
}

// 删除专业图片
export function deleteMajorImage(imageId, success, failure) {
  return del(`/api/course/majorImage/${imageId}`, null, success, failure);
} 