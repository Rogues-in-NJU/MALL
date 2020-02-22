package edu.nju.mall.service;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-22 14:07
 */
public interface AdminUserService {
    boolean checkLogin(String phoneNumber, String password);
}
