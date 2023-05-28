package com.atguigu.educenter.controller;
//微信apicontroller

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

@Controller //注意在生成微信二维码时，我们不需要返回请全体，所有不选择restcontroller,只返回请求地址
//@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    //1 生成微信扫描二维码
    @GetMapping("login")
    public String getWxCode() {
        //固定地址，后面拼接参数
//        String url = "https://open.weixin.qq.com/" +
//                "connect/qrconnect?appid="+ ConstantWxUtils.WX_OPEN_APP_ID+"&response_type=code";

        // 微信开放平台授权baseUrl  %s相当于?代表占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //对redirect_url进行URLEncoder编码
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        }catch(Exception e) {
        }

        //设置%s里面值
        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );

        //重定向到请求微信地址里面
        return "redirect:"+url;
    }

    //2 利用扫码二维码获取登陆人信息，并返回相关结果
    @GetMapping("callback")
    public String callback(String code,String state){
        try {
            //1、获取code值，临时票据，类似于验证码
            //2拿着code请求微信固定的地址，得到两个值access_token和openid
            String baseUrl = "https://api.weixin.qq.com/sns/oauth2/" +
                    "access_token?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            String requsetUrl = String.format(baseUrl, ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET, code);
            //利用httpclient工具(封装好的http请求工具)请求这个requsetURl地址，返回后面所需要的access_token和openid
            /**
             * access_token为接口调用凭证 openid授权用户唯一标识
             */
            //使用httpclient发送请求，得到返回结果
            String acceesTokenInfo = HttpClientUtils.get(requsetUrl);
            //使用gson将 http响应得到的json字符串转换为hashmap
            Gson gson = new Gson();
            HashMap accessMap = gson.fromJson(acceesTokenInfo, HashMap.class);
            String access_token = (String) accessMap.get("access_token");
            String openid =(String) accessMap.get("openid"); //获取得到微信授权用户的唯一标识
            QueryWrapper<UcenterMember>queryWrapperMember = new QueryWrapper<>();
            queryWrapperMember.eq("openid",openid);
            UcenterMember ucenterMember= ucenterMemberService.getOpenIdMember(openid);
            //判断当前扫码人是否已经注册过，如果没有注册，则添加到数据库中
            if(ucenterMember==null){
                //3 拿着得到accsess_token 和 openid，再去请求微信提供固定的地址，获取到扫描人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
                //使用httpClientUtils发送请求地址获取响应体
                String userInfo = HttpClientUtils.get(userInfoUrl); //json格数的字符串对象
                //使用gson转换为hashmap格式的存储对象
                HashMap userMap = gson.fromJson(userInfo,HashMap.class);
                String nickname = (String) userMap.get("nickname");
                String headimgurl = (String)userMap.get("headimgurl");
                ucenterMember = new UcenterMember();
                ucenterMember.setOpenid(openid);
                ucenterMember.setAvatar(headimgurl);
                ucenterMember.setNickname(nickname);
                ucenterMemberService.save(ucenterMember);
            }
            //使用jwt根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(),ucenterMember.getNickname());
            //最后：返回首页面，通过路径传递token字符串
            return "redirect:http://localhost:3000?token="+jwtToken;
        }catch (Exception e){
            throw new GuliException(20001,"微信登陆失败");
        }
    }
}
