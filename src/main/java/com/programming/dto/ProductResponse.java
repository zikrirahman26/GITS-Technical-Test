package com.programming.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private String name;
    private String skuCode;
    private String imageUrl;
    private Double price;
    private String category;
    private String description;
}
