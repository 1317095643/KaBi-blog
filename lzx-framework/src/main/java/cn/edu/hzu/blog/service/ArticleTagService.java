package cn.edu.hzu.blog.service;

import cn.edu.hzu.blog.domain.entity.Article;
import cn.edu.hzu.blog.domain.entity.ArticleTag;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ArticleTagService extends IService<ArticleTag> {
    void updateTagsById(Article article);
}
