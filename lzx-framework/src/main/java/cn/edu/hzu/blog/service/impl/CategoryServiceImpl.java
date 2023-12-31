package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.constants.SystemConstants;
import cn.edu.hzu.blog.domain.entity.Article;
import cn.edu.hzu.blog.domain.entity.Category;
import cn.edu.hzu.blog.domain.vo.CategoryVo;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.enums.AppHttpCodeEnum;
import cn.edu.hzu.blog.exception.SystemException;
import cn.edu.hzu.blog.mapper.CategoryMapper;
import cn.edu.hzu.blog.service.ArticleService;
import cn.edu.hzu.blog.service.CategoryService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import cn.edu.hzu.blog.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.edu.hzu.blog.constants.SystemConstants.CATEGORY_LIST;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private ArticleService articleService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<CategoryVo> getCategoryList() {

//        LambdaQueryWrapper<Article> articleQueryWrapper = new LambdaQueryWrapper<>();
//        // 查询已发布的文章
//        articleQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
//        List<Article> articles = articleService.list(articleQueryWrapper);
//        // 查询结果只保存分类id,利用set去除
//        Set<Long> categoryId = articles.stream().map(Article::getCategoryId).collect(Collectors.toSet());
//        // 查询分类信息，并将非正常状态的信息过滤。
//        List<Category> categoryList = listByIds(categoryId).stream().filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus())).collect(Collectors.toList());
//        return BeanCopyUtils.copyBean(categoryList, CategoryVo.class);
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(CATEGORY_LIST);
        return entries.entrySet().stream().map(obj -> {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setName((String) obj.getValue());
            categoryVo.setId(Long.parseLong((String) obj.getKey()));
            return categoryVo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CategoryVo> getAllCategoryList() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.CATEGORY_STATUS_NORMAL);
        List<Category> result = list(queryWrapper);
        return BeanCopyUtils.copyBean(result, CategoryVo.class);
    }

    @Override
    public PageVo list(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name), Category::getName, name);
        wrapper.eq(StringUtils.hasText(status), Category::getStatus, status);
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);
        List<CategoryVo> result = BeanCopyUtils.copyBean(page.getRecords(), CategoryVo.class);
        return new PageVo(result, page.getTotal());
    }

    @Override
    @Transactional
    public void add(Category category) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, category.getName());
        if (count(wrapper) > 0) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NAME_EXIST);
        }
        save(category);
        stringRedisTemplate.opsForHash().put(CATEGORY_LIST, category.getId().toString(), category.getName());
    }

    @Override
    @Transactional
    public void updateCategory(Category category) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, category.getName());
        wrapper.ne(Category::getId, category.getId());
        if (count(wrapper) > 0) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NAME_EXIST);
        }
        updateById(category);
        stringRedisTemplate.opsForHash().put(CATEGORY_LIST, category.getId(), category.getName());
    }



}
