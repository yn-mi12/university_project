package commons.dto;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
public class EventDTO {
    private long id;
    private String title;
    private String inviteCode;
    private List<ExpenseDTO> expenses;
    private List<ParticipantDTO> participants;
    public EventDTO() {
    }

    public EventDTO(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInviteCode() {
        if(inviteCode == null)
            this.inviteCode = UUID.randomUUID().toString();
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public List<ExpenseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseDTO> expenses) {
        this.expenses = expenses;
    }

    public List<ParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantDTO> participants) {
        this.participants = participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDTO eventDTO = (EventDTO) o;
        return id == eventDTO.id && Objects.equals(title, eventDTO.title) &&
                Objects.equals(inviteCode, eventDTO.inviteCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, inviteCode, expenses, participants);
    }
}
