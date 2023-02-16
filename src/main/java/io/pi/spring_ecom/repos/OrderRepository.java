package io.pi.spring_ecom.repos;

import io.pi.spring_ecom.domain.AppUser;
import io.pi.spring_ecom.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByClient_Id(Long id);
    List<Order> findAllByClient(AppUser client);
}
