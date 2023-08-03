package cn.edu.hzu.blog.controler;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.service.UploadService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class UploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping("/upload")
    @PreAuthorize("@ps.hasPermission('content:article:writer')")
    public ResponseResult uploadImg(MultipartFile img){
        String upload = uploadService.upload(img);
        return ResponseResult.okResult(upload);
    }

}
