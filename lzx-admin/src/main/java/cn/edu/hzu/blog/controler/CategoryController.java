package cn.edu.hzu.blog.controler;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.entity.Category;
import cn.edu.hzu.blog.domain.vo.CategoryVo;
import cn.edu.hzu.blog.domain.vo.ExcelCategoryVo;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.enums.AppHttpCodeEnum;
import cn.edu.hzu.blog.service.CategoryService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import cn.edu.hzu.blog.utils.WebUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import org.burningwave.core.assembler.StaticComponentContainer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    @PreAuthorize("@ps.hasPermission('content:category:list')")
    public ResponseResult listAllCategory(){
        return ResponseResult.okResult(categoryService.getAllCategoryList());
    }


    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        StaticComponentContainer.Modules.exportAllToAll();
        try {
            // 设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            // 获取需要导出的数据
            List<Category> list = categoryService.list();
            List<ExcelCategoryVo> data = BeanCopyUtils.copyBean(list, ExcelCategoryVo.class);
            // 把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class)
                    .autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(data);
        } catch (Exception e) {
            // 如果出现异常也要响应json
            response.reset();
            e.printStackTrace();
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('content:category:list')")
    public ResponseResult list(Integer pageNum, Integer pageSize, String name, String status){
        PageVo result = categoryService.list(pageNum, pageSize, name, status);
        return ResponseResult.okResult(result);
    }

    @PostMapping
    @PreAuthorize("@ps.hasPermission('content:category:list')")
    public ResponseResult add(@RequestBody Category category){
        categoryService.add(category);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:category:list')")
    public ResponseResult query(@PathVariable("id") Long id){
        CategoryVo result = BeanCopyUtils.copyBean(categoryService.getById(id), CategoryVo.class);
        return ResponseResult.okResult(result);
    }

    @PutMapping
    @PreAuthorize("@ps.hasPermission('content:category:list')")
    public ResponseResult update(@RequestBody Category category){
        categoryService.updateCategory(category);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:category:list')")
    public ResponseResult delete(@PathVariable("id") Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }

}
