package com.talentica.workshop.order.repository;

import com.talentica.workshop.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> { // Note: Integer for ID
}