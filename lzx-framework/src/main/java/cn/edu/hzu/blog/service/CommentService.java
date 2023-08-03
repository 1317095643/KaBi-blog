package cn.edu.hzu.blog.service;

import cn.edu.hzu.blog.domain.entity.Comment;
import cn.edu.hzu.blog.domain.vo.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CommentService extends IService<Comment> {
    PageVo commentList(String type, Long articleId, Integer pageNum, Integer pageSize);

    void addComment(Comment comment);
}
