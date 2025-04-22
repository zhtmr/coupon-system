package sample.consumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sample.consumer.domain.Coupon;
import sample.consumer.domain.FailedEvent;
import sample.consumer.repository.CouponRepository;
import sample.consumer.repository.FailedEventRepository;

@Component
public class CouponCreatedConsumer {

  private final Logger logger = LoggerFactory.getLogger(CouponCreatedConsumer.class);

  private final CouponRepository couponRepository;
  private final FailedEventRepository failedEventRepository;

  public CouponCreatedConsumer(CouponRepository couponRepository, FailedEventRepository failedEventRepository) {
    this.couponRepository = couponRepository;
    this.failedEventRepository = failedEventRepository;
  }

  @KafkaListener(topics = "coupon_create", groupId = "group_1")
  public void listener(Long userId) {
    try {
      couponRepository.save(new Coupon(userId));
    } catch (Exception e) {
      logger.error("failed to create coupon::%d".formatted(userId));
      failedEventRepository.save(new FailedEvent(userId));
    }
  }
}
