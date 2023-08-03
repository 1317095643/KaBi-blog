package cn.edu.hzu.blog.controller;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.vo.LinkVo;
import cn.edu.hzu.blog.service.LinkService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Resource
    LinkService linkService;

    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        List<LinkVo> list = linkService.getAllLink();
        return ResponseResult.okResult(list);
    }
}
