package edu.nju.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@SuperBuilder
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(name = "openid", unique = true)
    private String openid;

    @Column(name = "session_key")
    private String sessionKey;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "country")
    private String country;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

}
