package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.constants.SystemConstants;
import cn.edu.hzu.blog.domain.dto.AddArticleDto;
import cn.edu.hzu.blog.domain.entity.Article;
import cn.edu.hzu.blog.domain.entity.ArticleTag;
import cn.edu.hzu.blog.domain.entity.Category;
import cn.edu.hzu.blog.domain.vo.*;
import cn.edu.hzu.blog.mapper.ArticleMapper;
import cn.edu.hzu.blog.service.ArticleService;
import cn.edu.hzu.blog.service.ArticleTagService;
import cn.edu.hzu.blog.service.CategoryService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import cn.edu.hzu.blog.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    @Lazy
    private CategoryService categoryService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleTagService articleTagService;

    @Override
    public List<HotArticleVo> hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 文章必须是已发布的
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 按照浏览量排序
        queryWrapper.orderByDesc(Article::getViewCount);
        // 查询前10个
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);
        List<Article> records = page.getRecords();
        return BeanCopyUtils.copyBean(records, HotArticleVo.class);
    }

    @Override
    public PageVo articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        queryWrapper.orderByDesc(Article::getIsTop);
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        page.getRecords().stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        return new PageVo(BeanCopyUtils.copyBean(page.getRecords(), ArticleListVo.class), page.getTotal());
    }

    @Override
    public ArticleVo article(Long id) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId, id);
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        Article result = getOne(queryWrapper);

        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        result.setViewCount(viewCount.longValue());

        Category category = categoryService.getById(result.getCategoryId());
        if (Objects.nonNull(category))result.setCategoryName(category.getName());
        return BeanCopyUtils.copyBean(result, ArticleVo.class);
    }

    @Override
    public void updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount", id.toString(), 1);
    }

    @Override
    @Transactional
    public void add(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
    }

    @Override
    public PageVo getArticlePage(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(title), Article::getTitle, title);
        queryWrapper.like(StringUtils.hasText(summary), Article::getSummary, summary);
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<ArticlePageVo> list = BeanCopyUtils.copyBean(page.getRecords(), ArticlePageVo.class);
        return new PageVo(list, page.getTotal());
    }

    @Override
    public Article getArticle(Long id) {
        // 获取文章内容
        Article article = getById(id);
        // 查询文章Tag信息
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> list = articleTagService.list(queryWrapper);
        // 将查询出来的Tag信息封装进文章类
        article.setTags(list.stream().map(ArticleTag::getTagId).collect(Collectors.toList()));
        return article;
    }

    @Override
    @Transactional
    public void updateArticleById(Article article) {
        updateById(article);
        articleTagService.updateTagsById(article);
    }
}
