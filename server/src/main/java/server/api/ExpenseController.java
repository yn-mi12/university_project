package server.api;

import java.util.List;

import commons.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository repo;
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;

    /**
     * The constructor for the ExpenseController class
     * @param repo   - The Expense repository
     * @param eventRepository - The Event repository
     * @param participantRepository - The Participant Repository
     */
    public ExpenseController(ExpenseRepository repo,
                             EventRepository eventRepository,
                             ParticipantRepository participantRepository) {
        this.repo = repo;
        this.eventRepository = eventRepository;
        this.participantRepository = participantRepository;
    }

    /**
     * @return - all the expenses currently stored for a specific event
     */
    @GetMapping("/event/{ev_id}")
    public ResponseEntity<List<Expense>> getByEventId(@PathVariable("ev_id") String id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.findById(id).get().updateDate();
        return ResponseEntity.ok(repo.findByEventId(id));
    }

    /**
     * Return a specific Expense from its id
     *
     * @param id - The id of the expense
     * @return - The Expense with the id specified
     */
    @GetMapping("/{ex_id}")
    public ResponseEntity<Expense> getById(@PathVariable("ex_id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.findById(repo.findById(id).get().getEvent().getId()).get().updateDate();
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * @param pid - The id of participant
     * @return - all Expenses associated with specified participant
     */
    @GetMapping("/participant/{p_id}")
    public ResponseEntity<List<Expense>> findAllByParticipantId(@PathVariable("p_id") long pid) {
        if (pid < 0 || !participantRepository.existsById(pid)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.findById(participantRepository.findById(pid).get().getEvent().getId()).get().updateDate();

        return ResponseEntity.ok(repo.findByParticipantId(pid));
    }

    /**
     * Adds an Expense to the repository
     * @param eid - The Event to which the expense should be added
     * @param expense - The expense to be added
     * @return - The saved Expense
     */
    @PostMapping("/event/{ev_id}")
    public ResponseEntity<Expense> save(@RequestBody Expense expense, @PathVariable("ev_id") String eid) {
        if (!eventRepository.existsById(eid)) {
            return ResponseEntity.notFound().build();
        } else {
            expense.setEvent(eventRepository.getReferenceById(eid));
        }
        for(var i: expense.getDebtors())
            i.setExpense(expense);
        Expense saved = repo.save(expense);
        eventRepository.findById(eid).get().updateDate();
        return ResponseEntity.ok(saved);

    }

    @PutMapping("/{id}/amount")
    public ResponseEntity<Expense> updateAmount(@RequestBody Double newAmount, @PathVariable("id") long id) {
        if(!repo.existsById(id))
            return ResponseEntity.notFound().build();
        Expense saved = repo.findById(id).get();
        saved.setAmount(newAmount);
        repo.save(saved);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes the Expense from repository
     * @param id - The ID of Expense to be deleted
     * @return - Deleted Expense
     */
    @DeleteMapping("/{ex_id}")
    public ResponseEntity<Expense> deleteById(@PathVariable("ex_id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Expense x = repo.findById(id).orElse(null);
        eventRepository.findById(repo.findById(id).get().getEvent().getId()).get().updateDate();
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
