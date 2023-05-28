package com.atguigu.eduservice.entity.subject;

import lombok.Data;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

//二级分类
@Data
public class TwoSubject {
    private String id;
    private String title;
}
