package com.poly.productmanagement.service;

import com.poly.productmanagement.entities.Product;
import com.poly.productmanagement.repository.productRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceProduct implements IServiceProduct {

    private productRepository pr;

    @Override
    public void addProduct(Product p) {
         pr.save(p);
    }

    @Override
    public void updateProduct(Product p) {
        pr.save(p);
    }

    @Override
    public void deleteProduct(Long id) {
        pr.deleteById(id);
    }

    @Override
    public List<Product> getProductByMC(String mc) {
        return pr.findProductByMC(mc);
    }

    @Override
    public List<Product> getProducts() {
        return pr.findAll();
    }
}
