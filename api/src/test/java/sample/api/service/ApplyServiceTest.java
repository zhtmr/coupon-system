package sample.api.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
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

//  @Autowired
//  private StringRedisTemplate stringRedisTemplate;


  //  @AfterEach
  //  void cleanupKafkaTopic() throws Exception {
  //    Properties config = new Properties();
  //    // 테스트용 Kafka 주소
  //    String bootstrapServers = "localhost:9092";
  //    config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
  //
  //    try (AdminClient adminClient = AdminClient.create(config)) {
  //      adminClient.deleteTopics(Collections.singletonList("coupon_create")).all().get();
  //      System.out.println("Kafka 토픽 coupon_create 삭제 완료");
  //    }
  //  }

  //  @AfterEach
  //  void resetRedisData() {
  //    // 초기화를 수행할 Redis 키
  //    stringRedisTemplate.opsForValue().set("coupon_count", "0");
  //    System.out.println("Redis의 coupon_count 초기화 완료");
  //  }


  @Test
  void 한번만응모() {
    // given
    applyService.apply(1L);

    // when
    long count = couponRepository.count();

    // then
    assertEquals(1, count);

  }

  // 실행 전 redis flushall 필요
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

    Thread.sleep(10000);

    // when
    long count = couponRepository.count();

    System.out.println("count = " + count);
    // then
    assertEquals(100, count);

  }

  @Test
  void 한명당_한개의쿠폰만_발급() throws Exception {
    // given
    int threadCount = 1000;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          applyService.apply(1L);
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

    Thread.sleep(10000);

    // when
    long count = couponRepository.count();

    System.out.println("count = " + count);
    // then
    assertEquals(1, count);

  }


}
