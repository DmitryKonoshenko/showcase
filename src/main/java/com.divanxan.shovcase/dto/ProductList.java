package com.divanxan.shovcase.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

public class ProductList implements Serializable {
    @Getter
    @Setter
    private List<Product> productList;
}
