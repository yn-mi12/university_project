package commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventRepoStorage implements EventRepository {
    private List<Event> events;

    /**
     * Creates a new Event list
     */
    public EventRepoStorage() {
        this.events = new ArrayList<>();
    }

    /***
     * Gets all the events in a list
     * @return all events
     */
    @Override
    public List<Event> getAllEvents() {
        return events;
    }

    /***
     * Gets the event that corresponds to the inviteCode
     * @param inviteCode invite code of the desired event
     * @return the event corresponding the invite code or null if it is not found
     */
    @Override
    public Event getEventByInviteCode(String inviteCode) {
        return events.stream()
                .filter(event -> inviteCode.equals(event.getInviteCode()))
                .findFirst()
                .orElse(null);
    }

    /***
     * Creates a new event
     * @param event the event to be added
     */
    @Override
    public void createEvent(Event event) {
        events.add(event);
    }

    /***
     * Updates a specific event
     * @param updatedEvent the event to be replaced/updated
     */
    @Override
    public void updateEvent(Event updatedEvent) {
        for(int i = 0; i < events.size(); i++)
        {
            if(events.get(i).getInviteCode().equals(updatedEvent.getInviteCode())){
                events.remove(i);
                events.add(updatedEvent);
                break;
            }
        }
    }

    /***
     * Setter for the events
     * @param events the new events list
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /***
     * Basic equals method
     * @param o the object to be compared with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventRepoStorage that = (EventRepoStorage) o;
        return Objects.equals(events, that.events);
    }

    /***
     * Hash code generator for the events
     * @return hashcode of the events
     */
    @Override
    public int hashCode() {
        return Objects.hash(events);
    }

    /***
     * Gives a human-friendly representation of the events list
     * @return the events list in a human-friendly String
     */
    @Override
    public String toString() {
        return "EventRepoStorage{" +
                "events=" + events +
                '}';
    }
}
