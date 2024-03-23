package server.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import commons.Expense;
import commons.dto.ExpenseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import server.database.ExpenseRepository;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final Random random;
    private ModelMapper modelMapper;
    private final ExpenseRepository repo;

    /**
     * The constructor for the ExpenseController class
     *
     * @param random - The random used to get a random entry
     * @param repo   - The Expense repository
     */
    public ExpenseController(Random random, ExpenseRepository repo) {
        this.modelMapper = new ModelMapper();
        this.random = random;
        this.repo = repo;
    }

    /**
     * @return - all the expenses currently stored
     */
    @GetMapping(path = {"", "/"})
    public List<ExpenseDTO> getAll() {
        List<Expense> entities = repo.findAll();
        List<ExpenseDTO> dtos = new ArrayList<>();
//        modelMapper.map(entities,events);
        dtos = entities.stream().map(post -> modelMapper.map(post, ExpenseDTO.class))
                .collect(Collectors.toList());

        return dtos;
    }

    @RequestMapping(value = "/event/{event_id}", method = RequestMethod.GET)
    public List<ExpenseDTO> getByEventId(@PathVariable(name = "event_id") Long id) {
        List<Expense> entities = repo.findByEventId(id);
        List<ExpenseDTO> dtos = new ArrayList<>();
//        modelMapper.map(entities,events);
        dtos = entities.stream().map(post -> modelMapper.map(post, ExpenseDTO.class))
                .collect(Collectors.toList());

        return dtos;
    }

    /**
     * Return a specific Expense from its id
     *
     * @param id - The id of the expense
     * @return - The Expense with the id specified
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getById(@PathVariable("id") long id) {
        Optional<Expense> exp = repo.findById(id);

        // convert entity to DTO
        ExpenseDTO response = modelMapper.map(exp.get(), ExpenseDTO.class);

        return ResponseEntity.ok().body(response);
    }

    /**
     * Adds an Expense to the repository
     *
     * @param expense - The expense to be added
     * @return - The saved Expense
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO expense) {
        System.out.println(expense.toString());
        Expense request = modelMapper.map(expense, Expense.class);

        Expense saved = repo.save(request);
        ExpenseDTO response = modelMapper.map(saved, ExpenseDTO.class);

        return new ResponseEntity<ExpenseDTO>(response, HttpStatus.CREATED);
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
