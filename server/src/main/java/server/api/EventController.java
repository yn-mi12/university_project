package server.api;

import commons.Expense;
import commons.Participant;
import commons.dto.EventDTO;
import commons.Event;
import commons.dto.ExpenseDTO;
import commons.dto.ParticipantDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/events")
public class EventController {

    private ModelMapper modelMapper;
    private final EventRepository repo;

    private final ParticipantRepository partRepo;
    private final ExpenseRepository expRepo;

    public EventController(EventRepository repo, ParticipantRepository partRepo, ExpenseRepository expRepo) {
        this.modelMapper = new ModelMapper();
        this.repo = repo;
        this.partRepo = partRepo;
        this.expRepo = expRepo;
    }

    @GetMapping
    public List<EventDTO> getAllEvents() {

        List<Event> entities = repo.findAll();
        List<EventDTO> events = new ArrayList<>();
        events = entities.stream().map(post -> modelMapper.map(post, EventDTO.class))
                .collect(Collectors.toList());
        //participants and expenses lists are not retrieved for performance reasons
        return events;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable(name = "id") Long id) {
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
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        Event eventRequest = modelMapper.map(eventDTO, Event.class);

        Event event = repo.save(eventRequest);

        // convert entity to DTO
        EventDTO eventResponse = modelMapper.map(event,EventDTO.class);

        return new ResponseEntity<EventDTO>(eventResponse, HttpStatus.CREATED);
    }
//    @PutMapping("/{id}")
//    public ResponseEntity<EventDTO> updateEvent(@PathVariable long id, @RequestBody EventDTO eventDTO) {
//        Event eventRequest = modelMapper.map(eventDTO, Event.class);
//        // convert DTO to Entity
//
//        Event event = repo.(id,eventRequest);
//        EventDTO eventResponse = modelMapper.map(event,EventDTO.class);
//
//
//        return ResponseEntity.ok().body(eventResponse);
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<ResponseEntity> deleteEvent(@PathVariable(name = "id") Long id) {
//        eventService.deleteEvent(id);
//        ResponseEntity apiResponse = new ResponseEntity<>("Post deleted successfully", HttpStatusCode.valueOf("OK"));
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }
}
