package org.example.backend.Controller;

import org.example.backend.Entity.pojo.RestBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
    
    // 允许的图片类型
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/bmp", "image/webp"
    );
    
    // 文件大小限制 (10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 处理图片上传请求
     * @param file 上传的图片文件
     * @return 上传结果，成功返回图片相对路径
     */
    @PostMapping("/blog/uploadImg")
    public RestBean<String> uploadBlogImage(@RequestParam("image") MultipartFile file) {
        return uploadImage(file, null);
    }

    /**
     * 处理用户头像上传请求
     * @param file 上传的头像文件
     * @return 上传结果，成功返回图片相对路径
     */
    @PostMapping("/user/uploadImg")
    public RestBean<String> uploadUserImage(@RequestParam("image") MultipartFile file) {
        return uploadImage(file, null);
    }
    
    /**
     * 处理活动图片上传请求
     * @param file 上传的活动图片文件
     * @return 上传结果，成功返回图片相对路径
     */
    @PostMapping("/activity/uploadImg")
    public RestBean<String> uploadActivityImage(@RequestParam("image") MultipartFile file) {
        return uploadImage(file, null);
    }

    /**
     * 处理图片上传请求（带有旧图片路径，会删除旧图片）
     * @param file 上传的图片文件
     * @param oldImagePath 旧图片路径
     * @return 上传结果，成功返回图片相对路径
     */
    @PostMapping("/uploadImg")
    public RestBean<String> uploadImageWithDelete(
            @RequestParam("image") MultipartFile file,
            @RequestParam(value = "oldImagePath", required = false) String oldImagePath) {
        return uploadImage(file, oldImagePath);
    }

    /**
     * 通用图片上传处理方法
     * @param file 上传的图片文件
     * @param oldImagePath 旧图片路径，如果不为空则删除旧图片
     * @return 上传结果，成功返回图片相对路径
     */
    private RestBean<String> uploadImage(MultipartFile file, String oldImagePath) {
        try {
            // 1. 校验文件非空
            if (file == null || file.isEmpty()) {
                logger.error("上传失败：文件为空");
                return RestBean.failure(400, "文件为空");
            }
            
            // 2. 校验文件大小
            if (file.getSize() > MAX_FILE_SIZE) {
                logger.error("上传失败：文件大小超过限制，文件大小={}", file.getSize());
                return RestBean.failure(400, "文件大小不能超过10MB");
            }
            
            // 3. 校验文件类型
            String contentType = file.getContentType();
            if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
                logger.error("上传失败：不支持的文件类型，contentType={}", contentType);
                return RestBean.failure(400, "只支持 JPG, JPEG, PNG, GIF, BMP, WEBP 格式的图片");
            }

            // 4. 创建上传目录（若不存在）
            File directory = new File(uploadDir);
            if (!directory.exists() && !directory.mkdirs()) {
                logger.error("上传失败：目录创建失败，目录路径={}", uploadDir);
                return RestBean.failure(500, "目录创建失败");
            }

            // 5. 如果有旧图片，则删除
            if (oldImagePath != null && !oldImagePath.isEmpty()) {
                boolean deleted = deleteOldImage(oldImagePath);
                if (!deleted) {
                    logger.warn("旧图片删除失败，路径={}", oldImagePath);
                }
            }

            // 6. 生成唯一文件名（UUID + 原始文件名）
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                originalFilename = "unknown.jpg";
            }
            
            String fileName = UUID.randomUUID() + "_" + originalFilename;
            Path filePath = Paths.get(uploadDir, fileName);

            // 7. 保存文件到服务器
            Files.copy(file.getInputStream(), filePath);

            // 8. 返回相对路径（如 "uploads/xxx.jpg"）
            String relativePath = "uploads/" + fileName;
            logger.info("图片上传成功：{}", relativePath);
            return RestBean.success("上传成功", relativePath);
        } catch (FileAlreadyExistsException e) {
            // 如果文件已存在，重新生成文件名再试一次
            logger.error("上传失败：文件已存在", e);
            return RestBean.failure(500, "文件已存在，请重试");
        } catch (IOException e) {
            logger.error("上传失败：IO异常", e);
            return RestBean.failure(500, "文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("上传失败：未知异常", e);
            return RestBean.failure(500, "文件上传失败，请联系管理员");
        }
    }

    /**
     * 删除旧图片
     * @param imagePath 图片相对路径
     * @return 是否删除成功
     */
    private boolean deleteOldImage(String imagePath) {
        try {
            // 如果路径包含"uploads/"前缀，说明是相对路径，需要转换为绝对路径
            if (imagePath.startsWith("uploads/")) {
                String relativePath = imagePath;
                // 构建完整的文件路径
                Path fullPath = Paths.get(uploadDir, relativePath.substring("uploads/".length()));
                File fileToDelete = fullPath.toFile();
                
                if (fileToDelete.exists() && fileToDelete.isFile()) {
                    return fileToDelete.delete();
                }
            } else {
                // 如果是直接的文件名，则直接在上传目录中查找
                Path fullPath = Paths.get(uploadDir, imagePath);
                File fileToDelete = fullPath.toFile();
                
                if (fileToDelete.exists() && fileToDelete.isFile()) {
                    return fileToDelete.delete();
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("删除旧图片失败", e);
            return false;
        }
    }
    
    /**
     * 删除图片API
     * @param imagePath 图片路径
     * @return 删除结果
     */
    @DeleteMapping("/deleteImg")
    public RestBean<Boolean> deleteImage(@RequestParam("imagePath") String imagePath) {
        boolean success = deleteOldImage(imagePath);
        if (success) {
            return RestBean.success("删除成功", true);
        } else {
            return RestBean.failure(500, "删除失败");
        }
    }
}
