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
    public ResponseEntity<Debt> save(@PathVariable("ev_id") long id, @RequestBody Debt debt) {
        if (!partRepo.existsById(debt.getCreditor().getId()) || !partRepo.existsById(debt.getDebtor().getId())
            || !eventRepo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        debt.setEvent(eventRepo.getReferenceById(id));
        Debt saved = repo.save(debt);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/creditor/{creditor-id}")
    public ResponseEntity<List<Debt>> getAllByCreditorId(@PathVariable("creditor-id") long id) {
        if(id < 0)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(repo.findAllByCreditorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Debt> deleteById(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Debt debt = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(debt);
    }
}
