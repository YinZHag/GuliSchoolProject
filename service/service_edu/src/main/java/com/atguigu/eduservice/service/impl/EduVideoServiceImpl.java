package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-05-19
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    //todo 删除视频待定
    @Autowired
    private VodClient vodClient;

    //根据课程id删除小节内容
    @Override
    public void removeVideoByCourseId(String courseId) {
        // 根据课程id查询课程所有的视频id
        QueryWrapper<EduVideo> queryWrapperVideo = new QueryWrapper<>();
        queryWrapperVideo.eq("course_id",courseId);
        //指定查询的数据表字段
        queryWrapperVideo.select("video_source_id");
        List<EduVideo> eduVideos = baseMapper.selectList(queryWrapperVideo);
        List<String>Ids = new ArrayList<>();
        for(int i=0;i<eduVideos.size();i++){
            String videoId = eduVideos.get(i).getVideoSourceId();
            if(!StringUtils.isEmpty(videoId)) { //不为空
                Ids.add(videoId);
            }
        }
        if(Ids.size()>0) {
            vodClient.deleteBatch(Ids);
        }
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        int delete = baseMapper.delete(queryWrapper);
//        if(delete==0){
//            throw new GuliException(20001,"根据课程id删除小节失败");
//        }


    }
}
