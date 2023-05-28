package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-05-08
 */
@RestController
@RequestMapping("/eduservice/subject")
//@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService eduSubjectService;
    //添加课程分类
    //获取上传过来的文件，把文件内容读取出来
    @PostMapping("addSubject")
    public R addSubject(MultipartFile multipartFile){

        //上传过来的excel文件
        eduSubjectService.saveSubject(multipartFile,eduSubjectService);
        return R.ok();

    }
    //课程分类列表功能(树形显示)
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        //list集合泛型是一级分类
        List<OneSubject> list = eduSubjectService.getAllOneTwoSubject();

        return R.ok().data("list",list);
    }
}

