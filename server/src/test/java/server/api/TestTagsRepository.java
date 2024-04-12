package server.api;
import commons.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestTagsRepository implements TagRepository {
    public final List<Tag> tags = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();


    private void call(String name){ calledMethods.add(name);}

    @Override
    public void flush() {}

    @Override
    public <S extends Tag> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Tag> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Tag getOne(Long aLong) {
        return null;
    }

    @Override
    public Tag getById(Long aLong) {
        return null;
    }

    @Override
    public Tag getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Tag> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Tag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }


    @Override
    public <S extends Tag> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Tag> boolean exists(Example<S> example) {
        return false;
    }


    @Override
    public <S extends Tag, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }


    @Override
    public <S extends Tag> S save(S entity) {
        call("save");
        if(entity.getId() != 0 && existsById(entity.getId())){
            tags.replaceAll(t -> (t.getId() == entity.getId()) ? entity : t);
            return entity;
        }
        entity.setId(tags.stream().mapToLong(Tag :: getId).max().orElse(0L)+1);
        tags.add(entity);
        return entity;
    }


    @Override
    public <S extends Tag> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Tag> findById(Long aLong) {
        call("findById");
        return tags.stream().filter(e -> e.getId() == aLong).findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return tags.stream().anyMatch(e -> e.getId() == id);
    }

    @Override
    public List<Tag> findAll() {
        call("findAll");
        return tags;
    }


    @Override
    public List<Tag> findAllById(Iterable<Long> longs) {
        return null;
    }


    @Override
    public long count() {
        call("count");
        return tags.size();
    }


    @Override
    public void deleteById(Long aLong) {
        call("deleteById");
        tags.removeIf(i -> i.getId() == aLong);
    }


    @Override
    public void delete(Tag entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Tag> entities) {

    }


    @Override
    public void deleteAll() {
        call("deleteAll");
        tags.clear();
    }

    @Override
    public List<Tag> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Tag> findByEventId(String eventId) {
        call("findByEventId");
        return tags.stream().filter(t -> t.getEvent().getId().equals(eventId)).toList();
    }
}
