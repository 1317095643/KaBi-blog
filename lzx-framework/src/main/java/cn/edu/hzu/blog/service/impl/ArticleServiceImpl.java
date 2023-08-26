package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.constants.SystemConstants;
import cn.edu.hzu.blog.domain.dto.AddArticleDto;
import cn.edu.hzu.blog.domain.entity.Article;
import cn.edu.hzu.blog.domain.entity.ArticleTag;
import cn.edu.hzu.blog.domain.entity.Category;
import cn.edu.hzu.blog.domain.vo.*;
import cn.edu.hzu.blog.enums.AppHttpCodeEnum;
import cn.edu.hzu.blog.exception.SystemException;
import cn.edu.hzu.blog.mapper.ArticleMapper;
import cn.edu.hzu.blog.service.ArticleService;
import cn.edu.hzu.blog.service.ArticleTagService;
import cn.edu.hzu.blog.service.CategoryService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import cn.edu.hzu.blog.utils.RedisCache;
import cn.edu.hzu.blog.utils.SimpleLockUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.edu.hzu.blog.constants.SystemConstants.*;
import static java.util.stream.Collectors.toList;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    @Lazy
    private CategoryService categoryService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleTagService articleTagService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public List<HotArticleVo> hotArticleList() {
        Set<ZSetOperations.TypedTuple<String>> typedTuples =
                stringRedisTemplate.opsForZSet().reverseRangeWithScores(VIEW_COUNT_KEY, 0, 10);
        if (typedTuples != null) {
            return typedTuples.stream().map(obj -> {
                Long id = Long.parseLong(Objects.requireNonNull(obj.getValue()));
                String title = Objects.requireNonNull(getArticleWithMutex(id)).getTitle();
                Long viewCount = Objects.requireNonNull(obj.getScore()).longValue();
                return new HotArticleVo(id, title, viewCount);
            }).collect(toList());
        }
        return null;
    }
//    @Override
//    public List<HotArticleVo> hotArticleList() {
//        Map<String, Integer> map = redisCache.getCacheMap("article:viewCount");
//        // Sort the map by values in descending order
//        List<Map.Entry<String, Integer>> sortedEntries = map.entrySet()
//                .stream()
//                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
//                .collect(toList());
//
//        // Get the top 10 entries
//        List<Map.Entry<String, Integer>> top10Entries = sortedEntries.subList(0, Math.min(10, sortedEntries.size()));
//
//        // Convert keys to long and store in a List<Long>
//        List<Article> articleList = top10Entries.stream()
//                .map(entry -> baseMapper.selectById(Long.parseLong(entry.getKey())).setViewCount(entry.getValue().longValue()))
//                .toList();
//
//        return BeanCopyUtils.copyBean(articleList, HotArticleVo.class);
//    }

    @Override
    public PageVo articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        queryWrapper.orderByAsc(Article::getIsTop);
        queryWrapper.orderByDesc(Article::getCreateTime);
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<Article> list = page.getRecords();
        list.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .map(article -> {
                    Long viewCount = Objects.requireNonNull(stringRedisTemplate.opsForZSet()
                            .score(VIEW_COUNT_KEY, article.getId().toString())).longValue();
                    article.setViewCount(viewCount);
                    return article;
                })
                .collect(toList());
        return new PageVo(BeanCopyUtils.copyBean(list, ArticleListVo.class), page.getTotal());
    }

    @Override
    public ArticleVo article(Long id) {
        ArticleVo result = getArticleWithMutex(id);
        if (result == null)throw new SystemException(AppHttpCodeEnum.ARTICLE_ID_NOT_EXIST);
        return result;
    }

    @Override
    public void updateViewCount(Long id) {
        stringRedisTemplate.opsForZSet().incrementScore(VIEW_COUNT_KEY, id.toString(), 1);
    }

    @Override
    @Transactional
    public void add(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        stringRedisTemplate.opsForZSet().add(VIEW_COUNT_KEY, article.getId().toString(), 0);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(toList());
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
        if (article == null)return null;
        // 查询文章Tag信息
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> list = articleTagService.list(queryWrapper);
        // 将查询出来的Tag信息封装进文章类
        article.setTags(list.stream().map(ArticleTag::getTagId).collect(toList()));
        return article;
    }

    @Override
    @Transactional
    public void updateArticleById(Article article) {
        updateById(article);
        articleTagService.updateTagsById(article);
        stringRedisTemplate.delete(ARTICLE_DETAIL + article.getId());
    }

    private ArticleVo getArticleWithMutex(Long id){
        String key = ARTICLE_DETAIL + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.hasText(json)){
            ArticleVo article = JSONUtil.toBean(json, ArticleVo.class);
            Long viewCount = Objects.requireNonNull(stringRedisTemplate.opsForZSet()
                    .score(VIEW_COUNT_KEY, article.getId().toString())).longValue();
            article.setViewCount(viewCount);
            return article;
        }else if(json == null){
            SimpleLockUtil lockUtil = new SimpleLockUtil(key, stringRedisTemplate);
            boolean isLock = lockUtil.tryLock(10L);
            if (isLock){
                try {
                    json = stringRedisTemplate.opsForValue().get(key);
                    if (json != null){
                        ArticleVo article = JSONUtil.toBean(json, ArticleVo.class);
                        Long viewCount = Objects.requireNonNull(stringRedisTemplate.opsForZSet()
                                .score(VIEW_COUNT_KEY, article.getId().toString())).longValue();
                        article.setViewCount(viewCount);
                        return article;
                    }
                    ArticleVo article = queryArticle(id);
                    if (article == null){
                        stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                        return null;
                    }
                    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(article), ARTICLE_DETAIL_TTL, TimeUnit.SECONDS);
                    return article;
                }finally {
                    lockUtil.unlock();
                }
            }else{
                try {
                    Thread.sleep(50);
                    return getArticleWithMutex(id);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    private ArticleVo queryArticle(Long id) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId, id);
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        Article result = getOne(queryWrapper);
        if (result == null)return null;
        Long viewCount = Objects.requireNonNull(stringRedisTemplate.opsForZSet().score(VIEW_COUNT_KEY, id.toString())).longValue();
//        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
//        if (viewCount == null)redisCache.setCacheMapValue("article:viewCount", id.toString(), result.getViewCount().intValue());
        result.setViewCount(viewCount);
        Category category = categoryService.getById(result.getCategoryId());
        if (Objects.nonNull(category))result.setCategoryName(category.getName());
        return BeanCopyUtils.copyBean(result, ArticleVo.class);
    }
}
