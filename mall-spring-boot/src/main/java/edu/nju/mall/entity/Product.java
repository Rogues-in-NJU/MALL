package edu.nju.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "classification_id")
    private int classificationId;

    @Column(name = "classification_name")
    private String classificationName;

    @Column(name = "buying_price")
    private Integer buyingPrice;

    private Integer price;

    private double percent;

    private int quantity;

    /**
     * 销量
     */
    private int saleVolume;

    @Column(name = "sell_start_time")
    private String sellStartTime;

    @Column(name = "sell_end_time")
    private String sellEndTime;

    private int status;
}
