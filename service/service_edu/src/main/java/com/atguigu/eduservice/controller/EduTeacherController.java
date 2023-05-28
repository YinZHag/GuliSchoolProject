package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-05-05
 */
@RestController
@RequestMapping("/eduservice/teacher")
@Api(description = "讲师管理")
//@CrossOrigin
public class EduTeacherController {
    //todo 利用service 接口调用查询方法，查询所有老师信息
    @Autowired
    private EduTeacherService TeacherService;
    @GetMapping("findAll")
    @ApiOperation(value = "显示讲师列表")
    public R findAllTeacher(){
        List<EduTeacher> list = TeacherService.list(null);
        return R.ok().data("items",list);
    }

    //todo 2、逻辑删除讲师通过路径传递讲师ID ,REST风格
    @DeleteMapping("{id}")
    @ApiOperation(value = "逻辑删除讲师列表")
    public R removeTeacher(@ApiParam(name="id",value="讲师ID",required = true) @PathVariable String id){
        boolean b = TeacherService.removeById(id);
        if (b){
            return R.ok();
        }else{
            return R.error();
        }
    }

    // 3 分页查询讲师的方法
    // current 当前页
    // limit 每页记录数
    @GetMapping("pageTeacher/{current}/{limit}")
    @ApiOperation(value = "分页查询讲师信息")
    public R pageListTeacher(@PathVariable long current,@PathVariable long limit){
        // 创建一个page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        TeacherService.page(pageTeacher,null);
        // 总记录数
        long total = pageTeacher.getTotal();
        // 数据list集合
        List<EduTeacher> records = pageTeacher.getRecords();
        Map map = new HashMap();
        map.put("total",total);
        map.put("listRecords",records);
        //放入返回结果集中
        return R.ok().data(map);
    }

    //4 条件查询带分页的方法
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) TeacherQuery teacherQuery){
        //创建Page对象
        Page<EduTeacher> page = new Page<>(current,limit);
        // 构建条件
        QueryWrapper<EduTeacher>queryWrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            queryWrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            //todo 注意带条件的范围查询中column列属性字段的值是与表字段一致的
            queryWrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            //todo 注意带条件的范围查询中column列属性字段的值是与表字段一致的
            queryWrapper.le("gmt_create",end);
        }

        //排序
        queryWrapper.orderByDesc("gmt_create");

        //调用方法实现条件查询分页
        TeacherService.page(page,queryWrapper);
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        return R.ok().data("total",total).data("list_records",records);
    }

    //5 添加讲师功能接口
    @PostMapping("addTeacher")
    @ApiOperation("添加讲师信息")
    public R addTeacher(@ApiParam("输入讲师信息") @RequestBody EduTeacher eduTeacher){
        boolean save = TeacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }else{
            return R.error();
        }
    }

    //6根据讲师id进行查询
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher eduTeacher = TeacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    //7讲师修改功能
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = TeacherService.updateById(eduTeacher);
        if(flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }








}

