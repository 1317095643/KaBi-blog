package cn.edu.hzu.blog.controler;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.dto.TagListDto;
import cn.edu.hzu.blog.domain.entity.Tag;
import cn.edu.hzu.blog.domain.vo.TagVo;
import cn.edu.hzu.blog.enums.AppHttpCodeEnum;
import cn.edu.hzu.blog.exception.SystemException;
import cn.edu.hzu.blog.service.TagService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return ResponseResult.okResult(tagService.pageTagList(pageNum, pageSize, tagListDto));
    }

    @GetMapping("/listAllTag")
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    public ResponseResult listAllTag(){
        return ResponseResult.okResult(tagService.listAllTag());
    }

    @PostMapping
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    public ResponseResult addTag(@RequestBody Tag tag){
        tagService.insertTag(tag);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    public ResponseResult deleteTag(@PathVariable Long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    public ResponseResult queryTag(@PathVariable Long id){
        Tag tag = tagService.getById(id);
        if (Objects.isNull(tag))throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        return ResponseResult.okResult(BeanCopyUtils.copyBean(tag, TagVo.class));
    }

    @PutMapping
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    public ResponseResult updateTag(@RequestBody Tag tag){
        boolean result = tagService.updateById(tag);
        if (!result)throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        return ResponseResult.okResult();
    }
}
