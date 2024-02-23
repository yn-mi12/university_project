package server.api;

import java.util.List;
import java.util.Random;

import commons.Person;
import commons.Event;
import server.database.EventRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {

    private final Random random;
    private final EventRepository repo;

    public EventController(Random random, EventRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<Event> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> add(@RequestBody Event event) {

        if (isNullOrEmpty(event.getTitle()) || isNullOrEmpty(event.getInviteCode())
                || isNullOrEmpty(event.getParticipants())) {
            return ResponseEntity.badRequest().build();
        }

        Event saved = repo.save(event);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) { return s == null || s.isEmpty(); }

    private static boolean isNullOrEmpty(List<Person> e) { return e == null || e.isEmpty(); }

    @GetMapping("rnd")
    public ResponseEntity<Event> getRandom() {
        var events = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(events.get(idx));
    }
}
