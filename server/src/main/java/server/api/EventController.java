package server.api;

import java.util.List;

import commons.Event;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import org.springframework.http.ResponseEntity;
import server.security.RequiresAdmin;

@RestController
@RequestMapping("api/events")
public class EventController {

    private final EventRepository repo;

    /**
     * The constructor for the EventController class
     * @param repo - The Event repository
     */
    public EventController(EventRepository repo) {
        this.repo = repo;
    }

    /**
     * Returns all the Events currently stored
     * @return - All the Events
     */
    @RequiresAdmin
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
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Adds an Event to the repository
     * @param event - The Event to be added
     * @return - The saved Event
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> save(@RequestBody Event event) {
        if (isNullOrEmpty(event.getTitle())||isNullOrEmpty(event.getInviteCode())) {
            return ResponseEntity.badRequest().build();
        }

        Event saved = repo.save(event);
        return ResponseEntity.ok(saved);
    }

    /**
     * Updates an Event
     * @param id - ID of an Event to be updated
     * @param newTitle - new title of an Event
     * @return - The updated Event
     */
    @PutMapping("/{id}/title")
    public ResponseEntity<Event> updateTitle(@PathVariable Long id, @RequestBody String newTitle) {
        Event event = repo.findById(id).orElse(null);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        event.setTitle(newTitle);
        Event saved = repo.save(event);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes Event by a specific id
     * @param id - the id of the deleted event
     * @return - the deleted event
     */
    @RequiresAdmin
    @DeleteMapping("/{id}")
    public ResponseEntity<Event> deleteById(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event x = repo.findById(id).orElse(null);
        repo.deleteById(id);
        return ResponseEntity.ok(x);
    }

    /**
     * Checks if the provided string is null or empty
     * @param s - The string to be checked
     * @return - True iff the string is neither null nor empty. False otherwise.
     */
    private static boolean isNullOrEmpty(String s) { return s == null || s.isEmpty(); }

}
