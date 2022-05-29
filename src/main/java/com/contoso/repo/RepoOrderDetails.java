package com.contoso.repo;

import com.contoso.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoOrderDetails extends JpaRepository<OrderDetails, Long> {
    List<OrderDetails> findByIdOrder(Long idOrder);

    List<OrderDetails> findByIdProduct(Long idProduct);
}
