package cn.edu.hzu.blog.domain.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author makejava
 * @since 2023-07-27 09:20:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTag implements Serializable{
    private static final long serialVersionUID = 625337492348897098L;
    //文章id
    private Long articleId;
    //标签id
    private Long tagId;
}

