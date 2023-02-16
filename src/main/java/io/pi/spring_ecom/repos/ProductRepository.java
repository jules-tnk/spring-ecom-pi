package io.pi.spring_ecom.repos;

import io.pi.spring_ecom.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
}
