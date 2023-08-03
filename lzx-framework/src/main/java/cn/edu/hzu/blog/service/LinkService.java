package cn.edu.hzu.blog.service;

import cn.edu.hzu.blog.domain.entity.Link;
import cn.edu.hzu.blog.domain.vo.LinkVo;
import cn.edu.hzu.blog.domain.vo.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface LinkService extends IService<Link> {
    List<LinkVo> getAllLink();

    PageVo list(Integer pageNum, Integer pageSize, String name, String status);


    Link queryLinkById(Long id);
}
