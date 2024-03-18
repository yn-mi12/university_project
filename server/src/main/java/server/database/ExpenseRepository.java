package server.database;

import commons.ExpenseTemp;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExpenseRepository extends JpaRepository<ExpenseTemp, Long> {
}
