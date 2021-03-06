package edu.nju.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 13:50
 */

@Data
@Builder
@Entity
@Table(name = "withdrawal_condition")
@NoArgsConstructor
@AllArgsConstructor
@Proxy(lazy = false)
public class WithdrawalCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int member;

    private Long cash;
}
