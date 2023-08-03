package cn.edu.hzu.blog.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    public String upload(MultipartFile img);
}
