package edu.nju.mall.service;

import com.google.common.base.Preconditions;
import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.dto.InfoSecurityUserDTO;
import edu.nju.mall.dto.UserDTO;
import edu.nju.mall.entity.Subordinate;
import edu.nju.mall.repository.SubordinateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SubordinateService {

    @Autowired
    private UserService userService;

    @Autowired
    private SubordinateRepository subordinateRepository;

    public List<InfoSecurityUserDTO> getSubordinates(@Nonnull final Long userId) {
        Preconditions.checkNotNull(userId);

        List<Subordinate> subordinates = subordinateRepository.findByUserId(userId);
        List<InfoSecurityUserDTO> infoSecurityUserDTOs = new LinkedList<>();
        for (Subordinate s : subordinates) {
            try {
                UserDTO userDTO = userService.findUser(s.getSubordinateId());
                InfoSecurityUserDTO infoSecurityUserDTO = InfoSecurityUserDTO.builder().build();
                BeanUtils.copyProperties(userDTO, infoSecurityUserDTO);
                infoSecurityUserDTOs.add(infoSecurityUserDTO);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return infoSecurityUserDTOs;
    }

    /**
     * 校验是否存在上级用户
     *
     * @param userId
     * @return
     */
    public boolean check(@Nonnull final Long userId) {
        Preconditions.checkNotNull(userId);
        List<Subordinate> subordinates = subordinateRepository.findBySubordinateId(userId);
        return !CollectionUtils.isEmpty(subordinates);
    }

    /**
     * 获取上级用户ID
     * @param userId
     * @return
     */
    public Long getLeaderId(@Nonnull final Long userId) {
        Preconditions.checkNotNull(userId);
        List<Subordinate> subordinates = subordinateRepository.findBySubordinateId(userId);
        if(CollectionUtils.isEmpty(subordinates)){
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "获取上级用户ID错误！");
        }
        return subordinates.get(0).getUserId();
    }

    public List<InfoSecurityUserDTO> getSubordinates(@Nonnull final String openId) {
        Preconditions.checkNotNull(openId);

        UserDTO userDTO = userService.findUser(openId);
        return this.getSubordinates(userDTO.getId());
    }

    public void addSubordinate(Long sharedUserId, Long userId) {
        if (Objects.isNull(sharedUserId)) {
            return;
        }
        if (sharedUserId.equals(userId)) {
            return;
        }
        List<Subordinate> subordinates = subordinateRepository.findBySubordinateId(userId);
        if (subordinates.size() != 0) {
            return;
        } else {
            Subordinate subordinate = Subordinate.builder()
                    .userId(sharedUserId)
                    .subordinateId(userId)
                    .build();
            subordinateRepository.save(subordinate);
            return;
        }
    }

}
