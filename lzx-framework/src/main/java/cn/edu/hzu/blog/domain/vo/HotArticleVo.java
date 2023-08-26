package cn.edu.hzu.blog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors
public class HotArticleVo {
    private Long id;
    private String title;
    private Long viewCount;
}
