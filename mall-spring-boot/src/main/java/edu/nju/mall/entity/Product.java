package edu.nju.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-24 14:23
 */
@Data
@SuperBuilder
@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    private String name;
    private int classificationId;
    private double buyingPrice;
    private double price;
    private double percent;
    private String sellTime;
    private int quantity;
    private String sellStartTime;
    private String sellEndTime;
    private int status;
}
