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

        debtor = new Participant("a", "b", null, null, null, null);
        debtor.setId(1);
        creditor = new Participant("c", "d", null, null, null, null);
        creditor.setId(2);
        event = new Event("Test");
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

        var br1 = debtc.save("x", d);
        var br2 = debtc.save(event.getId(), new Debt(new Participant("John", "Doe", null, null, null, null), creditor, 100));
        var br3 = debtc.save(event.getId(), new Debt(debtor, new Participant("Jane", "Doe", null, null, null, null), 100));
        assertEquals(BAD_REQUEST, br1.getStatusCode());
        assertEquals(BAD_REQUEST, br2.getStatusCode());
        assertEquals(BAD_REQUEST, br3.getStatusCode());
    }

    @Test
    void getAllPaidDebtsTest() {
        var br = debtc.getAllPaidDebts(-1L);
        assertEquals(BAD_REQUEST, br.getStatusCode());

        List<Debt> creditorDebts = debtc.getAllPaidDebts(creditor.getId()).getBody();
        assertTrue(repo.calledMethods.contains("getAllByCreditorId"));
        assertEquals(List.of(d), creditorDebts);
    }

}
