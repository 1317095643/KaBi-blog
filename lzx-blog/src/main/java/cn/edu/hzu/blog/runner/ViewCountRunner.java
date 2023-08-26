package cn.edu.hzu.blog.runner;

import cn.edu.hzu.blog.constants.SystemConstants;
import cn.edu.hzu.blog.domain.entity.Article;
import cn.edu.hzu.blog.domain.entity.Category;
import cn.edu.hzu.blog.mapper.ArticleMapper;
import cn.edu.hzu.blog.mapper.CategoryMapper;
import cn.edu.hzu.blog.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.edu.hzu.blog.constants.SystemConstants.*;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private RedisCache redisCache;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
        // 查询博客信息 id -> viewCount
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Article::getId, Article::getViewCount, Article::getCategoryId);
        wrapper.eq(Article::getStatus, STATUS_NORMAL);
        List<Article> articles = articleMapper.selectList(wrapper);
        // 存储到redis中
        for (Article article:articles){
            stringRedisTemplate.opsForZSet().add(VIEW_COUNT_KEY, article.getId().toString(), article.getViewCount());
        }
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId, Category::getName);
        queryWrapper.eq(Category::getStatus, STATUS_NORMAL);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        for (Category category:categories){
            stringRedisTemplate.opsForHash().put(CATEGORY_LIST, category.getId().toString(), category.getName());
        }
    }


//    @Override
//    public void run(String... args) throws Exception {
//        // 查询博客信息 id -> viewCount
//        List<Article> articles = articleMapper.selectList(null);
//        Map<String, Integer> viewCountMap = articles.stream()
//                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));
//
//        // 存储到redis中
//        redisCache.setCacheMap("article:viewCount", viewCountMap);
//    }
}
