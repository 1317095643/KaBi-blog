package cn.edu.hzu.blog.controller;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.vo.CategoryVo;
import cn.edu.hzu.blog.service.CategoryService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        List<CategoryVo> result = categoryService.getCategoryList();
        return ResponseResult.okResult(result);
    }
}
