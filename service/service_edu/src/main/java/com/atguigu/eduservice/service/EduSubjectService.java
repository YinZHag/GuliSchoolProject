package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-05-08
 */
public interface EduSubjectService extends IService<EduSubject> {
    //方法添加课程分类
    void saveSubject(MultipartFile multipartFile,EduSubjectService eduSubjectService);

    List<OneSubject> getAllOneTwoSubject();
}
