package server.database;

import commons.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findAllByCreditorId(@Param("creditorId") Long creditorId);
}
