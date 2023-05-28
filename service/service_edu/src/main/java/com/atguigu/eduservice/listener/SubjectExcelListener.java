package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.val;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    //因为SubjectListener不能交给spring进行管理，需要自己new，不能注入其它对象
    //不能实现数据库操作
    //需要自己手动创建edusubjectservice
    public EduSubjectService eduSubjectService;

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    public SubjectExcelListener() {
    }
    //读取excel内容，一行一行进行读取
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData==null){
            throw new GuliException(20001,"文件数据为空");
        }
        //一行一行读取，每次读取有两个值，第一个值为一级分类，第二个值为二级分类
        //判断一级分类是否包含重复的值
        EduSubject eduSubject = this.existOneSubject(eduSubjectService, subjectData.getOneSubjectName());
        if (eduSubject==null){ //没有相同的一级分类，进行添加
            eduSubject = new EduSubject();
            eduSubject.setTitle(subjectData.getOneSubjectName());
            eduSubject.setParentId("0");
            eduSubjectService.save(eduSubject);

        }
        //添加二级分类
        //判断二级分类是否重复
        //根据一级分类的eduSubject对象获取一级分类的pid值
        String parent_id = eduSubject.getId();
        EduSubject eduSubject1 = this.existTwoSubject(eduSubjectService, subjectData.getTwoSubjectName(), parent_id);
        if(eduSubject1==null){ //二级分类不包含重复的值
            eduSubject1 = new EduSubject();
            eduSubject1.setTitle(subjectData.getTwoSubjectName());
            eduSubject1.setParentId(parent_id);
            eduSubjectService.save(eduSubject1);

        }


    }
    //判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService eduSubjectService,String name){
        QueryWrapper<EduSubject>wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",0);
        EduSubject one = eduSubjectService.getOne(wrapper);
        return one;
    }
    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService,String name,String parent_id){
        QueryWrapper<EduSubject>wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",parent_id);
        EduSubject two = eduSubjectService.getOne(wrapper);
        return two;
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
