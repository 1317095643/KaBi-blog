package cn.edu.hzu.blog.runner;

import cn.edu.hzu.blog.domain.entity.Article;
import cn.edu.hzu.blog.mapper.ArticleMapper;
import cn.edu.hzu.blog.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        // 查询博客信息 id -> viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));

        // 存储到redis中
        redisCache.setCacheMap("article:viewCount", viewCountMap);
    }
}
