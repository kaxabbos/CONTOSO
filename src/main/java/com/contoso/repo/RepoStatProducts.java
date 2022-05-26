package com.contoso.repo;

import com.contoso.models.StatProducts;
import com.contoso.models.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoStatProducts extends JpaRepository<StatProducts, Long> {
    StatProducts findByIdOrderDetails(Long id);

    List<StatProducts> findByProductStatusAndDateOrderByIdDesc(ProductStatus productStatus, String date);

    List<StatProducts> findAllByOrderByIdDesc();

    List<StatProducts> findByDateOrderByIdDesc(String date);

    List<StatProducts> findByProductStatusOrderByIdDesc(ProductStatus productStatus);
}
