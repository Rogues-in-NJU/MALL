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
@Table(name = "user_info")
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 提现额度
     * */
    @Column(name = "withdrawal", nullable = false)
    private Double withdrawal;

    /**
     * 下级用户数量
     * */
    @Column(name = "subordinate_num", nullable = false)
    private Long subordinateNum;

}
