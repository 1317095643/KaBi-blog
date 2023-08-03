package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.constants.SystemConstants;
import cn.edu.hzu.blog.domain.entity.Comment;
import cn.edu.hzu.blog.domain.entity.User;
import cn.edu.hzu.blog.domain.vo.CommentVo;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.enums.AppHttpCodeEnum;
import cn.edu.hzu.blog.exception.SystemException;
import cn.edu.hzu.blog.mapper.CommentMapper;
import cn.edu.hzu.blog.service.CommentService;
import cn.edu.hzu.blog.service.UserService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private UserService userService;
    @Override
    public PageVo commentList(String type, Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, -1);
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(type), Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getType, type);
        // 分页
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
        // 查询child
        if (Objects.nonNull(commentVoList))commentVoList.stream().map(obj -> obj.setChildren(getChildren(obj.getId()))).collect(Collectors.toList());

        return new PageVo(commentVoList, page.getTotal());
    }

    @Override
    public void addComment(Comment comment) {
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
    }

    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Comment::getCreateTime);
        queryWrapper.eq(Comment::getRootId, id);
        return toCommentVoList(list(queryWrapper));
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
//        if (Objects.isNull(list))return null;
        List<CommentVo> commentVoList = BeanCopyUtils.copyBean(list, CommentVo.class);
        commentVoList.stream().map(obj -> {
            User user = userService.getById(obj.getCreateBy());
            obj.setUsername(user.getNickName());
            obj.setAvatar(user.getAvatar());
            if (obj.getToCommentUserId() != -1)obj.setToCommentUserName(userService.getById(obj.getToCommentUserId()).getNickName());
            return obj;
        }).collect(Collectors.toList());
        return commentVoList;
    }
}
