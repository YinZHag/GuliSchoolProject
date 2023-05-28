package com.atguigu.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssInterface {

    //上传头像到oss
    String uploadFileAvatar(MultipartFile file);
}
