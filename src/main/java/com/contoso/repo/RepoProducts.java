package com.contoso.repo;

import com.contoso.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoProducts extends JpaRepository<Products, Long> {
    public List<Products> findAllByOrderByNameModel();

    public List<Products> findAllByOrderByIdDesc();
}
