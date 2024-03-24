package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "expense_participant")
public class ExpenseParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonIgnore
    @ManyToOne
    private Expense expense;
    @ManyToOne
    private Participant participant;
    private int share;
    private boolean owner = false;
}
