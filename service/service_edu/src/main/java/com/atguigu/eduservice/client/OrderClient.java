package com.atguigu.eduservice.client;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//edu服务调用order订单接口方法
@Component
@FeignClient(value = "service-order")
public interface OrderClient {
    //根据课程id和用户id查询订单表中订单信息
    @GetMapping("/eduorder/order/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable("courseId") String courseId, @PathVariable("memberId") String memberId);
}
