package server.api;

import java.util.List;
import java.util.Random;

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
@RequestMapping("/api/events/{id}/expenses")
public class ExpenseController {

    private final Random random;
    private final ExpenseRepository repo;

    /**
     * The constructor for the ExpenseController class
     *
     * @param random - The random used to get a random entry
     * @param repo   - The Expense repository
     */
    public ExpenseController(Random random, ExpenseRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    /**
     * @return - all the expenses currently stored
     */
    @GetMapping(path = {"", "/"})
    public List<Expense> getAll() {
        return repo.findAll();
    }

    /**
     * Return a specific Expense from its id
     *
     * @param id - The id of the expense
     * @return - The Expense with the id specified
     */
    @GetMapping("/{expense_id}")
    public ResponseEntity<Expense> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Adds an Expense to the repository
     *
     * @param expense - The expense to be added
     * @return - The saved Expense
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> save(@RequestBody Expense expense) {

//        if (expense.getPaidBy() == null || isNullOrEmpty(expense.getPaidBy().getFirstName())
//                || isNullOrEmpty(expense.getPaidBy().getLastName())
//                || isNullOrEmpty(expense.getDescription())
//                || expense.getAmount() == 0) {
//            return ResponseEntity.badRequest().build();
//        }

        Expense saved = repo.save(expense);
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

    /**
     * @return - a random Expense from the repository
     */
    @GetMapping("rnd")
    public ResponseEntity<Expense> getRandom() {
        var expenses = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(expenses.get(idx));
    }
}
