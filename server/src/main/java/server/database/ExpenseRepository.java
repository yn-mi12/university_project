package server.database;

import commons.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT e FROM Expense e where e.paidBy.event.id = :eventId")
    List<Expense> findByEventId(@Param("eventId") long eventId);

    @Query("SELECT e FROM Expense e where e.paidBy.id = :paidById")
    List<Expense> findByParticipantId(@Param("paidById") long paidById);
}
