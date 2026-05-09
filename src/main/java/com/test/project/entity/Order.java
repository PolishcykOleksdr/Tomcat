package com.test.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * author: user,
 * date: 09.05.2026
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Order {
    private int id;
    private String date;
    private double cost;
    private List<Product> products;
}
