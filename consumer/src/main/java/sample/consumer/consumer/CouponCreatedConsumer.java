package sample.consumer.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sample.consumer.domain.Coupon;
import sample.consumer.repository.CouponRepository;

@Component
public class CouponCreatedConsumer {

  private final CouponRepository couponRepository;

  public CouponCreatedConsumer(CouponRepository couponRepository) {
    this.couponRepository = couponRepository;
  }

  @KafkaListener(topics = "coupon_create", groupId = "group_1")
  public void listener(Long userId) {
    couponRepository.save(new Coupon(userId));
  }
}
