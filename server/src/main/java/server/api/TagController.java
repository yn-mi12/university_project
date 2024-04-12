package server.api;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.TagRepository;

import java.util.List;


@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository repo;
    private final EventRepository eventRepository;

    public TagController(TagRepository repo, EventRepository eventRepository) {
        this.repo = repo;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/event/{ev_id}")
    public ResponseEntity<List<Tag>> getByEventId(@PathVariable("ev_id") String id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.findById(id).get().updateDate();
        return ResponseEntity.ok(repo.findByEventId(id));
    }

    @GetMapping("/{tag_id}")
    public ResponseEntity<Tag> getById(@PathVariable("tag_id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.findById(repo.findById(id).get().getEvent().getId()).get().updateDate();
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = "/event/{eventId}")
    public ResponseEntity<Tag> saveToEvent(@PathVariable("eventId") String eventId,
                                                   @RequestBody Tag tag) {
        if (tag == null || isNullOrEmpty(tag.getLabel()) || isNullOrEmpty(tag.getColor()))
            return ResponseEntity.badRequest().build();
        if (!eventRepository.existsById(eventId))
            return ResponseEntity.notFound().build();

        eventRepository.findById(eventId).get().updateDate();
        tag.setEvent(eventRepository.getReferenceById(eventId));
        Tag saved = repo.save(tag);
        return ResponseEntity.ok(saved);
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
