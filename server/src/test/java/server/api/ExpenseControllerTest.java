package server.api;

import org.junit.jupiter.api.BeforeEach;

public class ExpenseControllerTest {

    private TestExpenseRepository expenseRepo;
    private TestEventRepository eventRepo;
    private TestParticipantRepository partRepo;
    private ExpenseController expensec;

    @BeforeEach
    void setUp() {
        expenseRepo = new TestExpenseRepository();
        eventRepo = new TestEventRepository();
        partRepo = new TestParticipantRepository();
        expensec = new ExpenseController(expenseRepo, eventRepo, partRepo);
    }
}
