package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.domain.dto.TagListDto;
import cn.edu.hzu.blog.domain.entity.Tag;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.domain.vo.TagVo;
import cn.edu.hzu.blog.enums.AppHttpCodeEnum;
import cn.edu.hzu.blog.exception.SystemException;
import cn.edu.hzu.blog.mapper.TagMapper;
import cn.edu.hzu.blog.service.TagService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Override
    public PageVo pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        return new PageVo(BeanCopyUtils.copyBean(page.getRecords(), TagVo.class), page.getTotal());
    }

    @Override
    public void insertTag(Tag tag) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getName, tag.getName());
        long count = count(queryWrapper);
        if (count != 0)throw new SystemException(AppHttpCodeEnum.TAG_EXIST);
        boolean save = save(tag);
        if (!save)throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId, Tag::getName);
        return BeanCopyUtils.copyBean(list(queryWrapper), TagVo.class);
    }
}
