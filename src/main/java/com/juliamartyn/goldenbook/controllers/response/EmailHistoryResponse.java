package com.juliamartyn.goldenbook.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailHistoryResponse {
    private Integer id;
    private String emailType;
    private Integer orderId;
    private String buyer;
    private Boolean availableToResend;
}
