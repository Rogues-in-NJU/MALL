package edu.nju.mall.service;

import com.google.common.base.Preconditions;
import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.dto.UserDTO;
import edu.nju.mall.entity.User;
import edu.nju.mall.entity.UserInfo;
import edu.nju.mall.repository.UserInfoRepository;
import edu.nju.mall.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Objects;

@Slf4j
@Service
public class UserInfoService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Nonnull
    public UserInfo findUserInfo(@Nonnull final Long userId) {
        Preconditions.checkNotNull(userId);
        UserInfo userInfo = userInfoRepository.findByUserId(userId).orElse(null);
        if (Objects.isNull(userInfo)) {
            log.error("用户信息不存在 id为[{}]", userId);
            throw new NJUException(ExceptionEnum.SERVER_ERROR, String.format("用户信息不存在 id为[%d]", userId));
        }
        return userInfo;
    }

    @Nonnull
    public UserInfo findUserInfo(@Nonnull final String openId) {
        Preconditions.checkNotNull(openId);
        UserDTO userDTO = userService.findUser(openId);
        UserInfo userInfo = userInfoRepository.findByUserId(userDTO.getId()).orElse(null);
        if (Objects.isNull(userInfo)) {
            log.error("用户信息不存在 id为[{}]", userDTO.getId());
            throw new NJUException(ExceptionEnum.SERVER_ERROR, String.format("用户信息不存在 id为[%d]", userDTO.getId()));
        }
        return userInfo;
    }

    @Nonnull
    public UserInfo saveUserInfo(@Nonnull UserInfo userInfo) {
        Preconditions.checkNotNull(userInfo);
        return userInfoRepository.save(userInfo);
    }

    @Nonnull
    public UserInfo initUserInfo(@Nonnull Long userId) {
        Preconditions.checkNotNull(userId);
        UserInfo userInfo = UserInfo.builder()
                .userId(userId)
                .withdrawal(0L)
                .subordinateNum(0L)
                .build();
        return userInfoRepository.save(userInfo);
    }

}
