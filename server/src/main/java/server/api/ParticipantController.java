package server.api;

import commons.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ParticipantRepository;

import java.util.List;

@RestController
@RequestMapping("api/participants")
public class ParticipantController {

    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    /**
     * The constructor for the ParticipantController class
     *
     * @param participantRepository - The ParticipantRepository
     * @param eventRepository  - The EventRepository
     */
    public ParticipantController(ParticipantRepository participantRepository, EventRepository eventRepository) {
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Return a specific Participant from its id
     *
     * @param id - The id of the participant
     * @return - The Participant with the id specified
     */
    @GetMapping("/{id}")
    public ResponseEntity<Participant> getById(@PathVariable("id") long id) {
        if (!participantRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(participantRepository.findById(id).get());
    }

    /**
     * Returns All participants from specified Event
     *
     * @param event_id - The id of event
     * @return - all participants with specified event ID
     */
    @GetMapping("/event/{event_id}")
    public ResponseEntity<List<Participant>> getByEventId(@PathVariable("event_id") String event_id) {
        if (!eventRepository.existsById(event_id))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(participantRepository.findByEventId(event_id));
    }

    /**
     * Adds a Participant to the ParticipantRepository
     *
     * @param participant - The Participant to be added
     * @return - The saved Participant
     */
    @PostMapping(path = "/event/{event_id}")
    public ResponseEntity<Participant> saveToEvent(@PathVariable("event_id") String event_id,
                                                   @RequestBody Participant participant) {
        if (participant == null || isNullOrEmpty(participant.getFirstName()) || isNullOrEmpty(participant.getLastName()))
            return ResponseEntity.badRequest().build();
        if (!eventRepository.existsById(event_id))
            return ResponseEntity.notFound().build();

        participant.setEvent(eventRepository.getReferenceById(event_id));
        Participant saved = participantRepository.save(participant);
        return ResponseEntity.ok(saved);
    }

    /**
     * Updates a Participant in the ParticipantRepository.
     *
     * @param id - The id of the Participant to be updated
     * @param participant - The new Participant
     * @return - The updated Participant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable Long id, @RequestBody Participant participant) {
        Participant participantOld = participantRepository.findById(id).orElse(null);
        if (participantOld == null)
            return ResponseEntity.notFound().build();
        if (participant == null || isNullOrEmpty(participant.getFirstName()) || isNullOrEmpty(participant.getLastName()))
            return ResponseEntity.badRequest().build();
        participantOld.setFirstName(participant.getFirstName());
        participantOld.setLastName(participant.getLastName());
        participantOld.setEmail(participant.getEmail());
        participantOld.setAccountName(participant.getAccountName());
        participantOld.setIban(participant.getIban());
        participantOld.setBic(participant.getBic());
        participantRepository.save(participantOld);
        return ResponseEntity.ok(participantOld);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Participant> deleteById(@PathVariable("id") long id){
        if (!participantRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Participant participant = participantRepository.findById(id).get();
        //TODO: change behaviour to check for openBalance and/or debtOwnership
        if (!participant.getExpenses().isEmpty())
            return ResponseEntity.badRequest().build();
        participantRepository.deleteById(id);
        return ResponseEntity.ok(participant);
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
