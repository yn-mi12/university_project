package commons;

import java.util.List;

public interface EventRepository {
    List<EventTemp> getAllEvents();
    EventTemp getEventByInviteCode(String inviteCode);
    void createEvent(EventTemp event);

    void updateEvent(EventTemp event);
}
