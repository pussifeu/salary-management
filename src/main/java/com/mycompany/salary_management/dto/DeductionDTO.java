package com.mycompany.salary_management.dto;

import lombok.Data;

@Data
public class DeductionDTO {
    private Long id;
    private String type;
    private double amount;
}
