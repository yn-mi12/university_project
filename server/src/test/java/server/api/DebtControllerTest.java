package server.api;

import commons.Debt;
import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class DebtControllerTest {

    private TestDebtRepository repo;
    private TestParticipantRepository partRepo;
    private TestEventRepository eventRepo;
    private DebtController debtc;
    private Participant debtor;
    private Participant creditor;
    private Event event;
    private Debt d;

    @BeforeEach
    void setUp() {
        repo = new TestDebtRepository();
        partRepo = new TestParticipantRepository();
        eventRepo = new TestEventRepository();
        debtc = new DebtController(repo, partRepo, eventRepo);

        debtor = new Participant("a", "b");
        debtor.setId(1);
        creditor = new Participant("c", "d");
        creditor.setId(2);
        event = new Event("Test");
        event.setId(1);
        d = new Debt(debtor, creditor, 100);
        partRepo.participants.add(debtor);
        partRepo.participants.add(creditor);
        eventRepo.events.add(event);
        debtc.save(event.getId(), d);
    }

    @Test
    void save() {
        assertTrue(repo.calledMethods.contains("save"));
        assertTrue(repo.debts.contains(d));

        var br1 = debtc.save(Long.valueOf(0), d);
        var br2 = debtc.save(event.getId(), new Debt(new Participant("John", "Doe"), creditor, 100));
        var br3 = debtc.save(event.getId(), new Debt(debtor, new Participant("Jane", "Doe"), 100));
        assertEquals(BAD_REQUEST, br1.getStatusCode());
        assertEquals(BAD_REQUEST, br2.getStatusCode());
        assertEquals(BAD_REQUEST, br3.getStatusCode());
    }

    @Test
    void getByEventIdTest() {
        d.setEvent(event);
        assertEquals(d, debtc.getByEventId(1).getBody().get(0));

        var br = debtc.getByEventId(-1).getStatusCode();
        var br2 = debtc.getByEventId(42).getStatusCode();
        assertEquals(BAD_REQUEST, br);
        assertEquals(BAD_REQUEST, br2);
    }

    @Test
    void updateAmountTest() {
        debtc.updateAmount(repo.debts.get(0).getId(), 1000);
        assertEquals(1000, repo.debts.get(0).getAmount());

        var br = debtc.getByEventId(100).getStatusCode();
        var br2 = debtc.updateAmount(42, 1000).getStatusCode();
        assertEquals(BAD_REQUEST, br);
        assertEquals(BAD_REQUEST, br2);
    }

    @Test
    void getAllByCreditorIdTest() {
        var br = debtc.getAllByCreditorId(Long.valueOf(-1));
        assertEquals(BAD_REQUEST, br.getStatusCode());

        List<Debt> creditorDebts = debtc.getAllByCreditorId(Long.valueOf(creditor.getId())).getBody();
        assertTrue(repo.calledMethods.contains("getAllByCreditorId"));
        assertEquals(List.of(d), creditorDebts);
    }

    @Test
    void getAllByDebtorTest() {
        var br = debtc.getAllByDebtorId(Long.valueOf(-1));
        assertEquals(BAD_REQUEST, br.getStatusCode());

        List<Debt> debtorDebts = debtc.getAllByDebtorId(Long.valueOf(debtor.getId())).getBody();
        assertTrue(repo.calledMethods.contains("getAllByDebtorId"));
        assertEquals(List.of(d), debtorDebts);
    }

    @Test
    void deleteById() {
        debtc.deleteById(repo.debts.get(0).getId());
        assertFalse(repo.debts.contains(d));

        var br = debtc.deleteById(-1);
        var br2 = debtc.deleteById(2);
        assertEquals(BAD_REQUEST, br.getStatusCode());
        assertEquals(BAD_REQUEST, br2.getStatusCode());
    }
}
