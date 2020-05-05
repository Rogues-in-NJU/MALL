package edu.nju.mall.service;

import com.google.common.base.Preconditions;
import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.dto.UserDTO;
import edu.nju.mall.dto.UserInfoDTO;
import edu.nju.mall.entity.User;
import edu.nju.mall.entity.UserInfo;
import edu.nju.mall.repository.UserInfoRepository;
import edu.nju.mall.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    public UserInfoDTO findUserInfo(@Nonnull final Long userId) {
        Preconditions.checkNotNull(userId);
        UserInfo userInfoEntity = findUserInfoEntity(userId);
        UserInfoDTO userInfoDTO = UserInfoDTO.builder().build();
        BeanUtils.copyProperties(userInfoEntity, userInfoDTO);
        // TODO: 订单搜索
        return userInfoDTO;
    }

    @Nonnull
    public UserInfo findUserInfoEntity(@Nonnull final Long userId) {
        Preconditions.checkNotNull(userId);
        UserInfo userInfoEntity = userInfoRepository.findByUserId(userId).orElse(null);
        if (Objects.isNull(userInfoEntity)) {
            log.error("用户信息不存在 id为[{}]", userId);
            throw new NJUException(ExceptionEnum.SERVER_ERROR, String.format("用户信息不存在 id为[%d]", userId));
        }
        return userInfoEntity;
    }


    @Nonnull
    public UserInfoDTO findUserInfo(@Nonnull final String openId) {
        Preconditions.checkNotNull(openId);
        UserDTO userDTO = userService.findUser(openId);
        return this.findUserInfo(userDTO.getId());
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
