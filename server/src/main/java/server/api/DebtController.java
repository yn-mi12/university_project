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

    @GetMapping("/paid/{part-id}")
    public ResponseEntity<List<Debt>> getAllPaidDebts(@PathVariable("part-id") long id) {
        if(id < 0)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(repo.findAllByCreditorId(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Debt>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }
}
