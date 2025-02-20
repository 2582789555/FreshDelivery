package org.example.freshdeliveryserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLoginTimeRequest {
    private BigDecimal latitude;
    private BigDecimal longitude;
}
