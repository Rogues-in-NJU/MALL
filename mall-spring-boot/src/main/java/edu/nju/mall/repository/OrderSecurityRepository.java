package edu.nju.mall.repository;

import edu.nju.mall.entity.OrderSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderSecurityRepository extends JpaRepository<OrderSecurity, Long> {

    Optional<OrderSecurity> findByNonceStr(final String nonce_str);

}
