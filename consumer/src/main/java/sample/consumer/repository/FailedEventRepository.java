package sample.consumer.repository;

import org.springframework.data.repository.CrudRepository;
import sample.consumer.domain.FailedEvent;

public interface FailedEventRepository extends CrudRepository<FailedEvent, Long> {
}
