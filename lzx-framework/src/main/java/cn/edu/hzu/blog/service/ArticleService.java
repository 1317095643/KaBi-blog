package cn.edu.hzu.blog.service;
import cn.edu.hzu.blog.domain.dto.AddArticleDto;
import cn.edu.hzu.blog.domain.entity.Article;
import cn.edu.hzu.blog.domain.vo.ArticleListVo;
import cn.edu.hzu.blog.domain.vo.ArticleVo;
import cn.edu.hzu.blog.domain.vo.HotArticleVo;
import cn.edu.hzu.blog.domain.vo.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ArticleService extends IService<Article> {
    List<HotArticleVo> hotArticleList();

    PageVo articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ArticleVo article(Long id);

    void updateViewCount(Long id);

    void add(AddArticleDto articleDto);

    PageVo getArticlePage(Integer pageNum, Integer pageSize, String title, String summary);

    Article getArticle(Long id);

    void updateArticleById(Article article);
}
