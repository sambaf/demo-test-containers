package org.sambasoft.repository;

import java.util.UUID;
import org.sambasoft.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> { }
