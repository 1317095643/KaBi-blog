package cn.edu.hzu.blog.controller;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.entity.Comment;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        PageVo pageVo = commentService.commentList("0", articleId, pageNum, pageSize);
        return ResponseResult.okResult(pageVo);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        commentService.addComment(comment);
        return ResponseResult.okResult();
    }

    @GetMapping("linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize){
        PageVo pageVo = commentService.commentList("1", null, pageNum, pageSize);
        return ResponseResult.okResult(pageVo);
    }
}
