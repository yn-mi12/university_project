package server.api;

import commons.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.EventRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("api/events")
public class EventController {

    private final EventRepository repo;

    /**
     * The constructor for the EventController class
     *
     * @param repo - The Event repository
     */
    public EventController(EventRepository repo){
        this.repo = repo;
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

    @MessageMapping("/updated")
    @SendTo("/topic/updated")
    public Event updateEvent(Event e) {
        return save(e).getBody();
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
     * Returns all the Events currently stored
     *
     * @return - All the Events
     */
    @GetMapping(path = { "", "/" })
    public ResponseEntity<List<Event> > getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    /**
     * Return a specific Event from its id
     *
     * @param id - The id of the Event
     * @return - The Event with the id specified
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") String id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.findById(id).get().updateDate();
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Adds an Event to the repository
     *
     * @param event - The Event to be added
     * @return - The saved Event
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Event> save(@RequestBody Event event) {
        if (isNullOrEmpty(event.getTitle()) || isNullOrEmpty(event.getId())) {
            return ResponseEntity.badRequest().build();
        }
        setEventDetails(event);
        if(event.getDebts() != null)
            for(var x: event.getDebts())x.setEvent(event);
        Event saved = repo.save(event);
        saved.updateDate();
        return ResponseEntity.ok(saved);
    }

    public void setEvent(Event event){
        for(var x:event.getParticipants()) x.setEvent(event);
        for(var x:event.getExpenses()) x.setEvent(event);
        for(var x:event.getExpenses())
            for(var y:x.getDebtors())y.setExpense(x);
        for(var x:event.getDebts())x.setEvent(event);
        setEventDetails(event);
    }

    private void setEventDetails(Event event) {
        addListeners.forEach((key, listener) -> listener.accept(event));
        if(event.getParticipants() != null)
            for(var x: event.getParticipants()) x.setEvent(event);
        if(event.getExpenses() != null) {
            for (var x : event.getExpenses()) x.setEvent(event);
            for (var x : event.getExpenses())
                if(x.getDebtors() != null)
                    for (var y : x.getDebtors()) y.setExpense(x);
        }
    }


    /**
     * Updates an Event
     *
     * @param id       - ID of an Event to be updated
     * @param newTitle - new title of an Event
     * @return - The updated Event
     */
    @PutMapping("/{id}/title")
    public ResponseEntity<Event> updateTitle(@PathVariable String id, @RequestBody String newTitle) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if(isNullOrEmpty(newTitle))
            return ResponseEntity.badRequest().build();

        Event event = repo.findById(id).get();
        event.setTitle(newTitle);
        Event saved = repo.save(event);
        saved.updateDate();
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes Event by a specific id
     *
     * @param id - the id of the deleted event
     * @return - the deleted event
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Event> deleteById(@PathVariable("id") String id){
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
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
