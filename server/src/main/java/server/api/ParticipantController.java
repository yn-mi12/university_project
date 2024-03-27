package server.api;

import commons.Participant;
import commons.dto.ParticipantDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/participants")
public class ParticipantController {

    private final Random random;
    private ModelMapper modelMapper;
    private final ParticipantRepository repo;

    /**
     * The constructor for the ParticipantController class
     *
     * @param random - The random used to get a random entry
     * @param repo   - The Participant repository
     */
    public ParticipantController(Random random, ParticipantRepository repo) {
        this.modelMapper = new ModelMapper();
        this.random = random;
        this.repo = repo;
    }

    /**
     * @return - all the participants currently stored
     */
    @GetMapping(path = { "", "/" })
    public List<ParticipantDTO> getAll() {
        List<Participant> entities = repo.findAll();
        List<ParticipantDTO> dtos ;
        dtos= entities.stream().map(post -> modelMapper.map(post, ParticipantDTO.class))
                .collect(Collectors.toList());

        return dtos;
    }
    /**
     * get the participants of a particular event.
     * @param id - the id of the event for which we want to see the participants
     * @return
     */
    @RequestMapping(value = "/event/{event_id}", method = RequestMethod.GET)
    public List<ParticipantDTO> getByEventId(@PathVariable(name = "event_id") Long id) {
        List<Participant> entities = repo.findByEventId(id);
        List<ParticipantDTO> dtos ;
        dtos= entities.stream().map(post -> modelMapper.map(post, ParticipantDTO.class))
                .collect(Collectors.toList());

        return dtos;
    }

    /**
     * Return a specific Participant from its id
     *
     * @param id - The id of the participant
     * @return - The Participant with the id specified
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParticipantDTO> getById(@PathVariable("id") long id) {
        Optional<Participant> participant = repo.findById(id);

        // convert entity to DTO
        ParticipantDTO response = modelMapper.map(participant.get(), ParticipantDTO.class);

        return ResponseEntity.ok().body(response);
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
    public ResponseEntity<ParticipantDTO> createParticipant(@RequestBody ParticipantDTO participant) {
        Participant request = modelMapper.map(participant, Participant.class);

        if (participant == null || isNullOrEmpty(participant.getFirstName()) || isNullOrEmpty(participant.getLastName())) {
            return ResponseEntity.badRequest().build();
        }

        Participant saved = repo.save(request);
        ParticipantDTO response = modelMapper.map(saved,ParticipantDTO.class);

        return new ResponseEntity<ParticipantDTO>(response, HttpStatus.CREATED);
    }

}
