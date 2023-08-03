package cn.edu.hzu.blog.controler;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.dto.AddArticleDto;
import cn.edu.hzu.blog.domain.entity.Article;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.service.ArticleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PostMapping
    @PreAuthorize("@ps.hasPermission('content:article:writer')")
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        articleService.add(articleDto);
        return ResponseResult.okResult();
    }

    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('content:article:list')")
    public ResponseResult list(Integer pageNum, Integer pageSize, String title, String summary){
        PageVo result = articleService.getArticlePage(pageNum, pageSize, title, summary);
        return ResponseResult.okResult(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:article:list')")
    public ResponseResult query(@PathVariable("id") Long id){
        Article article = articleService.getArticle(id);
        return ResponseResult.okResult(article);
    }

    @PutMapping
    @PreAuthorize("@ps.hasPermission('content:article:list')")
    public ResponseResult update(@RequestBody Article article){
        articleService.updateArticleById(article);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:article:list')")
    public ResponseResult delete(@PathVariable("id") Long id){
        articleService.removeById(id);
        return ResponseResult.okResult();
    }

}
