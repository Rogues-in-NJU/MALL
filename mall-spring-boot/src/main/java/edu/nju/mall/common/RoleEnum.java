package edu.nju.mall.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  RoleEnum {

    ADMIN(1, "admin", "管理员"),
    USER(2, "user", "普通用户");

    private long id;
    private String name;
    private String description;

}
