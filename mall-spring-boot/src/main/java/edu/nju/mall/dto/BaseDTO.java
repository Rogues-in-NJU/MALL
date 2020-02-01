package edu.nju.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO {

    protected Long id;

    protected String createdAt;

    protected String updatedAt;

    protected String deletedAt;

}
