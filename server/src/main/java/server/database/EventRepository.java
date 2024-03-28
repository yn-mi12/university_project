package server.database;

import commons.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByInviteCode(String inviteCode);

    boolean existsByInviteCode(String inviteCode);
}
