package edu.nju.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {

    private Long id;

    private String name;

    private int classificationId;

    private String classificationName;

    private double buyingPrice;

    private double price;

    private double percent;

    private int quantity;

    private String sellStartTime;

    private String sellEndTime;

    private int status;

    private List<String> imageAddresses;

    private List<String> imageInfoAddresses;
}
