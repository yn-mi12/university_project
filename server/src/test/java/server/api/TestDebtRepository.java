package server.api;

import commons.Debt;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.DebtRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestDebtRepository implements DebtRepository {

    public final List<Debt> debts = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name){calledMethods.add(name);}
    private Debt findI(Long id){
        for(var d: debts)
        {
            if(d.getId() == id) return d;
        }
        return null;
    }

    @Override
    public List<Debt> findAllByCreditorId(Long creditorId) {
        call("getAllByCreditorId");
        List<Debt> creditorDebts = new ArrayList<>();
        for(Debt d : debts) {
            if(d.getCreditor().getId() == creditorId)
                creditorDebts.add(d);
        }
        return creditorDebts;
    }


    @Override
    public void flush() {

    }

    @Override
    public <S extends Debt> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Debt> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Debt> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Debt getOne(Long aLong) {
        return null;
    }

    @Override
    public Debt getById(Long aLong) {
        return null;
    }

    @Override
    public Debt getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Debt> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Debt> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Debt> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Debt> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Debt> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Debt> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Debt, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Debt> S save(S entity) {
        call("save");
        entity.setId(debts.size());
        debts.add(entity);
        return entity;
    }

    @Override
    public <S extends Debt> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Debt> findById(Long aLong) {
        return Optional.ofNullable(findI(aLong));
    }

    @Override
    public boolean existsById(Long aLong) {
        Debt find = findI(aLong);
        return find != null;
    }

    @Override
    public List<Debt> findAll() {
        return null;
    }

    @Override
    public List<Debt> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
        call("deleteById");
        debts.remove(findI(aLong));
    }

    @Override
    public void delete(Debt entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Debt> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Debt> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Debt> findAll(Pageable pageable) {
        return null;
    }
}
