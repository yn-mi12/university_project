package server.api;

import java.util.List;

import commons.Debt;
import server.database.DebtRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/debts")
public class DebtController {

    private final DebtRepository repo;

    /**
     * The constructor for the DebtController class
     *
     * @param repo - The Debt repository
     */
    public DebtController(DebtRepository repo) {
        this.repo = repo;
    }

    /**
     * Returns all the Debts currently stored
     *
     * @return - All the Debts
     */
    @GetMapping(path = {"", "/"})
    public List<Debt> getAll() {
        return repo.findAll();
    }

    /**
     * Return a specific Debt from its id
     *
     * @param id - The id of the debt
     * @return - The Debt with the id specified
     */
    @GetMapping("/{id}")
    public ResponseEntity<Debt> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Adds a Debt to the repository
     *
     * @param debt - The Debt to be added
     * @return - The saved Debt
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Debt> save(@RequestBody Debt debt) {
        Debt saved = repo.save(debt);
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
}


