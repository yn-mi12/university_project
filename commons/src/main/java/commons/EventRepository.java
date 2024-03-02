package commons;

import java.util.List;

public interface EventRepository {
    List<Event> getAllEvents();
    Event getEventByInviteCode(String inviteCode);
    void createEvent(Event event);

    void updateEvent(Event event);
}
