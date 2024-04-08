package server.database;

import commons.Event;
import commons.Expense;
import commons.ExpenseParticipant;
import commons.Participant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"},
        showSql = false)
class ExpenseRepositoryTest {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    private Event ev1;
    private Event ev2;
    private Participant p1;
    private Participant p2;
    private Participant p3;
    private Participant p4;
    private Participant p5;
    private Participant p6;
    private Expense e1;
    private Expense e2;
    private Expense e3;

    @BeforeEach
    void setUp() {
        ev1 = new Event("First event");
        ev2 = new Event("Second Event");
        eventRepository.save(ev1);
        eventRepository.save(ev2);
        eventRepository.flush();

        p1 = new Participant("1name", "1st", null, null, null, null);
        p2 = new Participant("2name", "2nd", null, null, null, null);
        p3 = new Participant("3name", "3rd", null, null, null, null);
        p4 = new Participant("4name", "4th", null, null, null, null);
        p5 = new Participant("5name", "5th", null, null, null, null);
        p6 = new Participant("6name", "6th", null, null, null, null);
        p1.setEvent(ev1);
        p2.setEvent(ev1);
        p3.setEvent(ev1);
        p4.setEvent(ev2);
        p5.setEvent(ev2);
        p6.setEvent(ev2);
        p1 = participantRepository.save(p1);
        p2 = participantRepository.save(p2);
        p3 = participantRepository.save(p3);
        p4 = participantRepository.save(p4);
        p5 = participantRepository.save(p5);
        p6 = participantRepository.save(p6);
        participantRepository.flush();

        e1 = new Expense("expense1","EUR",10, Date.valueOf(LocalDate.now()));
        e1.setEvent(ev1);
        Set<ExpenseParticipant> d1 = new HashSet<>();
        d1.add(new ExpenseParticipant(e1, p1, 0, true));
        d1.add(new ExpenseParticipant(e1, p2, 100, false));
        e1.setDebtors(d1);
        e1 = expenseRepository.saveAndFlush(e1);

        e2 = new Expense("expense2","EUR",20, Date.valueOf(LocalDate.now()));
        e2.setEvent(ev1);
        Set<ExpenseParticipant> d2 = new HashSet<>();
        d2.add(new ExpenseParticipant(e2, p1, 0, false));
        d2.add(new ExpenseParticipant(e2, p2, 46, true));
        d2.add(new ExpenseParticipant(e2, p3, 54, false));
        e2.setDebtors(d2);
        e2 = expenseRepository.saveAndFlush(e2);

        e3 = new Expense("expense3","EUR",30, Date.valueOf(LocalDate.now()));
        e3.setEvent(ev2);
        Set<ExpenseParticipant> d3 = new HashSet<>();
        d3.add(new ExpenseParticipant(e3, p4, 0, true));
        d3.add(new ExpenseParticipant(e3, p5, 100, false));
        e3.setDebtors(d3);
        e3 = expenseRepository.saveAndFlush(e3);
    }

    @AfterEach
    void tearDown() {
        expenseRepository.delete(e1);
        expenseRepository.delete(e2);
        expenseRepository.delete(e3);
        expenseRepository.flush();
        participantRepository.delete(p1);
        participantRepository.delete(p2);
        participantRepository.delete(p3);
        participantRepository.delete(p4);
        participantRepository.delete(p5);
        participantRepository.delete(p6);
        participantRepository.flush();
        eventRepository.delete(ev1);
        eventRepository.delete(ev2);
        eventRepository.flush();
    }

    @Test
    void noEvent() {
        e1.setEvent(null);
        assertThrows(DataIntegrityViolationException.class, () -> expenseRepository.saveAndFlush(e1));
    }

    @Test
    void noExpenseTitle() {
        e1.setDescription(null);
        assertThrows(DataIntegrityViolationException.class, () -> expenseRepository.saveAndFlush(e1));
    }

    @Test
    void noDate() {
        e1.setDate(null);
        assertThrows(DataIntegrityViolationException.class, () -> expenseRepository.saveAndFlush(e1));
    }

    @Test
    void noCurrencySet() {
        e1.setCurrency(null);
        assertThrows(DataIntegrityViolationException.class, () -> expenseRepository.saveAndFlush(e1));
    }

    @Test
    void findByEventId() {
        assertEquals(List.of(e1,e2), expenseRepository.findByEventId(ev1.getId()));
        assertEquals(List.of(e3), expenseRepository.findByEventId(ev2.getId()));
        assertEquals(List.of(), expenseRepository.findByEventId("n"));
    }

    @Test
    void findByParticipantId() {
        assertEquals(List.of(e1, e2), expenseRepository.findByParticipantId(p1.getId()));
        assertEquals(List.of(e1, e2), expenseRepository.findByParticipantId(p2.getId()));
        assertEquals(List.of(e2), expenseRepository.findByParticipantId(p3.getId()));
        assertEquals(List.of(e3), expenseRepository.findByParticipantId(p4.getId()));
        assertEquals(List.of(e3), expenseRepository.findByParticipantId(p5.getId()));
        assertEquals(List.of(), expenseRepository.findByParticipantId(p6.getId()));
        assertEquals(List.of(), expenseRepository.findByParticipantId(-1L));
    }
}
