package server.database;

import commons.EventTemp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventTemp, Long> {
}
