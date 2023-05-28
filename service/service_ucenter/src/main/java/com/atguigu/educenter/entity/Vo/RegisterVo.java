package com.atguigu.educenter.entity.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//前端客户 注册 实体类 不仅仅包含ucenter_member属性还包含了验证码是否正确
@Data
public class RegisterVo {
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "验证码")
    private String code;
}
