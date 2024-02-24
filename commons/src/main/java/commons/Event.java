package commons;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Event {
    private String title;
    private List<Person> participants;
    private String inviteCode;
    private List<Debt> debts;
    private List<Expense> expenses;
    //I'm not putting creator as a parameter because once the event is created all users have the same rights

    /**
     * Creates an Event object
     * @param creator the user/person that creates the event
     * @param title the title of the event
     * @param inviteCode the code sent to other users inviting them to join the event
     */
    public Event(Person creator, String title, String inviteCode) {
        this.title = title; //user gives the title when creating an event (mandatory!)
        this.participants = new ArrayList<Person>();
        this.participants.add(creator);//the user creating it should be automatically added
        this.inviteCode = inviteCode; //invite code should be generated and passed when an event is created
        this.debts = new ArrayList<Debt>(); //empty list by default
        this.expenses = new ArrayList<Expense>(); //empty list by default
    }

    /**
     * Getter for the title
     * @return the event's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title
     * @param title sets the title
     */
    public void setTitle(String title) {
        this.title = title;
    } //enough for editing the title?

    /**
     * Getter for the participants
     * @return the list of the participants
     */
    public List<Person> getParticipants() {
        return participants;
    }

    /**
     * Setter for the participants
     * @param participants sets the participants
     */
    public void setParticipants(List<Person> participants) {
        this.participants = participants;
    }

    /**
     * Getter for the invite code
     * @return the invite code
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * Setter for the invite code
     * @param inviteCode the invite code
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * Getter for the list of debts
     * @return the list of debts
     */
    public List<Debt> getDebts() {
        return debts;
    }

    /**
     * Setter for the debts list
     * @param debts the list of debts
     */
    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    /**
     * Getter for the list of expenses
     * @return the list of the expenses
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Setter for the list of the expenses
     * @param expenses the list of expenses
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    /**
     * Checks the equality of two Event objects
     * @param o the object to be tested with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;
        return Objects.equals(title, event.title) &&
                Objects.equals(participants, event.participants) &&
                Objects.equals(inviteCode, event.inviteCode) &&
                Objects.equals(debts, event.debts) &&
                Objects.equals(expenses, event.expenses);
    }

    /**
     * Hash code generator for an event
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, participants, inviteCode, debts, expenses);
    }

    /**
     * Gives a human-friendly representation of an event
     * @return the event in a human-friendly way
     */
    @Override
    public String toString() {
        String result = "Event{ " + "\n" +
                "Title: " + title + "\n" +
                "Participants: ";
        for (int i = 0; i < participants.size(); i++){
            result += participants.get(i).toString();
            if (i < participants.size() - 1){
                result += ", ";
            }
        }
        result += "\n" +
                "Invite Code: " + inviteCode + "\n" +
                "Debts: ";
        for (int i = 0; i < debts.size(); i++){
            result += debts.get(i).toString();
            if (i < debts.size() - 1){
                result += ", ";
            }
        }
        result += "\n" +
                "Expenses: ";
        for (int i = 0; i < expenses.size(); i++){
            result += expenses.get(i).toString();
            if (i < expenses.size() - 1){
                result += ", ";
            }
        }
        result += "}";
        return result;
    }


    /**
     * Add an expense to the event
     * @param expense the new expense
     */
     public void addExpense(Expense expense){
        expenses.add(expense);
     }

    /**
     * Remove an expense from the event
     * @param expense the expense to be removed
     */
     public void removeExpense(Expense expense){
        expenses.remove(expense);
     }

    /**
     * Compute and return the expenses sum of the event
     * @return the sum of the expenses
     */
     public double expensesSum(){
        return expenses.stream()
                .mapToDouble(expense -> expense.getAmount())
                .sum();
     }

    /**
     * Adds a participant to the event
     * @param person the participant to be added
     */
     public void addParticipant(Person person){
         participants.add(person);
     }

}
