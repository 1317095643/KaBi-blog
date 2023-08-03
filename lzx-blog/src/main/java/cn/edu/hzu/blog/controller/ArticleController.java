package cn.edu.hzu.blog.controller;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.vo.HotArticleVo;
import cn.edu.hzu.blog.service.ArticleService;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    ArticleService articleService;

//    @GetMapping("/list")
//    public List<Article> test(){
//        return articleService.list();
//    }

    @GetMapping("/hotArticleList")
    public ResponseResult getHotList(){
        List<HotArticleVo> articles = articleService.hotArticleList();
        return ResponseResult.okResult(articles);
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId){
        return ResponseResult.okResult(articleService.articleList(pageNum, pageSize, categoryId));
    }

    @GetMapping("/{id}")
    public ResponseResult article(@PathVariable("id") Long id){
        return ResponseResult.okResult(articleService.article(id));
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        articleService.updateViewCount(id);
        return ResponseResult.okResult();
    }
}
