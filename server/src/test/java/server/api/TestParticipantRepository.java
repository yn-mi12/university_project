package server.api;

import commons.Participant;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestParticipantRepository implements ParticipantRepository {
    public final List<Participant> participants = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name){ calledMethods.add(name);}

    @Override
    public void flush() {}

    @Override
    public <S extends Participant> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Participant> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Participant> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Participant getOne(Long aLong) {
        return null;
    }

    @Override
    public Participant getById(Long id) {
        return null;
    }

    @Override
    public Participant getReferenceById(Long id) {
        call("getReferenceById");
        return findById(id).orElse(null);
    }

    @Override
    public <S extends Participant> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Participant> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Participant> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Participant> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Participant> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Participant> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Participant, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Participant> S save(S entity) {
        call("save");
        if(entity.getId() != 0 && existsById(entity.getId())) {
            participants.replaceAll(p -> (p.getId() == entity.getId()) ? entity : p);
            return entity;
        }
        entity.setId(participants.stream().mapToLong(Participant::getId).max().orElse(0L)+1);
        participants.add(entity);
        return entity;
    }

    @Override
    public <S extends Participant> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Participant> findById(Long aLong) {
        call("findById");
        return participants.stream().filter(e -> e.getId() == aLong).findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return participants.stream().anyMatch(e -> e.getId() == id);
    }

    @Override
    public List<Participant> findAll() {
        calledMethods.add("findAll");
        return participants;
    }

    @Override
    public List<Participant> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        calledMethods.add("count");
        return participants.size();
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Participant entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Participant> entities) {

    }

    @Override
    public void deleteAll() {
        calledMethods.add("deleteAll");
        participants.clear();
    }

    @Override
    public List<Participant> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Participant> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Participant> findByEventId(String eventId) {
        return participants.stream().filter(p -> p.getEvent().getId().equals(eventId)).toList();
    }
}
