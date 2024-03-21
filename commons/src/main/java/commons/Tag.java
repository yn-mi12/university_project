package commons;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String label;
    private String color;

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
