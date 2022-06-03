package com.contoso.repo;

import com.contoso.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoProducts extends JpaRepository<Products, Long> {
    List<Products> findAllByOrderByName();
    Products findByName(String name);

    List<Products> findAllByOrderByIdDesc();
}
