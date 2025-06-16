package org.example.backend.Service;

import org.example.backend.Entity.pojo.College;
import org.example.backend.Entity.pojo.Course;
import org.example.backend.Entity.pojo.Major;
import org.example.backend.Entity.pojo.MajorImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 课程服务接口
 */
public interface CourseService {
    
    // 学院相关方法
    List<College> getAllColleges();
    College getCollegeById(Integer id);
    College addCollege(College college);
    College updateCollege(College college);
    boolean deleteCollege(Integer id);
    
    // 专业相关方法
    List<Major> getAllMajors();
    Major getMajorById(Integer id);
    List<Major> getMajorsByCollegeId(Integer collegeId);
    Major addMajor(Major major);
    Major updateMajor(Major major);
    boolean deleteMajor(Integer id);
    
    // 课程相关方法
    List<Course> getAllCourses();
    Course getCourseById(Integer id);
    List<Course> getCoursesByMajorId(Integer majorId);
    List<Course> getCoursesByMajorIdAndCategory(Integer majorId, String category);
    Course addCourse(Course course);
    Course updateCourse(Course course);
    boolean deleteCourse(Integer id);
    
    // 专业图片相关方法
    List<MajorImage> getMajorImagesByMajorId(Integer majorId);
    List<MajorImage> getMajorImagesByMajorIdAndType(Integer majorId, String imageType);
    MajorImage addMajorImage(MajorImage majorImage);
    MajorImage updateMajorImage(MajorImage majorImage);
    boolean deleteMajorImage(Integer id);
    
    // 上传专业相关图片
    Map<String, Object> uploadMajorImage(Integer majorId, String imageType, MultipartFile file, String description);
    
    // 获取专业完整信息（包含图片）
    Map<String, Object> getMajorFullInfo(Integer majorId);
} 