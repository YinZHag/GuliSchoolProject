package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-05-19
 */
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService eduCourseService;
    //添加课程基本信息的方法
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        //返回添加之后课程id，为了后面添加课程大纲使用

        String cId = eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",cId);
    }
    //根据课程id查询课程和课程描述表基本信息
    @GetMapping("getCourseInfo/{courseId}")
    public  R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = eduCourseService.getCourseInfo(courseId);
            return R.ok().data("courseInfoVo",courseInfoVo);
    }
    //根据课程id查询只和课程相关的信息
    @GetMapping("getCourseOnlyInfo/{courseId}")
    public R getCourseOnlyInfo(@PathVariable String courseId){
        EduCourse eduCourse = eduCourseService.getById(courseId);
        return R.ok().data("course",eduCourse);

    }

    //修改课程和课程描述表信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String cId = eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok().data("courseId",cId);
    }
    //仅仅修改课程表信息
    @PostMapping("updateCourseOnly")
    public R updateCourseOnly(@RequestBody EduCourse eduCourse){
        boolean b = eduCourseService.updateById(eduCourse);
        if(!b){
            throw new GuliException(20001,"修改课程表失败!");
        }
        return R.ok();

    }
    //根据课程id查询课程确认发布信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = eduCourseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);

    }
    //课程最终发布
    //修改课程状态即可
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");//设置课程是否发布状态
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }
    //课程列表 基本实现
    //todo 完善条件查询带分页
    @GetMapping("pageCourse/{current}/{limit}")
    @ApiOperation("分页查询的课程列表")
    public R pageCourse(@PathVariable long current,@PathVariable long limit){
        //创建一个page对象
        Page<EduCourse>page = new Page<>(current,limit);
        QueryWrapper<EduCourse>queryWrapper = new QueryWrapper<>();
        //按时间降序查询课程列表信息
        queryWrapper.orderByDesc("gmt_create");

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到page对象里面
        eduCourseService.page(page,queryWrapper);
        //总记录数
        long total = page.getTotal();
        //分页查询列表
        List<EduCourse> records = page.getRecords();
        return R.ok().data("list",records).data("total",total);
    }

    @PostMapping("pageCourseCondition/{current}/{limit}")
    @ApiOperation("带条件的分页查询课程列表")
    public R pageCourseCondition(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false)CourseQuery courseQuery){
        //创建page对象
        Page<EduCourse>pageCourse = new Page<>(current,limit);
        //创建条件查询wrapper对象
        QueryWrapper<EduCourse>queryWrapper = new QueryWrapper<>();
        //多条件组合查询
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        if(!StringUtils.isEmpty(title)){
            queryWrapper.eq("title",title);
        }
        if(!StringUtils.isEmpty(status)){
            if (status.equals("1")){
                queryWrapper.eq("status", "Normal");
            }else{
                queryWrapper.eq("status", "Draft");
            }
        }
        //查询结果按创建时间降序排列
        queryWrapper.orderByDesc("gmt_create");
        eduCourseService.page(pageCourse,queryWrapper);
        //总记录数
        long total = pageCourse.getTotal();
        //条件分页查询表
        List<EduCourse> records = pageCourse.getRecords();
        return R.ok().data("total",total).data("listRecords",records);
    }

    //删除课程
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId) {
        eduCourseService.removeCourse(courseId);
        return R.ok();
    }
}

