package cn.edu.hzu.blog.utils;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils(){}

    public static <Vo> Vo copyBean(Object source, Class<Vo> clazz){
        Vo result = null;
        try {
            result = clazz.getConstructor().newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static <O,Vo> List<Vo> copyBean(List<O> sourceList, Class<Vo> clazz){
        return sourceList.stream().map(obj -> copyBean(obj, clazz)).collect(Collectors.toList());
    }
}
