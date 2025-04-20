package sample.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.api.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
