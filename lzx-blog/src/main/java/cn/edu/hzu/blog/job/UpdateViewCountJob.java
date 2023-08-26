package cn.edu.hzu.blog.job;

import cn.edu.hzu.blog.domain.entity.Article;
import cn.edu.hzu.blog.service.ArticleService;
import cn.edu.hzu.blog.utils.RedisCache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.edu.hzu.blog.constants.SystemConstants.VIEW_COUNT_KEY;

@Component
public class UpdateViewCountJob {

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleService articleService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCount(){
        String key = VIEW_COUNT_KEY;
        Set<String> range = stringRedisTemplate.opsForZSet().range(key, 0, -1);
        List<Article> articleList = range.stream().map(obj -> {
            Long id = Long.valueOf(obj);
            Long viewCount = Objects.requireNonNull(stringRedisTemplate.opsForZSet().score(key, id.toString())).longValue();
            return new Article(id, viewCount);
        }).toList();
        articleService.updateBatchById(articleList);
    }

//    @Scheduled(cron = "0 0/10 * * * ?")
//    public void updateViewCount(){
//        Map<String, Integer> map = redisCache.getCacheMap("article:viewCount");
//        List<Article> articleList = map.entrySet().stream()
//                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
//                .collect(Collectors.toList());
//        articleService.updateBatchById(articleList);
//    }
}
