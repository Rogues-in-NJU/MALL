package edu.nju.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-03-01 18:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    private String phoneNumber;
    private String password;
}
