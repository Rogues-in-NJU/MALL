package edu.nju.mall.dto;

import edu.nju.mall.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends BaseDTO {

    private String openid;

    private String sessionKey;

    private String nickname;

    private Integer gender;

    private String avatarUrl;

    private String country;

    private String province;

    private String city;

    private List<Role> roles;

}
