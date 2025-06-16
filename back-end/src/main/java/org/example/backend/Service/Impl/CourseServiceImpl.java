package org.example.backend.Service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.example.backend.Entity.pojo.College;
import org.example.backend.Entity.pojo.Course;
import org.example.backend.Entity.pojo.Major;
import org.example.backend.Entity.pojo.MajorImage;
import org.example.backend.Mapper.CollegeMapper;
import org.example.backend.Mapper.CourseMapper;
import org.example.backend.Mapper.MajorImageMapper;
import org.example.backend.Mapper.MajorMapper;
import org.example.backend.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 课程服务实现类
 */
@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CollegeMapper collegeMapper;
    
    @Autowired
    private MajorMapper majorMapper;
    
    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private MajorImageMapper majorImageMapper;
    
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 学院相关方法实现
    @Override
    public List<College> getAllColleges() {
        return collegeMapper.findAll();
    }

    @Override
    public College getCollegeById(Integer id) {
        return collegeMapper.findById(id);
    }

    @Override
    @Transactional
    public College addCollege(College college) {
        collegeMapper.insert(college);
        return college;
    }

    @Override
    @Transactional
    public College updateCollege(College college) {
        collegeMapper.update(college);
        return college;
    }

    @Override
    @Transactional
    public boolean deleteCollege(Integer id) {
        return collegeMapper.deleteById(id) > 0;
    }

    // 专业相关方法实现
    @Override
    public List<Major> getAllMajors() {
        return majorMapper.findAll();
    }

    @Override
    public Major getMajorById(Integer id) {
        return majorMapper.findById(id);
    }
    
    @Override
    public List<Major> getMajorsByCollegeId(Integer collegeId) {
        return majorMapper.findByCollegeId(collegeId);
    }

    @Override
    @Transactional
    public Major addMajor(Major major) {
        majorMapper.insert(major);
        return major;
    }

    @Override
    @Transactional
    public Major updateMajor(Major major) {
        majorMapper.update(major);
        return major;
    }

    @Override
    @Transactional
    public boolean deleteMajor(Integer id) {
        return majorMapper.deleteById(id) > 0;
    }

    // 课程相关方法实现
    @Override
    public List<Course> getAllCourses() {
        return courseMapper.findAll();
    }

    @Override
    public Course getCourseById(Integer id) {
        return courseMapper.findById(id);
    }
    
    @Override
    public List<Course> getCoursesByMajorId(Integer majorId) {
        return courseMapper.findByMajorId(majorId);
    }
    
    @Override
    public List<Course> getCoursesByMajorIdAndCategory(Integer majorId, String category) {
        return courseMapper.findByMajorIdAndCategory(majorId, category);
    }

    @Override
    @Transactional
    public Course addCourse(Course course) {
        courseMapper.insert(course);
        return course;
    }

    @Override
    @Transactional
    public Course updateCourse(Course course) {
        courseMapper.update(course);
        return course;
    }

    @Override
    @Transactional
    public boolean deleteCourse(Integer id) {
        return courseMapper.deleteById(id) > 0;
    }
    
    // 专业图片相关方法实现
    @Override
    public List<MajorImage> getMajorImagesByMajorId(Integer majorId) {
        return majorImageMapper.findByMajorId(majorId);
    }
    
    @Override
    public List<MajorImage> getMajorImagesByMajorIdAndType(Integer majorId, String imageType) {
        return majorImageMapper.findByMajorIdAndType(majorId, imageType);
    }
    
    @Override
    @Transactional
    public MajorImage addMajorImage(MajorImage majorImage) {
        majorImageMapper.insert(majorImage);
        return majorImage;
    }
    
    @Override
    @Transactional
    public MajorImage updateMajorImage(MajorImage majorImage) {
        majorImageMapper.update(majorImage);
        return majorImage;
    }
    
    @Override
    @Transactional
    public boolean deleteMajorImage(Integer id) {
        return majorImageMapper.deleteById(id) > 0;
    }

    /**
     * 上传专业相关图片
     * @param majorId 专业ID
     * @param imageType 图片类型
     * @param file 上传的图片文件
     * @param description 图片描述
     * @return 上传结果
     */
    @Override
    @Transactional
    public Map<String, Object> uploadMajorImage(Integer majorId, String imageType, MultipartFile file, String description) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证图片类型
            if (!isValidImageType(imageType)) {
                result.put("status", "error");
                result.put("message", "无效的图片类型，必须是CREDIT_REQUIREMENT、MAIN_COURSE或STRUCTURE");
                return result;
            }
            
            // 查询专业是否存在
            Major major = majorMapper.findById(majorId);
            if (major == null) {
                result.put("status", "error");
                result.put("message", "专业不存在");
                return result;
            }
            
            // 校验文件非空
            if (file == null || file.isEmpty()) {
                result.put("status", "error");
                result.put("message", "文件为空");
                return result;
            }

            // 创建上传目录（若不存在）
            File directory = new File(uploadDir);
            if (!directory.exists() && !directory.mkdirs()) {
                result.put("status", "error");
                result.put("message", "目录创建失败");
                return result;
            }

            // 创建课程图片专用目录
            String courseDirPath = uploadDir + "/course_structure";
            File courseDir = new File(courseDirPath);
            if (!courseDir.exists() && !courseDir.mkdirs()) {
                result.put("status", "error");
                result.put("message", "课程图片目录创建失败");
                return result;
            }

            // 生成唯一文件名（前缀_专业ID_UUID+原始文件名）
            String prefix = getImageTypePrefix(imageType);
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                originalFilename = "unknown.jpg";
            }
            // 净化文件名，防止路径遍历攻击
            String safeFilename = originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
            
            String fileName = prefix + "_major_" + majorId + "_" + UUID.randomUUID() + "_" + safeFilename;
            Path filePath = Paths.get(courseDirPath, fileName);

            try {
                // 保存文件到服务器
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                log.error("保存文件失败: ", e);
                result.put("status", "error");
                result.put("message", "文件保存失败: " + e.getMessage());
                return result;
            }
            
            // 文件相对路径
            String relativePath = "uploads/course_structure/" + fileName;
            
            // 获取当前类型图片的最大排序号
            List<MajorImage> existingImages = majorImageMapper.findByMajorIdAndType(majorId, imageType);
            int maxSortOrder = 0;
            if (existingImages != null && !existingImages.isEmpty()) {
                for (MajorImage img : existingImages) {
                    if (img.getSortOrder() != null && img.getSortOrder() > maxSortOrder) {
                        maxSortOrder = img.getSortOrder();
                    }
                }
            }
            
            // 创建并保存图片记录
            MajorImage majorImage = new MajorImage();
            majorImage.setMajorId(majorId);
            majorImage.setImageType(imageType);
            majorImage.setImageUrl(relativePath);
            majorImage.setSortOrder(maxSortOrder + 1);
            majorImage.setDescription(description);
            
            try {
                majorImageMapper.insert(majorImage);
            } catch (Exception e) {
                // 如果数据库插入失败，删除已上传的文件
                log.error("保存图片记录失败: ", e);
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException ex) {
                    log.error("删除文件失败: ", ex);
                }
                result.put("status", "error");
                result.put("message", "保存图片记录失败: " + e.getMessage());
                return result;
            }

            result.put("status", "success");
            result.put("message", "上传成功");
            result.put("image", majorImage);
            
            return result;
        } catch (Exception e) {
            log.error("文件上传失败: ", e);
            result.put("status", "error");
            result.put("message", "文件上传失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取专业完整信息（包含图片）
     * @param majorId 专业ID
     * @return 专业完整信息
     */
    @Override
    public Map<String, Object> getMajorFullInfo(Integer majorId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取专业基本信息
        Major major = majorMapper.findById(majorId);
        if (major == null) {
            result.put("status", "error");
            result.put("message", "专业不存在");
            return result;
        }
        
        // 获取专业所属学院
        College college = collegeMapper.findById(major.getCollegeId());
        
        // 获取专业图片信息
        List<MajorImage> creditRequirementImages = majorImageMapper.findByMajorIdAndType(majorId, "CREDIT_REQUIREMENT");
        List<MajorImage> mainCourseImages = majorImageMapper.findByMajorIdAndType(majorId, "MAIN_COURSE");
        List<MajorImage> structureImages = majorImageMapper.findByMajorIdAndType(majorId, "STRUCTURE");
        
        // 构建结果
        result.put("status", "success");
        result.put("major", major);
        result.put("college", college);
        result.put("creditRequirementImages", creditRequirementImages);
        result.put("mainCourseImages", mainCourseImages);
        result.put("structureImages", structureImages);
        
        return result;
    }
    
    /**
     * 验证图片类型是否有效
     */
    private boolean isValidImageType(String imageType) {
        return "CREDIT_REQUIREMENT".equals(imageType) || 
               "MAIN_COURSE".equals(imageType) || 
               "STRUCTURE".equals(imageType);
    }
    
    /**
     * 获取图片类型前缀
     */
    private String getImageTypePrefix(String imageType) {
        switch (imageType) {
            case "CREDIT_REQUIREMENT":
                return "cr";
            case "MAIN_COURSE":
                return "mc";
            case "STRUCTURE":
                return "st";
            default:
                return "img";
        }
    }
} 