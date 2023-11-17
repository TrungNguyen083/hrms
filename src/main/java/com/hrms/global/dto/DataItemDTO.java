package com.hrms.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataItemDTO {
    private String label;
    private Float value;

    public DataItemDTO(String current, Double aDouble) {
        this.label = current;
        this.value = aDouble.floatValue();
    }
}
