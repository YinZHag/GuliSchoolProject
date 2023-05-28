package com.atguigu.eduservice.entity.vo;

import lombok.Data;

@Data
public class CoursePublishVo {
    private String id; //课程id
    private String title;//课程名称
    private String cover; //
    private Integer lessonNum;
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;
    private String price;//只用于显示
}
