package com.juliamartyn.goldenbook.controllers.request.readAndReturn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReadAndReturnTariffPricePerDayRequest {
    private BigDecimal pricePerDay;
}
