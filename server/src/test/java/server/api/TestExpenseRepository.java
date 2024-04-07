package server.api;

import commons.Expense;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestExpenseRepository implements ExpenseRepository {
    public final List<Expense> expenses = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name){ calledMethods.add(name);}

    @Override
    public void flush() {}

    @Override
    public <S extends Expense> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Expense> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Expense> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Expense getOne(Long aLong) {
        return null;
    }

    @Override
    public Expense getById(Long id) {
        return null;
    }

    @Override
    public Expense getReferenceById(Long id) {
        call("getReferenceById");
        return findById(id).orElse(null);
    }

    @Override
    public <S extends Expense> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Expense> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Expense> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Expense> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Expense> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Expense> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Expense, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Expense> S save(S entity) {
        call("save");
        if(entity.getId() != 0 && existsById(entity.getId())) {
            expenses.replaceAll(e -> (e.getId() == entity.getId()) ? entity : e);
            return entity;
        }
        entity.setId(expenses.stream().mapToLong(Expense::getId).max().orElse(1L));
        expenses.add(entity);
        return entity;
    }

    @Override
    public <S extends Expense> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Expense> findById(Long aLong) {
        call("findById");
        return expenses.stream().filter(e -> e.getId() == aLong).findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return expenses.stream().anyMatch(e -> e.getId() == id);
    }

    @Override
    public List<Expense> findAll() {
        calledMethods.add("findAll");
        return expenses;
    }

    @Override
    public List<Expense> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        calledMethods.add("count");
        return expenses.size();
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Expense entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Expense> entities) {

    }

    @Override
    public void deleteAll() {
        calledMethods.add("deleteAll");
        expenses.clear();
    }

    @Override
    public List<Expense> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Expense> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Expense> findByEventId(String eventId) {
        return expenses.stream().filter(p -> p.getEvent().getId().equals(eventId)).toList();
    }

    @Override
    public List<Expense> findByParticipantId(Long participantId) {
        return expenses.stream().filter(e -> e.getDebtors().stream().anyMatch(ep -> ep.getParticipant().getId() == participantId)).toList();
    }
}
