package server.api;

import commons.Event;
import commons.Participant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.EventRepository;
import server.database.ParticipantRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("api/events")
public class EventController {

    private final EventRepository repo;
    private final ParticipantRepository partRepo;

    /**
     * The constructor for the EventController class
     *
     * @param repo - The Event repository
     */
    public EventController(EventRepository repo, ParticipantRepository partRepo) {
        this.repo = repo;
        this.partRepo = partRepo;
    }

    /**
     * Returns all the Events currently stored
     *
     * @return - All the Events
     */
    @GetMapping(path = {"", "/"})
    public List<Event> getAll() {
        return repo.findAll();
    }

    @MessageMapping("/events")
    @SendTo("/topic/events")
    public Event addEvent(Event e) {
        save(e);
        return e;
    }

    @MessageMapping("/titles")
    @SendTo("/topic/titles")
    public Event editTitleEvent(Event e) {
        editListeners.forEach((key, listener) -> listener.accept(e));
        return updateTitle(e.getId(), e.getTitle()).getBody();
    }

    @MessageMapping("/deleted")
    @SendTo("/topic/deleted")
    public Event deleteEvent(Event e) {
        deleteById(e.getId()).getBody();
        deleteListeners.forEach((key, listener) -> listener.accept(e));
        return e;
    }

    private Map<Object, Consumer<Event>> addListeners = new HashMap<>();

    @GetMapping(path = {"", "/addUpdates"})
    public DeferredResult<ResponseEntity<Event>> getAddUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Event>>(5000L, noContent);

        var key = new Object();
        addListeners.put(key, event -> {
            res.setResult(ResponseEntity.ok(event));
        });

        res.onCompletion(() -> addListeners.remove(key));
        return res;
    }
    private Map<Object, Consumer<Event>> editListeners = new HashMap<>();

    @GetMapping(path = {"", "/editUpdates"})
    public DeferredResult<ResponseEntity<Event>> getEditUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Event>>(5000L, noContent);

        var key = new Object();
        editListeners.put(key, event -> {
            res.setResult(ResponseEntity.ok(event));
        });

        res.onCompletion(() -> editListeners.remove(key));
        return res;
    }

    private Map<Object, Consumer<Event>> deleteListeners = new HashMap<>();

    @GetMapping(path = {"", "/deleteUpdates"})
    public DeferredResult<ResponseEntity<Event>> getDeleteUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Event>>(5000L, noContent);

        var key = new Object();
        deleteListeners.put(key, event -> {
            res.setResult(ResponseEntity.ok(event));
        });

        res.onCompletion(() -> deleteListeners.remove(key));
        return res;
    }

    /**
     * Return a specific Event from its id
     *
     * @param id - The id of the Event
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
     * Return a specific Event from its invite code
     *
     * @param inviteCode - The invite code of the Event
     * @return - The Event with the invite code specified
     */
    @GetMapping("/code={invite-code}")
    public ResponseEntity<Event> getByInviteCode(@PathVariable("invite-code") String inviteCode) {
        if (inviteCode == null || !repo.existsByInviteCode(inviteCode))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.findByInviteCode(inviteCode).get(0));
    }

    /**
     * Adds an Event to the repository
     *
     * @param event - The Event to be added
     * @return - The saved Event
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Event> save(@RequestBody Event event) {
        if (isNullOrEmpty(event.getTitle()) || isNullOrEmpty(event.getInviteCode())) {
            return ResponseEntity.badRequest().build();
        }
        addListeners.forEach((key, listener) -> listener.accept(event));
        Event saved = repo.save(event);
        return ResponseEntity.ok(saved);
    }

    @RequestMapping(value = "/{invite_code}/participants", method = RequestMethod.POST)
    public ResponseEntity<Participant> saveParticipantToEvent(@RequestBody Participant participant,
                                                              @PathVariable("invite_code") String id) {

        if (participant == null || isNullOrEmpty(participant.getFirstName()) || isNullOrEmpty(participant.getLastName())
                || !repo.existsByInviteCode(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event event = repo.findByInviteCode(id).getFirst();
        participant.setEvent(event);
        partRepo.save(participant);
        return ResponseEntity.ok(participant);
    }

    /**
     * Updates an Event
     *
     * @param id       - ID of an Event to be updated
     * @param newTitle - new title of an Event
     * @return - The updated Event
     */
    @PutMapping("/{id}/title")
    public ResponseEntity<Event> updateTitle(@PathVariable Long id, @RequestBody String newTitle) {
        Event event = repo.findById(id).orElse(null);
        if (event == null) {
            return ResponseEntity.badRequest().build();
        }
        event.setTitle(newTitle);
        Event saved = repo.save(event);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes Event by a specific id
     *
     * @param id - the id of the deleted event
     * @return - the deleted event
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Event> deleteById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event x = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(x);
    }

    /**
     * Checks if the provided string is null or empty
     *
     * @param s - The string to be checked
     * @return - True iff the string is neither null nor empty. False otherwise.
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}
