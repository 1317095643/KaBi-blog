package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.constants.SystemConstants;
import cn.edu.hzu.blog.domain.entity.Link;
import cn.edu.hzu.blog.domain.vo.LinkVo;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.mapper.LinkMapper;
import cn.edu.hzu.blog.service.LinkService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Override
    public List<LinkVo> getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_APPROVED);
        List<Link> list = list(queryWrapper);
        return BeanCopyUtils.copyBean(list, LinkVo.class);
    }

    @Override
    public PageVo list(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status), Link::getStatus, status);
        wrapper.like(StringUtils.hasText(name), Link::getName, name);
        wrapper.select(Link::getAddress, Link::getDescription, Link::getId, Link::getName, Link::getStatus, Link::getLogo);
        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);
        return new PageVo(page.getRecords(), page.getTotal());
    }

    @Override
    public Link queryLinkById(Long id) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Link::getId, id);
        wrapper.select(Link::getAddress, Link::getDescription, Link::getId, Link::getLogo, Link::getName, Link::getStatus);
        return getOne(wrapper);
    }

}
