package com.contoso.repo;

import com.contoso.models.Orders;
import com.contoso.models.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoOrders extends JpaRepository<Orders, Long> {
    List<Orders> findAllByOrderByIdDesc();
    List<Orders> findByOrderStatus(OrderStatus orderStatus);
    List<Orders> findByOrderStatusOrderByIdDesc(OrderStatus orderStatus);
}
