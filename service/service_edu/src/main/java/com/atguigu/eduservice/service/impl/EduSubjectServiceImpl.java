package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.One;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-05-08
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    //添加课程分类
    @Override
    public void saveSubject(MultipartFile multipartFile,EduSubjectService eduSubjectService) {
        try{
            //文件输入流
            InputStream in = multipartFile.getInputStream();
            //调用方法进行读取
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //获取所有的课程一级分类和二级分类列表，树形结构
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //1、查询所有一级分类 parent_id=0
        QueryWrapper<EduSubject>queryWrapperone = new QueryWrapper<>();
        queryWrapperone.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(queryWrapperone);
        //2、查询所有二级分类 parent_id!=0
        QueryWrapper<EduSubject>queryWrappertwo = new QueryWrapper<>();
        queryWrappertwo.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(queryWrappertwo);
        //创建list集合用于收集整体数据
        List<OneSubject>finalSubjectList = new ArrayList<>();
        //3 封装一级分类
        for(int i=0;i<oneSubjectList.size();i++){
            EduSubject one = oneSubjectList.get(i);
            OneSubject oneSubject = new OneSubject();
//            oneSubject.setId(one.getId());
//            oneSubject.setTitle(one.getTitle());
            BeanUtils.copyProperties(one,oneSubject);
            //在一级分类循环遍历查询所有的二级分类
            //创建list集合封装每个一级分类的二级分类
            List<TwoSubject>finalTwoSubjectList = new ArrayList<>();
            for(int m=0;m<twoSubjectList.size();m++){
                EduSubject two = twoSubjectList.get(m);
                if(two.getParentId().equals(one.getId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(two,twoSubject);
                    finalTwoSubjectList.add(twoSubject);
                }

            }
            if(!finalTwoSubjectList.isEmpty()) {
                oneSubject.setChildren(finalTwoSubjectList);
            }
            finalSubjectList.add(oneSubject);
        }

        return finalSubjectList;
    }
}
