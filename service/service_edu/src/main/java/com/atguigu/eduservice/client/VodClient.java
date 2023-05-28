package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 视频vod客户端，调用feign服务需要写此接口
 *  */
@FeignClient(name="service-vod",fallback = VodFileDegradeFeignClient.class) //调用的微服务名字
@Component
public interface VodClient {
    //定义调用的方法路径
    //根据视频id删除上传的阿里云视频
    @DeleteMapping(value = "/eduvod/video/removeAlyVideo/{id}")
    // 注意 @PathVariable 注解一定要指定参数名称，否则会报错
    public R removeAlyVideo(@PathVariable("id") String id);

    //删除多个阿里云视频的方法
    //参数多个视频id  List videoIdList
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);

}
