package cn.edu.hzu.blog.service;

import cn.edu.hzu.blog.domain.entity.Category;
import cn.edu.hzu.blog.domain.vo.CategoryVo;
import cn.edu.hzu.blog.domain.vo.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<CategoryVo> getCategoryList();

    List<CategoryVo> getAllCategoryList();


    PageVo list(Integer pageNum, Integer pageSize, String name, String status);

    void add(Category category);

    void updateCategory(Category category);
}
