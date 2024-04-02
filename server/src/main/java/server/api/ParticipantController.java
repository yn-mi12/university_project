package server.api;

import commons.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ParticipantRepository;

import java.util.List;

@RestController
@RequestMapping("api/participants")
public class ParticipantController {

    private final ParticipantRepository repo;

    /**
     * The constructor for the ParticipantController class
     *
     * @param repo - The Participant repository
     */
    public ParticipantController(ParticipantRepository repo) {
        this.repo = repo;
    }

    /**
     * @return - all the participants currently stored
     */
    @GetMapping(path = {"", "/"})
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

    @GetMapping("/event/{id}")
    public ResponseEntity<List<Participant>> getByEventId(@PathVariable("id") long id) {
        if (id < 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.findByEventId(id));
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

    /***
     * Returns the email of a Participant by id, if it exists
     * @param id - The id of the Participant
     * @return - The Participants email
     */
    @GetMapping(path = {"/{id}/email"})
    public ResponseEntity<String> getEmail(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id) || isNullOrEmpty(repo.findById(id).get().getEmail())) {
            return ResponseEntity.badRequest().build();
        } else return ResponseEntity.ok(repo.findById(id).get().getEmail());
    }

    /**
     * Adds a Participant to the repository
     *
     * @param participant - The Participant to be added
     * @return - The saved Participant
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Participant> save(@RequestBody Participant participant) {

        if (participant == null || isNullOrEmpty(participant.getFirstName()) || isNullOrEmpty(participant.getLastName())) {
            return ResponseEntity.badRequest().build();
        }

        Participant saved = repo.save(participant);
        return ResponseEntity.ok(saved);
    }

    /**
     * Updates a Participant in the repository.
     * @param id - The id of the Participant to be updated
     * @param participant - The new Participant
     * @return - The updated Participant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable Long id, @RequestBody Participant participant) {
        Participant participantOld = repo.findById(id).orElse(null);
        if (participantOld == null) {
            return ResponseEntity.badRequest().build();
        }
        participantOld.setFirstName(participant.getFirstName());
        participantOld.setLastName(participant.getLastName());
        participantOld.setEmail(participant.getEmail());
        repo.save(participantOld);
        return ResponseEntity.ok(participantOld);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Participant> deleteById(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Participant participant = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(participant);
    }
}
