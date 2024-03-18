package commons;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class ExpenseTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    @JoinColumn(name="paid_by",referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Participant paid;
    private String currency;
    private double amount;
    private Date date;

    public ExpenseTemp() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ExpenseTemp(String description, String currency, double amount, Date date) {
        this.description = description;
        this.currency = currency;
        this.amount = amount;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

