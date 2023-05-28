package com.atguigu.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data //生成属性对应的getter和setter方法
public class SubjectData {
    @ExcelProperty(index = 0)
    private String oneSubjectName;//一级分类名称
    @ExcelProperty(index = 1)
    private String twoSubjectName;//二级分类名称
}
