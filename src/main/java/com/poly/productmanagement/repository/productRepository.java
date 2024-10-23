package com.poly.productmanagement.repository;

import com.poly.productmanagement.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface productRepository extends JpaRepository<Product, Long> {

        //public List<Product> findBynameContain(String name);

    @Query("select p from Product p where p.name like %:name%")
    public List<Product> findProductByMC(String name);

}
