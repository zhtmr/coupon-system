package sample.api.service;

import org.springframework.stereotype.Service;
import sample.api.producer.CouponCreateProducer;
import sample.api.repository.AppliedUserRepository;
import sample.api.repository.CouponCountRepository;
import sample.api.repository.CouponRepository;

@Service
public class ApplyService {

  private final CouponRepository couponRepository;

  private final CouponCountRepository couponCountRepository;
  private final CouponCreateProducer couponCreateProducer;
  private final AppliedUserRepository appliedUserRepository;

  public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository,
      CouponCreateProducer couponCreateProducer, AppliedUserRepository appliedUserRepository) {
    this.couponRepository = couponRepository;
    this.couponCountRepository = couponCountRepository;
    this.couponCreateProducer = couponCreateProducer;
    this.appliedUserRepository = appliedUserRepository;
  }

  public void apply(Long userId) {
    Long applyResult = appliedUserRepository.add(userId);
    if (isApplyFailed(applyResult)) {
      return;
    }

    Long count = couponCountRepository.increment();  // 쿠폰 갯수는 싱글스레드(redis)로 동작하므로 레이스컨디션 일어나지 않음

    if (count > 100) {
      return;
    }

    couponCreateProducer.create(userId);   // kafka 를 통해 요청 폭주를 방지 할 수 있다. kafka 는 메시지를 순차적으로 처리함
  }

  private boolean isApplyFailed(Long applyResult) {
    // SADD : 추가된 경우 1, 중복인 경우 0
    return applyResult != 1;
  }
}
