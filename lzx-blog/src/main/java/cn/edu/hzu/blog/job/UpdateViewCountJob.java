package cn.edu.hzu.blog.job;

import cn.edu.hzu.blog.domain.entity.Article;
import cn.edu.hzu.blog.service.ArticleService;
import cn.edu.hzu.blog.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleService articleService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCount(){
        Map<String, Integer> map = redisCache.getCacheMap("article:viewCount");
        List<Article> articleList = map.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        articleService.updateBatchById(articleList);
    }
}
