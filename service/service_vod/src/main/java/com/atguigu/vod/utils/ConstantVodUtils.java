package com.atguigu.vod.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//当项目已启动，spring接口，spring加载之后，执行接口一个方法
@Component
public class ConstantVodUtils implements InitializingBean {
    @Value("${aliyun.vod.file.keyid}")
    private String keyid;
    @Value("${aliyun.vod.file.keysecret}")
    private String keysecret;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = keyid;
        ACCESS_KEY_SECRET=keysecret;
    }
}
