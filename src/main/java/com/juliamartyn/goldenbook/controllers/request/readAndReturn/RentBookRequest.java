package com.juliamartyn.goldenbook.controllers.request.readAndReturn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentBookRequest {
    private Integer tariffId;
    private Integer rentDaysNumber;
}
