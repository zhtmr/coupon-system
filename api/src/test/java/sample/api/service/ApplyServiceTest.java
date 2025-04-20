package sample.api.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sample.api.repository.CouponRepository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ApplyServiceTest {

  @Autowired
  private ApplyService applyService;

  @Autowired
  private CouponRepository couponRepository;

  @Test
  void 한번만응모() {
    // given
    applyService.apply(1L);

    // when
    long count = couponRepository.count();

    // then
    assertEquals(1, count);

  }

  @Test
  void 여러명응모() throws Exception {
    // given
    int threadCount = 1000;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      long userId = i;
      executorService.submit(() -> {
        try {
          applyService.apply(userId);
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

    // when
    long count = couponRepository.count();
    // then
    assertEquals(100, count);

  }


}
