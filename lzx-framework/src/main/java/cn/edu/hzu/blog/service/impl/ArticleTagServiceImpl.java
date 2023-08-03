package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.domain.entity.Article;
import cn.edu.hzu.blog.domain.entity.ArticleTag;
import cn.edu.hzu.blog.mapper.ArticleTagMapper;
import cn.edu.hzu.blog.service.ArticleTagService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
    @Override
    @Transactional
    public void updateTagsById(Article article) {
        List<Long> tags = article.getTags();
        List<ArticleTag> list = tags.stream().map(obj -> new ArticleTag(article.getId(), obj)).collect(Collectors.toList());
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, article.getId());
        baseMapper.delete(queryWrapper);
        for (ArticleTag articleTag : list){
            baseMapper.insert(articleTag);
        }
    }
}
