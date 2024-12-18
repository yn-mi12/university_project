package server.api;

import commons.Event;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestEventRepository implements EventRepository {

    public final List<Event> events = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name){
        calledMethods.add(name);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Event> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Event> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Event> entities) {

    }

    @Override
    public void deleteAllInBatch(Iterable<Event> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }


    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Event getOne(String s) {
        return null;
    }

    @Override
    public Event getById(String s) {
        return null;
    }

    @Override
    public Event getReferenceById(String s) {
        call("getReferenceById");
        return findById(s).orElse(null);
    }

    @Override
    public <S extends Event> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Event> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Event> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Event> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Event> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Event> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Event, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Event> S save(S entity) {
        call("save");
        if(existsById(entity.getId())) {
            events.replaceAll(e -> (e.getId().equals(entity.getId())) ? entity : e);
            return entity;
        }
        events.add(entity);
        return entity;
    }

    @Override
    public <S extends Event> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Event> findById(String id) {
        call("findById");
        return events.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public boolean existsById(String id) {
        call("existsById");
        return events.stream().anyMatch(e -> e.getId().equals(id));
    }

    @Override
    public List<Event> findAll() {
        call("findAll");
        return events;
    }

    @Override
    public List<Event> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        call("count");
        return events.size();
    }

    @Override
    public void deleteById(String s) {
        call("deleteById");
        if(existsById(s))
            delete(findById(s).get());
    }


    @Override
    public void delete(Event entity) {
        events.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }


    @Override
    public void deleteAll(Iterable<? extends Event> entities) {

    }

    @Override
    public void deleteAll() {
        call("deleteAll");
        events.clear();
    }

    @Override
    public List<Event> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        return null;
    }

}
