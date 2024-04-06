package server.database;

import commons.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByEventId(@Param("eventId") Long eventId);
    @Query("SELECT e FROM Expense e JOIN ExpenseParticipant ep ON e.id = ep.expense.id WHERE ep.participant.id = :participantId")
    List<Expense> findAllByParticipantId(@Param("participantId") Long participantId);
}
