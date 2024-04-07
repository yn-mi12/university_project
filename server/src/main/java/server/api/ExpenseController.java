package server.api;

import java.util.List;
import java.util.Objects;

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
        //Check if all Participants exist and are in the same event
        //Check that there is 1 owner
        //Check that shares add up to 100%
        double shareSum = 0;
        int ownerCount = 0;
        for(var i: expense.getDebtors()) {
            i.setExpense(expense);
            shareSum += i.getShare();
            if (i.isOwner()) ownerCount++;
            if (participantRepository.findById(i.getParticipant().getId()).isEmpty())
                return ResponseEntity.notFound().build();
            if (!Objects.equals(participantRepository.findById(i.getParticipant().getId()).get().getEvent().getId(), eid)){
                return ResponseEntity.badRequest().build();
        }}
        //TODO: this needs to be changed!!
        if (!((shareSum > 98) && (shareSum < 102))
                || ownerCount != 1
                || isNullOrEmpty(expense.getDescription())
                || isNullOrEmpty(expense.getCurrency())
                || expense.getAmount() <= 0
                || expense.getDate() == null) {
            return ResponseEntity.badRequest().build();
        } //TODO: Add checks for Date
        Expense saved = repo.save(expense);
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
