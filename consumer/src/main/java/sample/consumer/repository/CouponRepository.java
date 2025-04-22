package sample.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.consumer.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
