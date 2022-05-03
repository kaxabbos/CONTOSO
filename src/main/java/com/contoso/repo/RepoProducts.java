package com.contoso.repo;

import com.contoso.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoProducts extends JpaRepository<Products, Long> {


}
