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

    /**
     * The constructor for the EventController class
     * @param random - The random used to get a random entry
     * @param repo - The Event repository
     */
    public EventController(Random random, EventRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    /**
     * Returns all the Events currently stored
     * @return - All the Events
     */
    @GetMapping(path = { "", "/" })
    public List<Event> getAll() {
        return repo.findAll();
    }

    /**
     * Return a specific Event from its id
     * @param id - The id of the event
     * @return - The Event with the id specified
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Adds an Event to the repository
     * @param event - The Event to be added
     * @return - The saved Event
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> add(@RequestBody Event event) {

        if (isNullOrEmpty(event.getTitle()) || isNullOrEmpty(event.getInviteCode())
                || isNullOrEmpty(event.getParticipants())) {
            return ResponseEntity.badRequest().build();
        }

        Event saved = repo.save(event);
        return ResponseEntity.ok(saved);
    }

    /**
     * Checks if the provided string is null or empty
     * @param s - The string to be checked
     * @return - True iff the string is neither null nor empty. False otherwise.
     */
    private static boolean isNullOrEmpty(String s) { return s == null || s.isEmpty(); }

    /**
     * Checks if the provided list of Persons is null or empty
     * @param e - The list to be checked
     * @return - True iff the list is neither null nor empty. False otherwise.
     */
    private static boolean isNullOrEmpty(List<Person> e) { return e == null || e.isEmpty(); }

    /**
     * Returns a random Event from the repository
     * @return - A randomly selected Event
     */
    @GetMapping("rnd")
    public ResponseEntity<Event> getRandom() {
        var events = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(events.get(idx));
    }
}
