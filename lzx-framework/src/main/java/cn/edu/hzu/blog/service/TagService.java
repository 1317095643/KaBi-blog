package cn.edu.hzu.blog.service;

import cn.edu.hzu.blog.domain.dto.TagListDto;
import cn.edu.hzu.blog.domain.entity.Tag;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.domain.vo.TagVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TagService extends IService<Tag> {
    PageVo pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    void insertTag(Tag tag);

    List<TagVo> listAllTag();
}
