package io.pi.spring_ecom.repos;

import io.pi.spring_ecom.domain.AppUser;
import io.pi.spring_ecom.service.AppUserService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<AppUser> findByEmailIgnoreCase(String email);

}
