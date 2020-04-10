package edu.nju.mall.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayResultResponseDTO {

    private String return_code;

    private String return_msg;

}
