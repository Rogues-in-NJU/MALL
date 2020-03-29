package edu.nju.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
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

    private Integer buyingPrice;

    private Integer price;

    private double percent;

    private int quantity;

    private String sellStartTime;

    private String sellEndTime;

    private int status;

    private List<String> imageAddresses;

    private List<String> imageInfoAddresses;

    protected String createdAt;

    protected String updatedAt;

    protected String deletedAt;
}
