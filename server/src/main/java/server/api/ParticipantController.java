package server.api;

import java.util.List;
import java.util.Random;

import commons.Participant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ParticipantRepository;

@RestController
@RequestMapping("api/participant")
public class ParticipantController {

    private final Random random;
    private final ParticipantRepository repo;

    public ParticipantController(Random random, ParticipantRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<Participant> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Participant> save(@RequestBody Participant participant) {

        if (isNullOrEmpty(participant.getFirstName()) || isNullOrEmpty(participant.getLastName())) {
            return ResponseEntity.badRequest().build();
        }

        Participant saved = repo.save(participant);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) { return s == null || s.isEmpty(); }

    @GetMapping("rnd")
    public ResponseEntity<Participant> getRandom() {
        var events = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(events.get(idx));
    }

}
