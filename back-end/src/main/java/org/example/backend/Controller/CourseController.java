package org.example.backend.Controller;

import lombok.extern.slf4j.Slf4j;
import org.example.backend.Entity.pojo.College;
import org.example.backend.Entity.pojo.Course;
import org.example.backend.Entity.pojo.Major;
import org.example.backend.Entity.pojo.MajorImage;
import org.example.backend.Entity.pojo.RestBean;
import org.example.backend.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程信息控制器
 */
@RestController
@RequestMapping("/api/course")
@Slf4j
public class CourseController {

    @Autowired
    private CourseService courseService;
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    // 允许的图片类型
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/bmp", "image/webp"
    );
    
    // 文件大小限制 (10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    
    /**
     * 获取所有学院信息
     */
    @GetMapping("/colleges")
    public RestBean<List<College>> getAllColleges() {
        try {
            List<College> colleges = courseService.getAllColleges();
            return RestBean.success("获取学院列表成功", colleges);
        } catch (Exception e) {
            log.error("获取学院列表失败", e);
            return RestBean.failure(500, "获取学院列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取学院信息
     */
    @GetMapping("/college/{id}")
    public RestBean<College> getCollegeById(@PathVariable Integer id) {
        try {
            College college = courseService.getCollegeById(id);
            if (college == null) {
                return RestBean.failure(404, "学院不存在");
            }
            return RestBean.success("获取学院信息成功", college);
        } catch (Exception e) {
            log.error("获取学院信息失败", e);
            return RestBean.failure(500, "获取学院信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 添加学院
     */
    @PostMapping("/college")
    public RestBean<College> addCollege(@RequestBody College college) {
        try {
            College addedCollege = courseService.addCollege(college);
            return RestBean.success("添加学院成功", addedCollege);
        } catch (Exception e) {
            log.error("添加学院失败", e);
            return RestBean.failure(500, "添加学院失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新学院信息
     */
    @PutMapping("/college")
    public RestBean<College> updateCollege(@RequestBody College college) {
        try {
            College existingCollege = courseService.getCollegeById(college.getId());
            if (existingCollege == null) {
                return RestBean.failure(404, "学院不存在");
            }
            College updatedCollege = courseService.updateCollege(college);
            return RestBean.success("更新学院成功", updatedCollege);
        } catch (Exception e) {
            log.error("更新学院失败", e);
            return RestBean.failure(500, "更新学院失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除学院
     */
    @DeleteMapping("/college/{id}")
    public RestBean<Boolean> deleteCollege(@PathVariable Integer id) {
        try {
            College existingCollege = courseService.getCollegeById(id);
            if (existingCollege == null) {
                return RestBean.failure(404, "学院不存在");
            }
            boolean result = courseService.deleteCollege(id);
            return RestBean.success("删除学院成功", result);
        } catch (Exception e) {
            log.error("删除学院失败", e);
            return RestBean.failure(500, "删除学院失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有专业信息
     */
    @GetMapping("/majors")
    public RestBean<List<Major>> getAllMajors() {
        try {
            List<Major> majors = courseService.getAllMajors();
            return RestBean.success("获取专业列表成功", majors);
        } catch (Exception e) {
            log.error("获取专业列表失败", e);
            return RestBean.failure(500, "获取专业列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据学院ID获取专业列表
     */
    @GetMapping("/majors/byCollege/{collegeId}")
    public RestBean<List<Major>> getMajorsByCollegeId(@PathVariable Integer collegeId) {
        try {
            List<Major> majors = courseService.getMajorsByCollegeId(collegeId);
            return RestBean.success("获取专业列表成功", majors);
        } catch (Exception e) {
            log.error("获取专业列表失败", e);
            return RestBean.failure(500, "获取专业列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取专业信息
     */
    @GetMapping("/major/{id}")
    public RestBean<Major> getMajorById(@PathVariable Integer id) {
        try {
            Major major = courseService.getMajorById(id);
            if (major == null) {
                return RestBean.failure(404, "专业不存在");
            }
            return RestBean.success("获取专业信息成功", major);
        } catch (Exception e) {
            log.error("获取专业信息失败", e);
            return RestBean.failure(500, "获取专业信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取专业完整信息（包含图片）
     */
    @GetMapping("/major/{id}/fullInfo")
    public RestBean<Map<String, Object>> getMajorFullInfo(@PathVariable Integer id) {
        try {
            Map<String, Object> result = courseService.getMajorFullInfo(id);
            if ("error".equals(result.get("status"))) {
                return RestBean.failure(404, (String) result.get("message"));
            }
            return RestBean.success("获取专业完整信息成功", result);
        } catch (Exception e) {
            log.error("获取专业完整信息失败", e);
            return RestBean.failure(500, "获取专业完整信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 添加专业
     */
    @PostMapping("/major")
    public RestBean<Major> addMajor(@RequestBody Major major) {
        try {
            // 校验学院是否存在
            College college = courseService.getCollegeById(major.getCollegeId());
            if (college == null) {
                return RestBean.failure(400, "所选学院不存在");
            }
            
            Major addedMajor = courseService.addMajor(major);
            return RestBean.success("添加专业成功", addedMajor);
        } catch (Exception e) {
            log.error("添加专业失败", e);
            return RestBean.failure(500, "添加专业失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新专业信息
     */
    @PutMapping("/major")
    public RestBean<Major> updateMajor(@RequestBody Major major) {
        try {
            Major existingMajor = courseService.getMajorById(major.getId());
            if (existingMajor == null) {
                return RestBean.failure(404, "专业不存在");
            }
            
            // 校验学院是否存在
            College college = courseService.getCollegeById(major.getCollegeId());
            if (college == null) {
                return RestBean.failure(400, "所选学院不存在");
            }
            
            Major updatedMajor = courseService.updateMajor(major);
            return RestBean.success("更新专业成功", updatedMajor);
        } catch (Exception e) {
            log.error("更新专业失败", e);
            return RestBean.failure(500, "更新专业失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除专业
     */
    @DeleteMapping("/major/{id}")
    public RestBean<Boolean> deleteMajor(@PathVariable Integer id) {
        try {
            Major existingMajor = courseService.getMajorById(id);
            if (existingMajor == null) {
                return RestBean.failure(404, "专业不存在");
            }
            boolean result = courseService.deleteMajor(id);
            return RestBean.success("删除专业成功", result);
        } catch (Exception e) {
            log.error("删除专业失败", e);
            return RestBean.failure(500, "删除专业失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新专业学分要求和主要课程内容
     */
    @PostMapping("/major/{majorId}/content")
    public RestBean<Major> updateMajorContent(
            @PathVariable Integer majorId,
            @RequestBody Map<String, String> contentData) {
        try {
            // 校验专业是否存在
            Major existingMajor = courseService.getMajorById(majorId);
            if (existingMajor == null) {
                return RestBean.failure(404, "专业不存在");
            }
            
            // 更新学分要求和主要课程内容
            String creditRequirements = contentData.get("creditRequirements");
            String mainCourses = contentData.get("mainCourses");
            
            existingMajor.setCreditRequirements(creditRequirements);
            existingMajor.setMainCourses(mainCourses);
            
            Major updatedMajor = courseService.updateMajor(existingMajor);
            return RestBean.success("更新专业内容成功", updatedMajor);
        } catch (Exception e) {
            log.error("更新专业内容失败", e);
            return RestBean.failure(500, "更新专业内容失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取专业图片
     */
    @GetMapping("/major/{majorId}/images")
    public RestBean<List<MajorImage>> getMajorImages(@PathVariable Integer majorId) {
        try {
            // 校验专业是否存在
            Major existingMajor = courseService.getMajorById(majorId);
            if (existingMajor == null) {
                return RestBean.failure(404, "专业不存在");
            }

            List<MajorImage> images = courseService.getMajorImagesByMajorId(majorId);
            return RestBean.success("获取专业图片成功", images);

        } catch (Exception e) {
            log.error("获取专业图片失败", e);
            return RestBean.failure(500, "获取专业图片失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取专业特定类型图片
     */
    @GetMapping("/major/{majorId}/images/{imageType}")
    public RestBean<List<MajorImage>> getMajorImagesByType(
            @PathVariable Integer majorId,
            @PathVariable String imageType) {
        try {
            // 校验专业是否存在
            Major existingMajor = courseService.getMajorById(majorId);
            if (existingMajor == null) {
                return RestBean.failure(404, "专业不存在");
            }
            
            List<MajorImage> images = courseService.getMajorImagesByMajorIdAndType(majorId, imageType);
            return RestBean.success("获取专业图片成功", images);
        } catch (Exception e) {
            log.error("获取专业图片失败", e);
            return RestBean.failure(500, "获取专业图片失败: " + e.getMessage());
        }
    }
    
    /**
     * 上传专业图片
     */
    @PostMapping("/major/{majorId}/uploadImage")
    public RestBean<Map<String, Object>> uploadMajorImage(
            @PathVariable Integer majorId,
            @RequestParam("imageType") String imageType,
            @RequestParam("image") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "oldImagePath", required = false) String oldImagePath) {
        try {
            // 校验文件非空
            if (file == null || file.isEmpty()) {
                log.error("上传失败：文件为空");
                return RestBean.failure(400, "文件为空");
            }
            
            // 校验文件大小
            if (file.getSize() > MAX_FILE_SIZE) {
                log.error("上传失败：文件大小超过限制，文件大小={}", file.getSize());
                return RestBean.failure(400, "文件大小不能超过10MB");
            }
            
            // 校验文件类型
            String contentType = file.getContentType();
            if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
                log.error("上传失败：不支持的文件类型，contentType={}", contentType);
                return RestBean.failure(400, "只支持 JPG, JPEG, PNG, GIF, BMP, WEBP 格式的图片");
            }
            
            // 校验专业是否存在
            Major existingMajor = courseService.getMajorById(majorId);
            if (existingMajor == null) {
                return RestBean.failure(404, "专业不存在");
            }
            
            // 如果有旧图片，先删除
            if (oldImagePath != null && !oldImagePath.isEmpty()) {
                try {
                    // 构建完整路径
                    File oldFile = new File(uploadDir, oldImagePath.replace("uploads/", ""));
                    if (oldFile.exists() && oldFile.isFile()) {
                        if (!oldFile.delete()) {
                            log.warn("旧图片删除失败，路径={}", oldImagePath);
                        }
                    }
                } catch (Exception e) {
                    log.error("删除旧图片失败: {}", e.getMessage());
                }
            }
            
            Map<String, Object> result = courseService.uploadMajorImage(majorId, imageType, file, description);
            
            if ("success".equals(result.get("status"))) {
                return RestBean.success((String) result.get("message"), result);
            } else {
                return RestBean.failure(500, (String) result.get("message"));
            }
        } catch (Exception e) {
            log.error("上传专业图片失败", e);
            return RestBean.failure(500, "上传专业图片失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除专业图片
     */
    @DeleteMapping("/majorImage/{id}")
    public RestBean<Boolean> deleteMajorImage(@PathVariable Integer id) {
        try {
            boolean result = courseService.deleteMajorImage(id);
            if (!result) {
                return RestBean.failure(404, "图片不存在或已删除");
            }
            return RestBean.success("删除图片成功", true);
        } catch (Exception e) {
            log.error("删除图片失败", e);
            return RestBean.failure(500, "删除图片失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有课程
     */
    @GetMapping("/courses")
    public RestBean<List<Course>> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            return RestBean.success("获取课程列表成功", courses);
        } catch (Exception e) {
            log.error("获取课程列表失败", e);
            return RestBean.failure(500, "获取课程列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据专业ID获取课程
     */
    @GetMapping("/courses/byMajor/{majorId}")
    public RestBean<List<Course>> getCoursesByMajorId(@PathVariable Integer majorId) {
        try {
            // 校验专业是否存在
            Major existingMajor = courseService.getMajorById(majorId);
            if (existingMajor == null) {
                return RestBean.failure(404, "专业不存在");
            }
            
            List<Course> courses = courseService.getCoursesByMajorId(majorId);
            return RestBean.success("获取课程列表成功", courses);
        } catch (Exception e) {
            log.error("获取课程列表失败", e);
            return RestBean.failure(500, "获取课程列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据专业ID和课程分类获取课程
     */
    @GetMapping("/courses/byMajor/{majorId}/category/{category}")
    public RestBean<List<Course>> getCoursesByMajorIdAndCategory(
            @PathVariable Integer majorId,
            @PathVariable String category) {
        try {
            // 校验专业是否存在
            Major existingMajor = courseService.getMajorById(majorId);
            if (existingMajor == null) {
                return RestBean.failure(404, "专业不存在");
            }
            
            List<Course> courses = courseService.getCoursesByMajorIdAndCategory(majorId, category);
            return RestBean.success("获取课程列表成功", courses);
        } catch (Exception e) {
            log.error("获取课程列表失败", e);
            return RestBean.failure(500, "获取课程列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取课程
     */
    @GetMapping("/course/{id}")
    public RestBean<Course> getCourseById(@PathVariable Integer id) {
        try {
            Course course = courseService.getCourseById(id);
            if (course == null) {
                return RestBean.failure(404, "课程不存在");
            }
            return RestBean.success("获取课程信息成功", course);
        } catch (Exception e) {
            log.error("获取课程信息失败", e);
            return RestBean.failure(500, "获取课程信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 添加课程
     */
    @PostMapping("/course")
    public RestBean<Course> addCourse(@RequestBody Course course) {
        try {
            // 校验专业是否存在
            Major existingMajor = courseService.getMajorById(course.getMajorId());
            if (existingMajor == null) {
                return RestBean.failure(400, "所选专业不存在");
            }
            
            Course addedCourse = courseService.addCourse(course);
            return RestBean.success("添加课程成功", addedCourse);
        } catch (Exception e) {
            log.error("添加课程失败", e);
            return RestBean.failure(500, "添加课程失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新课程
     */
    @PutMapping("/course")
    public RestBean<Course> updateCourse(@RequestBody Course course) {
        try {
            Course existingCourse = courseService.getCourseById(course.getId());
            if (existingCourse == null) {
                return RestBean.failure(404, "课程不存在");
            }
            
            // 校验专业是否存在
            Major existingMajor = courseService.getMajorById(course.getMajorId());
            if (existingMajor == null) {
                return RestBean.failure(400, "所选专业不存在");
            }
            
            Course updatedCourse = courseService.updateCourse(course);
            return RestBean.success("更新课程成功", updatedCourse);
        } catch (Exception e) {
            log.error("更新课程失败", e);
            return RestBean.failure(500, "更新课程失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除课程
     */
    @DeleteMapping("/course/{id}")
    public RestBean<Boolean> deleteCourse(@PathVariable Integer id) {
        try {
            Course existingCourse = courseService.getCourseById(id);
            if (existingCourse == null) {
                return RestBean.failure(404, "课程不存在");
            }
            boolean result = courseService.deleteCourse(id);
            return RestBean.success("删除课程成功", result);
        } catch (Exception e) {
            log.error("删除课程失败", e);
            return RestBean.failure(500, "删除课程失败: " + e.getMessage());
        }
    }
} 