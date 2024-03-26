package server.api;

import java.util.List;

import commons.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.database.ExpenseRepository;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository repo;

    /**
     * The constructor for the ExpenseController class
     * @param repo   - The Expense repository
     */
    public ExpenseController(ExpenseRepository repo) {
        this.repo = repo;
    }

    /**
     * @return - all the expenses currently stored
     */
    @GetMapping("/event/{ev_id}")
    public List<Expense> findAddByEventID(@PathVariable("ev_id") long id) {
        return repo.findAllByEventId(id);
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
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * @param pid - The id of participant
     * @return - all Expenses associated with specified participant
     */
    @GetMapping("/participant/{p_id}")
    public ResponseEntity<List<Expense>> findAllByParticipantId(@PathVariable("p_id") long pid) {
        return ResponseEntity.ok(repo.findAllByParticipantId(pid));
    }

    /**
     * @param pid - The id of participant
     * @return - all Expenses where specified participant
     */
    @GetMapping("/participant/{p_id}/owner")
    public ResponseEntity<List<Expense>> findAllByParticipantIdWhereOwner(@PathVariable("p_id") long pid) {
        return ResponseEntity.ok(repo.findAllByParticipantIdWhereOwner(pid));
    }

    /**
     * @param pid - The id of participant
     * @return - all Expenses associated with specified participant
     */
    @GetMapping("/participant/{p_id}/debts")
    public ResponseEntity<List<Expense>> findAllByParticipantIdWhereDebt(@PathVariable("p_id") long pid) {
        return ResponseEntity.ok(repo.findAllByParticipantIdWhereDebt(pid));
    }

//    /**
//     * Adds an Expense to the repository
//     *
//     * @param expense - The expense to be added
//     * @return - The saved Expense
//     */
//    @PostMapping(path = {"", "/"})
//    public ResponseEntity<Expense> save(@RequestBody Expense expense) {
//
//        if (expense.getPaidBy() == null || isNullOrEmpty(expense.getPaidBy().getFirstName())
//                || isNullOrEmpty(expense.getPaidBy().getLastName())
//                || isNullOrEmpty(expense.getDescription())
//                || expense.getAmount() == 0) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Expense saved = repo.save(expense);
//        return ResponseEntity.ok(saved);
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

}
