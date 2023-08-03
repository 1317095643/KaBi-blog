package cn.edu.hzu.blog.controler;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.entity.Link;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.service.LinkService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Resource
    private LinkService linkService;

    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('content:link:query')")
    public ResponseResult list(Integer pageNum, Integer pageSize, String name, String status){
        PageVo result = linkService.list(pageNum, pageSize, name, status);
        return ResponseResult.okResult(result);
    }

    @PostMapping
    @PreAuthorize("@ps.hasPermission('content:link:add')")
    public ResponseResult add(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:link:query')")
    public ResponseResult query(@PathVariable("id") Long id){
        Link link = linkService.queryLinkById(id);
        return ResponseResult.okResult(link);
    }

    @PutMapping
    @PreAuthorize("@ps.hasPermission('content:link:edit')")
    public ResponseResult update(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:link:remove')")
    public ResponseResult delete(@PathVariable("id") Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }

}
