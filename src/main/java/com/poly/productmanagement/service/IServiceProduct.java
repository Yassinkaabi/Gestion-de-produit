package com.poly.productmanagement.service;

import com.poly.productmanagement.entities.Product;

import java.util.List;

public interface IServiceProduct {

    public void addProduct(Product p);
    public void updateProduct(Product p);
    public void deleteProduct(Long id);
    public List<Product> getProductByMC(String mc);
    public List<Product> getProducts();
}
