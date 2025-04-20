package sample.api.service;

import org.springframework.stereotype.Service;
import sample.api.domain.Coupon;
import sample.api.repository.CouponCountRepository;
import sample.api.repository.CouponRepository;

@Service
public class ApplyService {

  private final CouponRepository couponRepository;

  private final CouponCountRepository couponCountRepository;

  public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository) {
    this.couponRepository = couponRepository;
    this.couponCountRepository = couponCountRepository;
  }

  public void apply(Long userId) {
    Long count = couponCountRepository.increment();

    if (count > 100) {
      return;
    }

    couponRepository.save(new Coupon(userId));
  }
}
