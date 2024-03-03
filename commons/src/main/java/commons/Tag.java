package commons;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<Expense> expenses;
    private String label;
    private String color;

    /**
     * The constructor for the Tag class
     * @param label - The label of the Tag
     * @param color - The color of the Tag
     */
    public Tag(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public Tag() {
        // For Object mapping
    }

    /**
     * Getter for the id of the Tag
     * @return - The id of the Tag
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for the Expenses this Tag is attached to
     * @return - The list of Expenses
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Set the list of Expenses this Tag is attached to
     * @param expenses - The new list of Expenses
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    /**
     * Add an Expense to the list of Expenses
     * @param expense - The Expense to be added
     */
    public void addExpense(Expense expense) {
        if(expense != null) {
            this.expenses.add(expense);
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id == tag.id && Objects.equals(expenses, tag.expenses) && Objects.equals(label, tag.label)
                && Objects.equals(color, tag.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expenses, label, color);
    }

    @Override
    public String toString() {
        String result = "Tag{" + "id=" + id + ", expenses=";
        if(expenses != null && !expenses.isEmpty()) {
            for(int i = 0; i < expenses.size(); i++) {
                result += expenses.get(i).toString() + ", ";
            }
        }
        result += "label='" + label + '\'' + ", color='" + color + '\'' + '}';
        return result;
    }
}
