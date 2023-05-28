package com.atguigu.eduorder.utils;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 微信支付账号配置选项
 */
@Component
public class ConstantWxUtils implements InitializingBean {
    @ApiModelProperty(value = "关联的公众号")
    @Value("${wx.pay.appid}")
    private String appid;

    @ApiModelProperty(value = "商户号")
    @Value("${wx.pay.partner}")
    private String partner;

    @ApiModelProperty(value = "商户key")
    @Value("${wx.pay.partnerkey}")
    private String partnerkey;

    @ApiModelProperty(value = "回调地址")
    @Value("${wx.pay.notifyurl}")
    private String notifyurl;

    public static String WX_PAY_APPID;
    public static String WX_PAY_PARTNER;
    public static String WX_PAY_PARTNERKEY;
    public static String WX_PAY_NOTIFYURL;
    @Override
    public void afterPropertiesSet() throws Exception {
        WX_PAY_APPID=appid;
        WX_PAY_PARTNER=partner;
        WX_PAY_PARTNERKEY=partnerkey;
        WX_PAY_NOTIFYURL=notifyurl;
    }
}
