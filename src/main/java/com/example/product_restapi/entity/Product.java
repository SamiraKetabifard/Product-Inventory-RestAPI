package com.example.product_restapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@Table(name="product_item")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
}
