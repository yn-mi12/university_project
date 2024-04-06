package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String label;
    private String color;

    @JsonIgnore
    @OneToMany(mappedBy = "tag")
    private Set<Expense> expenses;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private Event event;

    @SuppressWarnings("unused")
    public Tag() {}

    /**
     * The constructor for the Tag class
     * @param label - The label of the Tag
     * @param color - The color of the Tag
     */
    public Tag(String label, String color) {
        this.label = label;
        this.color = color;
    }

    /**
     * Getter for the id of the Tag
     * @return - The id of the Tag
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the label of the Tag
     * @return - The label of the Tag
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter for the label of the Tag
     * @param label - The new label of the Tag
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for the color of the Tag
     * @return - The color of the Tag
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter for the color of the Tag
     * @param color - The new color of the Tag
     */
    public void setColor(String color) {
        this.color = color;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(label, tag.label)
                && Objects.equals(color, tag.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, color);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
