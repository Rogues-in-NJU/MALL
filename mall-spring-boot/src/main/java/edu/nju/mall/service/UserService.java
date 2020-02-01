package edu.nju.mall.service;

import com.google.common.base.Preconditions;
import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.RoleEnum;
import edu.nju.mall.dto.UserDTO;
import edu.nju.mall.entity.Role;
import edu.nju.mall.entity.User;
import edu.nju.mall.entity.UserRole;
import edu.nju.mall.repository.RoleRepository;
import edu.nju.mall.repository.UserRepository;
import edu.nju.mall.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author hermc
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserDTO findUser(final Long id) {
        User userEntity = userRepository.findById(id).orElse(null);
        if (Objects.isNull(userEntity)) {
            return null;
        }
        UserDTO userDTO = UserDTO.builder().build();
        BeanUtils.copyProperties(userEntity, userDTO);
        userDTO.setRoles(this.findUserRoles(userEntity.getId()));
        return userDTO;
    }

    public UserDTO findUser(final String openid) {
        User userEntity = userRepository.findByOpenid(openid).orElse(null);
        if (Objects.isNull(userEntity)) {
            return null;
        }
        UserDTO userDTO = UserDTO.builder().build();
        BeanUtils.copyProperties(userEntity, userDTO);
        userDTO.setRoles(this.findUserRoles(userEntity.getId()));
        return userDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDTO register(UserDTO userDTO) {
        User userEntity = User.builder().build();
        BeanUtils.copyProperties(userDTO, userEntity);
        userEntity = userRepository.save(userEntity);
        grantDefaultRole(userEntity.getId());
        return this.findUser(userEntity.getId());
    }

    public List<Role> findUserRoles(final Long userId) {
        List<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
        return userRoles.stream()
                .map(userRole -> roleRepository.findById(userRole.getRoleId()).orElse(null))
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDTO grantDefaultRole(Long userId) {
        Role exampleRoleUser = Role.builder()
                .id(RoleEnum.USER.getId())
                .name(RoleEnum.USER.getName())
                .build();
        Role realRole = roleRepository.findOne(Example.of(exampleRoleUser)).orElse(null);
        if (Objects.isNull(realRole)) {
            log.error("no default user role: [{}]", RoleEnum.USER);
            throw ExceptionEnum.SERVER_ERROR.exception("默认权限不存在!");
        }
        addRole(userId, realRole.getId());
        return this.findUser(userId);
    }

    public void addRole(Long userId, Long roleId) {
        Preconditions.checkNotNull(userId, "用户ID为空!");
        Preconditions.checkNotNull(roleId, "权限ID为空!");
        if (Objects.nonNull(userRoleRepository.findByUserIdAndRoleId(userId, roleId))) {
            return;
        }
        UserRole userRole = UserRole.builder()
                .userId(userId)
                .roleId(roleId)
                .build();
        userRoleRepository.save(userRole);
    }

}
