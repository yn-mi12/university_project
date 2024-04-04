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

    @BeforeEach
    void setUp() {
        repo = new TestDebtRepository();
        partRepo = new TestParticipantRepository();
        eventRepo = new TestEventRepository();
        debtc = new DebtController(repo, partRepo, eventRepo);
    }

    @Test
    void save() {
        Participant debtor = new Participant("a", "b");
        debtor.setId(1);
        Participant creditor = new Participant("c", "d");
        creditor.setId(2);
        Event event = new Event("Test");
        event.setId(1);
        Debt d = new Debt(debtor, creditor, 100);
        d.setEvent(event);
        partRepo.participants.add(debtor);
        partRepo.participants.add(creditor);
        eventRepo.events.add(event);

        debtc.save(event.getId(), d);
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
    void getAllByCreditorIdTest() {
        Participant debtor = new Participant("a", "b");
        debtor.setId(1);
        Participant creditor = new Participant("c", "d");
        creditor.setId(2);
        Event event = new Event("Test");
        event.setId(1);
        Debt d = new Debt(debtor, creditor, 100);
        d.setEvent(event);
        partRepo.participants.add(debtor);
        partRepo.participants.add(creditor);
        eventRepo.events.add(event);
        debtc.save(event.getId(), d);

        var br = debtc.getAllByCreditorId(Long.valueOf(-1));
        assertEquals(BAD_REQUEST, br.getStatusCode());

        List<Debt> creditorDebts = debtc.getAllByCreditorId(Long.valueOf(creditor.getId())).getBody();
        assertTrue(repo.calledMethods.contains("getAllByCreditorId"));
        assertEquals(List.of(d), creditorDebts);
    }
}
