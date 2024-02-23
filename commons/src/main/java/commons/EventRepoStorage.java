package commons;

import java.util.ArrayList;
import java.util.List;

public class EventRepoStorage implements EventRepository {
    private List<Event> events;

    public EventRepoStorage() {
        this.events = new ArrayList<>();
    }

    @Override
    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }

    @Override
    public Event getEventByInviteCode(String inviteCode) {
        return events.stream()
                .filter(event -> inviteCode.equals(event.getInviteCode()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void createEvent(Event event) {
        events.add(event);
    }

    @Override
    public void updateEvent(Event updatedEvent) {
        // Implement logic to update the event in the repository
    }
}
