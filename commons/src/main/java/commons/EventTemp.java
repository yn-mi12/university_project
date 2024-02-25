
package commons;

import jakarta.persistence.*;

/**
 * This class is temporarily used until we decide on a better structure for the
 * Event and Expense classes that will be representable in the database.
 * It is necessary to either have a new class or fix the old one because of the
 * @OneToOne, @OneToMany flags. Fields expenses, debts and participants in the original
 * class are possibly redundant.
 */
@Entity
public class EventTemp{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String inviteCode;

    public EventTemp(String title, String inviteCode) {
        this.title = title;
        this.inviteCode = inviteCode;
    }

    public EventTemp() {

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
        return inviteCode;
    }

    @Override
    public String toString() {
        return "EventTemp{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                '}';
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
