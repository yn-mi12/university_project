package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import commons.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
