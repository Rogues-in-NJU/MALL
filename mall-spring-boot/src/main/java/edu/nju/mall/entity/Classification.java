package edu.nju.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:11
 */

@Data
@Builder
@Entity
@Table(name = "classification")
@NoArgsConstructor
@AllArgsConstructor
public class Classification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 添加时间
     */
    @Column(name = "created_time")
    private String createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private String updatedTime;

    /**
     * 删除时间
     */
    @Column(name = "deleted_time")
    private String deletedTime;

    /**
     * 分类状态（0：使用中，1：废弃）
     */
    private Integer status;
}
