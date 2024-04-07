package server.api;

import commons.Event;
import commons.Expense;
import commons.ExpenseParticipant;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

public class ExpenseControllerTest {


    private TestEventRepository eventRepo;
    private TestParticipantRepository participantRepo;
    private TestExpenseRepository expenseRepo;
    private ExpenseController ec;

    private Event e1;
    private Event e2;
    private Event e3;
    private Participant p1;
    private Participant p2;
    private Participant p3;
    private Participant p4;
    private Expense ex1;
    private Expense ex2;
    private Expense ex3;
    ExpenseParticipant d;
    @BeforeEach
    void SetUp() {
        eventRepo = new TestEventRepository();
        participantRepo = new TestParticipantRepository();
        expenseRepo = new TestExpenseRepository();
        ec = new ExpenseController(expenseRepo, eventRepo, participantRepo);

        e1 = new Event("X");
        e2 = new Event("Z");
        eventRepo.save(e1);
        eventRepo.save(e2);
        e1.setParticipants(new ArrayList<>());
        e2.setParticipants(new ArrayList<>());


        p1 = new Participant("pa", "pb");
        p1.setEvent(e1);
        p1.setExpenses(new HashSet<>());
        participantRepo.save(p1);
        e1.addParticipant(p1);

        p2 = new Participant("a", "b", "c");
        p2.setEvent(e1);
        p2.setExpenses(new HashSet<>());
        participantRepo.save(p2);
        e1.addParticipant(p2);

        p3 = new Participant("d", "b");
        p3.setEvent(e2);
        p3.setExpenses(new HashSet<>());
        participantRepo.save(p3);
        e2.addParticipant(p3);

        p4 = new Participant("empty", "expenses");
        p4.setEvent(e2);
        p4.setExpenses(new HashSet<>());
        participantRepo.save(p4);
        e2.addParticipant(p4);

        ex1 = new Expense("Expense 1", "EUR", 40, Date.valueOf(LocalDate.now()));
        ex1.setDebtors(new HashSet<>());

        d = new ExpenseParticipant(ex1, p1, 50, true);
        d.setId(1);
        ex1.getDebtors().add(d);
        p1.getExpenses().add(d);
        d = new ExpenseParticipant(ex1, p2, 50, false);
        d.setId(2);
        ex1.getDebtors().add(d);
        p2.getExpenses().add(d);

        ex3 = new Expense("Expense 1", "EUR", 40, Date.valueOf(LocalDate.now()));
        ex3.setDebtors(new HashSet<>());

        d = new ExpenseParticipant(ex3, p1, 99, true);
        d.setId(5);
        ex3.getDebtors().add(d);
        p1.getExpenses().add(d);
        d = new ExpenseParticipant(ex3, p2, 1, false);
        d.setId(4);
        ex3.getDebtors().add(d);
        p2.getExpenses().add(d);

        ex2 = new Expense("Expense 2", "EUR", 40, Date.valueOf(LocalDate.now()));
        ex2.setDebtors(new HashSet<>());

        d = new ExpenseParticipant(ex2, p3, 100, true);
        d.setId(3);
        ex2.getDebtors().add(d);
        p3.getExpenses().add(d);
    }

    @Test
    void saveBad1NoEvent() {
        var actual = ec.save(ex1, "");
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void saveBad2NoParticipantDoesntExist() {

        ex2 = new Expense("Expense 2", "EUR", 40, Date.valueOf(LocalDate.now()));
        ex2.setDebtors(new HashSet<>());

        d = new ExpenseParticipant(ex2, new Participant("dsa","dwa"), 100, true);
        d.setId(3);
        ex2.getDebtors().add(d);

        var actual = ec.save(ex2, e2.getId());
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void saveBad3ParticipantInWrongEvent() {

        ex2 = new Expense("Expense 2", "EUR", 40, Date.valueOf(LocalDate.now()));
        ex2.setDebtors(new HashSet<>());

        d = new ExpenseParticipant(ex2, p1, 100, true);
        d.setId(3);
        ex2.getDebtors().add(d);
        p1.getExpenses().add(d);

        var actual = ec.save(ex2, e2.getId());
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void correctSave() {
        assertEquals(0, expenseRepo.count());
        var req1 = ec.save(ex1, e1.getId());
        assertEquals(OK, req1.getStatusCode());
        assertEquals(1, expenseRepo.count());
        var req2 = ec.save(ex2, e2.getId());
        assertEquals(2, expenseRepo.count());
        assertEquals(OK, req2.getStatusCode());
    }

    @Test
    void saveBadExpensesNoDescription() {
        ex2.setDescription("");
        var req1 = ec.save(ex2, e2.getId());
        assertEquals(BAD_REQUEST, req1.getStatusCode());
    }

    @Test
    void saveBadExpensesNoCurrency() {
        ex2.setCurrency("");
        var req1 = ec.save(ex2, e2.getId());
        assertEquals(BAD_REQUEST, req1.getStatusCode());
    }

    @Test
    void saveBadExpensesNoDate() {
        ex2.setDate(null);
        var req1 = ec.save(ex2, e2.getId());
        assertEquals(BAD_REQUEST, req1.getStatusCode());
    }


    @Test
    void deleteById() {
        ec.save(ex1, e1.getId());
        ec.save(ex2, e2.getId());
        assertEquals(2, expenseRepo.count());
        var id = ex1.getId();
        var req = ec.deleteById(id);
        assertEquals(OK,req.getStatusCode());
        req = ec.deleteById(id);
        assertEquals(NOT_FOUND, req.getStatusCode());
        assertEquals(1,expenseRepo.count());
    }

    @Test
    void deleteByIdBad() {
        var req = ec.deleteById(-1L);
        assertEquals(NOT_FOUND, req.getStatusCode());
    }

    @Test
    void returnByID() {
        ec.save(ex1, e1.getId());
        ec.save(ex2, e2.getId());
        var actual = ec.getById(ex1.getId());
        assertEquals(ex1, actual.getBody());
        assertEquals(OK, actual.getStatusCode());
        assertEquals(NOT_FOUND, ec.getById(-1L).getStatusCode());
    }

    @Test
    void returnByParticipant() {
        ec.save(ex1, e1.getId());
        ec.save(ex3, e1.getId());
        ec.save(ex2, e2.getId());

        assertEquals(NOT_FOUND, ec.findAllByParticipantId(-1L).getStatusCode());

        var req1 = ec.findAllByParticipantId(p1.getId());
        assertEquals(OK, req1.getStatusCode());
        assertEquals(List.of(ex1,ex3), req1.getBody());

        req1 = ec.findAllByParticipantId(p2.getId());
        assertEquals(OK, req1.getStatusCode());
        assertEquals(List.of(ex1,ex3), req1.getBody());

        req1 = ec.findAllByParticipantId(p3.getId());
        assertEquals(OK, req1.getStatusCode());
        assertEquals(List.of(ex2), req1.getBody());

        req1 = ec.findAllByParticipantId(p4.getId());
        assertEquals(OK, req1.getStatusCode());
        assertEquals(List.of(), req1.getBody());
    }

    @Test
    void returnByEventId() {
        ec.save(ex1, e1.getId());
        ec.save(ex3, e1.getId());
        ec.save(ex2, e2.getId());

        e3 = new Event("XX");
        eventRepo.save(e3);


        assertEquals(NOT_FOUND, ec.getByEventId("").getStatusCode());

        var req1 = ec.getByEventId(e1.getId());
        assertEquals(OK, req1.getStatusCode());
        assertEquals(List.of(ex1,ex3), req1.getBody());

        req1 = ec.getByEventId(e2.getId());
        assertEquals(OK, req1.getStatusCode());
        assertEquals(List.of(ex2), req1.getBody());

        req1 = ec.getByEventId(e3.getId());
        assertEquals(OK, req1.getStatusCode());
        assertEquals(List.of(), req1.getBody());

    }

}
