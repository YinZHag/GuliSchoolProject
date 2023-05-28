package com.atguigu;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println(JwtUtils.getJwtToken("1","liam"));
    }
}