package com.zsy.util;


import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"success"),
    USERNAME_ERROR(501,"usernameError"),
    PASSWORD_ERROR(503,"passwordError"),
    NOTLOGIN(504,"无法连接"),
    USERNAME_USED(505,"userNameUsed");

    private Integer code;
    private String message;

   ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
