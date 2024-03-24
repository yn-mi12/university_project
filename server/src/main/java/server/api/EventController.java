package server.api;

import commons.Expense;
import commons.Participant;
import commons.dto.EventDTO;
import commons.Event;
import commons.dto.ExpenseDTO;
import commons.dto.ParticipantDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;


import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/events")
public class EventController {

    private ModelMapper modelMapper;
    private Random random;
    private final EventRepository repo;

    private final ParticipantRepository partRepo;
    private final ExpenseRepository expRepo;

    /**
     * The constructor for the EventController class
     *
     * @param repo     - the event repository
     * @param partRepo - the participant repository
     * @param expRepo  - the expense repository
     * @param random   - the random used to get random entry
     */
    public EventController(EventRepository repo, ParticipantRepository partRepo, ExpenseRepository expRepo, Random random) {
        this.modelMapper = new ModelMapper();
        this.repo = repo;
        this.partRepo = partRepo;
        this.expRepo = expRepo;
        this.random = random;
    }

    /**
     * Returns all the Events currently stored
     *
     * @return - All the Events
     */
    @GetMapping(path = {"", "/"})
    public List<EventDTO> getAll() {

        List<Event> entities = repo.findAll();
        List<EventDTO> events;
        events = entities.stream().map(post -> modelMapper.map(post, EventDTO.class))
                .collect(Collectors.toList());
        //participants and expenses lists are not retrieved for performance reasons
        return events;
    }

    /**
     * Return a specific Event from its id
     *
     * @param id - The id of the event
     * @return - The Event with the id specified
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable(name = "id") Long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Event> event = repo.findById(id);
        EventDTO eventResponse = modelMapper.map(event.get(), EventDTO.class);
        //retrieve related lists
        List<Participant> eventParticipants = partRepo.findByEventId(id);
        List<Expense> eventExpenses = expRepo.findByEventId(id);

        // convert entity to DTO
        eventResponse.setParticipants(eventParticipants.stream().map(post -> modelMapper.map(post, ParticipantDTO.class))
                .collect(Collectors.toList()));
        eventResponse.setExpenses(eventExpenses.stream().map(post -> modelMapper.map(post, ExpenseDTO.class))
                .collect(Collectors.toList()));

        return ResponseEntity.ok().body(eventResponse);
    }

    /**
     * Adds an Event to the repository
     *
     * @param eventDTO - The Event to be added
     * @return - The saved Event
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<EventDTO> save(@RequestBody EventDTO eventDTO) {
        if (isNullOrEmpty(eventDTO.getTitle())) {
            return ResponseEntity.badRequest().build();
        }

        Event eventRequest = modelMapper.map(eventDTO, Event.class);

        Event event = repo.save(eventRequest);

        // convert entity to DTO
        EventDTO eventResponse = modelMapper.map(event, EventDTO.class);

        return ResponseEntity.ok(eventResponse);
    }

    /**
     * Changes the title of a particular event.
     *
     * @param id       - the id of the event that has to be updated
     * @param newTitle - the new title the event will have
     * @return the event with the new title
     */
    @PutMapping("/{id}/title")
    public ResponseEntity<EventDTO> updateTitle(@PathVariable Long id, @RequestBody String newTitle) {
        Optional<Event> event = repo.findById(id);
        event.get().setTitle(newTitle);
        repo.save(event.get());
        EventDTO dto = modelMapper.map(event.get(),EventDTO.class);
        return ResponseEntity.ok(dto);
    }

    //This needs to be fixed

//    @PostMapping
//    public ResponseEntity<ParticipantDTO> addParticipant(@PathVariable Long id, @RequestBody ParticipantDTO newParticipant){
//        if (isNullOrEmpty(newParticipant.getFirstName()) || isNullOrEmpty(newParticipant.getLastName()) ||
//                isNullOrEmpty(newParticipant.getEmail())){
//            return ResponseEntity.badRequest().build();
//        }
//        Participant request = modelMapper.map(newParticipant, Participant.class);
//        Participant saved = partRepo.save(request);
//        ParticipantDTO response = modelMapper.map(saved, ParticipantDTO.class);
//        return new ResponseEntity<ParticipantDTO>(response, HttpStatus.CREATED);
//    }

    /**
     * Checks if the provided string is null or empty
     *
     * @param s - The string to be checked
     * @return - True iff the string is neither null nor empty. False otherwise.
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Returns a random Event from the repository
     *
     * @return - A randomly selected Event
     */
    @GetMapping("rnd")
    public ResponseEntity<EventDTO> getRandom() {
        var events = getAll();
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(events.get(idx));
    }

    /**
     * Deletes Event by a specific id
     *
     * @param id - the id of the deleted event
     * @return - the deleted event
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        repo.deleteById(id);
        return ResponseEntity.ok("");
    }
}
