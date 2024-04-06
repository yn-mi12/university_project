package server.database;

import commons.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByEventId(@Param("eventId") String eventId);
    @Query("SELECT e FROM Expense e JOIN ExpenseParticipant ep ON e.id = ep.expense.id WHERE ep.participant.id = :participantId")
    List<Expense> findByParticipantId(@Param("participantId") Long participantId);
}
