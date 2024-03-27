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
@RequestMapping("api/participants")
public class ParticipantController {

    private final Random random;
    private final ParticipantRepository repo;

    /**
     * The constructor for the ParticipantController class
     *
     * @param random - The random used to get a random entry
     * @param repo   - The Participant repository
     */
    public ParticipantController(Random random, ParticipantRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    /**
     * @return - all the participants currently stored
     */
    @GetMapping(path = { "", "/" })
    public List<Participant> getAll() {
        return repo.findAll();
    }

    /**
     * Return a specific Participant from its id
     *
     * @param id - The id of the participant
     * @return - The Participant with the id specified
     */
    @GetMapping("/{id}")
    public ResponseEntity<Participant> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<List<Participant> > getByEventId(@PathVariable("id") long id) {
        return ResponseEntity.ok(repo.findByEventId(id));
    }

    /**
     * Checks if the provided string is null or empty
     *
     * @param s - The string to be checked
     * @return - True iff the string is neither null nor empty. False otherwise.
     */
    private static boolean isNullOrEmpty(String s) { return s == null || s.isEmpty(); }

    /**
     * Return a random Participant from the repo
     * @return - a random Participant from the repository
     */
    @GetMapping("rnd")
    public ResponseEntity<Participant> getRandom() {
        var events = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(events.get(idx));
    }

    /***
     * Returns the email of a Participant by id, if it exists
     * @param id - The id of the Participant
     * @return - The Participants email
     */
    @GetMapping(path = { "/{id}/email" })
    public ResponseEntity<Object> getEmail(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id) || isNullOrEmpty(repo.findById(id).get().getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        else return ResponseEntity.ok(repo.findById(id).get().getEmail());
    }

    /**
     * Adds a Participant to the repository
     *
     * @param participant - The Participant to be added
     * @return - The saved Participant
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Participant> save(@RequestBody Participant participant) {

        if (participant == null || isNullOrEmpty(participant.firstName) || isNullOrEmpty(participant.lastName)) {
            return ResponseEntity.badRequest().build();
        }

        Participant saved = repo.save(participant);
        return ResponseEntity.ok(saved);
    }

}
