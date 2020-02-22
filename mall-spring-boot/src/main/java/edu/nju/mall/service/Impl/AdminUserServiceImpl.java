package edu.nju.mall.service.Impl;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.entity.Admin;
import edu.nju.mall.repository.AdminRepository;
import edu.nju.mall.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-22 14:07
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private AdminRepository adminRepository;

    public boolean checkLogin(String phoneNumber, String password) {
        Admin admin = adminRepository.findDistinctByPhoneNumber(phoneNumber);
        if (admin == null) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "不存在该手机用户！");
        }
        return password.equals(admin.getPassword());
    }
}
