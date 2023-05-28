package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-05-19
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduVideoService eduVideoService; //注入小节的service

    //删除章节的做法
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapterid章节id查询小节表，如果查询数据，不进行删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(wrapper);
        if(count>0){//查询出小节，不进行删除
            throw new GuliException(20001,"不能删除");

        }else{
            //删除章节
            int result = baseMapper.deleteById(chapterId);
            return result>0;
        }
    }
    //课程大纲列表，根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        // 1、 根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> queryWrapperChapter = new QueryWrapper<>();
        queryWrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(queryWrapperChapter);
        // 2、 根据课程id查询课程里面所有的小节
        QueryWrapper<EduVideo> queryWrapperVideo = new QueryWrapper<>();
        queryWrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(queryWrapperVideo);
        //创建list集合，用于最终封装数据
        List<ChapterVo>finaList = new ArrayList<>();
        // 3、遍历查询章节list集合进行封装
        for (int i = 0; i < eduChapterList.size(); i++) {
            EduChapter eduChapter = eduChapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            List<VideoVo>videoVoList = new ArrayList<>();
            // 4、 遍历查询小节list集合，进行封装
            for (int m = 0; m < eduVideoList.size(); m++) {
                //得到每个小节
                EduVideo eduVideo = eduVideoList.get(m);
                //判断当前小节属于哪个章节一部分
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVoList.add(videoVo);
                }

            }
            //把封装之后小节list集合，放到章节对象里面
            chapterVo.setChildren(videoVoList);
            finaList.add(chapterVo);
        }
        return finaList;
    }

    //根据课程id删除章节内容
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter>queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        int delete = baseMapper.delete(queryWrapper);
//        if(delete==0){
//            throw new GuliException(20001,"根据课程id删除章节失败!");
//        }

    }
}
