package server.api;

import org.springframework.http.ResponseEntity;
import commons.Debt;
import org.springframework.web.bind.annotation.*;
import server.database.DebtRepository;
import server.database.EventRepository;
import server.database.ParticipantRepository;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
public class DebtController {

    private final DebtRepository repo;
    private final ParticipantRepository partRepo;
    private final EventRepository eventRepo;

    public DebtController(DebtRepository repo, ParticipantRepository partRepo, EventRepository eventRepo) {
        this.repo = repo;
        this.partRepo = partRepo;
        this.eventRepo = eventRepo;
    }

    @PostMapping({"/event/{ev_id}"})
    public ResponseEntity<Debt> save(@PathVariable("ev_id") String id, @RequestBody Debt debt) {
        if (!partRepo.existsById(debt.getCreditor().getId()) || !partRepo.existsById(debt.getDebtor().getId())
            || !eventRepo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        debt.setEvent(eventRepo.getReferenceById(id));
        Debt saved = repo.save(debt);
        //eventRepo.findById(id).get().updateDate();
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<List<Debt>> getByEventId(@PathVariable("id") String id) {
        if (!eventRepo.existsById(id))
            return ResponseEntity.badRequest().build();
        eventRepo.findById(id).get().updateDate();
        return ResponseEntity.ok(repo.findByEventId(id));
    }

    @GetMapping("/creditor/{creditor-id}")
    public ResponseEntity<List<Debt>> getAllByCreditorId(@PathVariable("creditor-id") long id) {
        if(id < 0)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(repo.findAllByCreditorId(id));
    }

    @GetMapping("/debtor/{debtor-id}")
    public ResponseEntity<List<Debt>> getAllByDebtorId(@PathVariable("debtor-id") long id) {
        if(id < 0)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(repo.findAllByDebtorId(id));
    }

    @PutMapping("/{id}/amount")
    public ResponseEntity<Debt> updateAmount(@PathVariable("id") long id, @RequestBody double amount) {
        Debt debt = repo.findById(id).orElse(null);
        if (debt == null) {
            return ResponseEntity.badRequest().build();
        }
        //eventRepo.findById(repo.findById(id).get().getEvent().getId()).get().updateDate();

        debt.setAmount(amount);
        Debt saved = repo.save(debt);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Debt> deleteById(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        //eventRepo.findById(repo.findById(id).get().getEvent().getId()).get().updateDate();
        //Debt debt = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Debt>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }
}
