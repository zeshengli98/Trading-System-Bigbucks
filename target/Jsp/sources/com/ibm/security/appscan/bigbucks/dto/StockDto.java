package com.ibm.security.appscan.bigbucks.dto;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class StockDto {
    private String symbol;
    private BigDecimal price;
//    private BigDecimal change;
//    private String currency;
//    private BigDecimal bid;


}
