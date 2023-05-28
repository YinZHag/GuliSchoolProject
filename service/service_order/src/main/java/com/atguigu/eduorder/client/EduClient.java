package com.atguigu.eduorder.client;

import com.atguigu.commonutils.ordervo.CourseWebOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 根据课程id返回课程信息
 */
@FeignClient(name = "service-edu")
@Component
public interface EduClient {
    //3 根据课程id返回课程以及讲师信息,order订单里面使用
    @PostMapping("/eduservice/coursefront/getCourseInfoOrder/{id}")
    public CourseWebOrder getCourseInfoOrder(@PathVariable("id") String id);

}
