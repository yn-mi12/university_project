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

    private void call(String name){calledMethods.add(name);}
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
    public void deleteAllInBatch(Iterable<Event> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Event getOne(Long aLong) {
        return null;
    }

    @Override
    public Event getById(Long aLong) {
        return null;
    }

    @Override
    public Event getReferenceById(Long aLong) {
        return null;
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
        entity.setId(events.size());
        events.add(entity);
        return entity;
    }

    @Override
    public <S extends Event> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    private Optional<Event> find(Long id){
        return events.stream().filter(e -> e.getId() == id).findFirst();
    }
    private Event findI(Long id){
        for(var x: events)
        {
            if(x.getId() == id) return x;
        }
        return null;
    }

    @Override
    public Optional<Event> findById(Long aLong) {
        call("findById");
        return find(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        call("existsById");
        return find(aLong).isPresent();
    }

    @Override
    public List<Event> findAll() {
        call("findAll");
        return events;
    }

    @Override
    public List<Event> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        call("count");
        return events.size();
    }

    @Override
    public void deleteById(Long aLong) {
        call("deleteById");
        events.remove(findI(aLong));
    }

    @Override
    public void delete(Event entity) {
        events.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Event> entities) {

    }

    @Override
    public void deleteAll() {
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

    @Override
    public List<Event> findByInviteCode(String inviteCode) {
        Event event = events.stream().filter(e -> e.getInviteCode().equals(inviteCode)).findFirst().orElse(null);
        if(event != null)
            return List.of(event);
        return null;
    }

    @Override
    public boolean existsByInviteCode(String inviteCode) {
        Event event = events.stream().filter(e -> e.getInviteCode().equals(inviteCode)).findFirst().orElse(null);
        return event != null;
    }
}
