package commons.dto;

public class ParticipantDTO {
    private long id;

    private String firstName;
    private String lastName;
    //Optional parameter. Will not be part of equals or hashcode.
    private String email;
    private EventDTO event;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public ParticipantDTO(long id, String firstName, String lastName, String email, EventDTO event) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.event = event;
    }
    public ParticipantDTO(){

    }
}
