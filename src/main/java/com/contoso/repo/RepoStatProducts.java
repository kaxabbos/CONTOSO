package com.contoso.repo;

import com.contoso.models.StatProducts;
import com.contoso.models.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoStatProducts extends JpaRepository<StatProducts, Long> {
    StatProducts findByIdProduct(Long id);

    StatProducts findByIdOrderDetails(Long id);

    List<StatProducts> findByProductStatusAndDate(ProductStatus productStatus, String date);

    List<StatProducts> findByDate(String date);

    List<StatProducts> findByProductStatus(ProductStatus productStatus);
}
