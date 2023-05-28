package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value = "Course查询对象",description = "课程查询条件封装对象")
@Data
public class CourseQuery {
    @ApiModelProperty(value = "课程名称 模糊查询")
    private String title;
    @ApiModelProperty(value = "课程状态 模糊查询")
    private String status;
}
