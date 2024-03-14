package server.api;

import java.util.Random;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ParticipantControllerTest {

    public int nextInt;
    private MyRandom random;
    private TestParticipantRepository repo;
    private ParticipantController partc;

    private static Participant getParticipant(String a,String b,String c){
        return new Participant(a,b,c);
    }

    @BeforeEach
    void setUp() {
        random = new MyRandom();
        repo = new TestParticipantRepository();
        partc = new ParticipantController(random, repo);
    }

    @Test
    public void randomSelection() {
        partc.add(getParticipant("a","b","c"));
        partc.add(getParticipant("b","c","d"));
        partc.add(getParticipant("c","d","e"));
        partc.add(getParticipant("e","f",null));
        nextInt = 3;
        var actual = partc.getRandom();
        assertTrue(random.wasCalled);
        assertEquals("e",actual.getBody().firstName);
        assertEquals("f",actual.getBody().lastName);
        assertEquals(null,actual.getBody().email);
        nextInt = 0;
        actual = partc.getRandom();
        assertTrue(random.wasCalled);
        assertEquals("a",actual.getBody().firstName);
        assertEquals("b",actual.getBody().lastName);
        assertEquals("c",actual.getBody().email);
    }

    @Test
    void cannotAddNullPerson() {
        var actual = partc.add(getParticipant(null,"a",null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        actual = partc.add(getParticipant("a",null,null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void getAll() {
        partc.add(getParticipant("a","b","c"));
        partc.add(getParticipant("b","c","d"));
        partc.add(getParticipant("c","d","e"));
        partc.add(getParticipant("e","f",null));
        assertEquals(4,repo.count());
        assertTrue(repo.calledMethods.contains("count"));
        var actual = partc.getAll();
        assertTrue(repo.calledMethods.contains("findAll"));
        assertEquals("a", actual.getFirst().firstName);
        assertEquals("b", actual.getFirst().lastName);
        assertEquals("c", actual.getFirst().email);
        assertEquals("e", actual.getLast().firstName);
        assertEquals("f", actual.getLast().lastName);
        assertEquals(null, actual.getLast().email);
    }

    @Test
    void getById() {
        partc.add(getParticipant("a","b","c"));
        partc.add(getParticipant("b","c","d"));
        partc.add(getParticipant("c","d","e"));
        partc.add(getParticipant("e","f",null));
        var actual = partc.getById(100);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        /*
        actual = partc.getById(0);
        assertEquals("c", actual.getBody().firstName);
        assertEquals("d", actual.getBody().lastName);
        assertEquals("e", actual.getBody().email);
         */
    }

    @Test
    void save() {
        var actual = partc.add(getParticipant(null,null,null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        assertFalse(repo.calledMethods.contains("save"));
        partc.add(getParticipant("a","b",null));
        assertTrue(repo.calledMethods.contains("save"));
    }

    @Test
    void getEmail() {
        partc.add(getParticipant("a","b","c"));
        partc.add(getParticipant("b","c","d"));
        assertEquals("c", repo.getById((0L)).email);
        assertEquals("d", repo.getById((1L)).email);
    }

    @SuppressWarnings("serial")
    public class MyRandom extends Random {

        public boolean wasCalled = false;

        /**
         * Method for manipulating the next instance in the Random function
         * @param bound the upper bound (exclusive).  Must be positive.
         * @return the next random position
         */
        @Override
        public int nextInt(int bound) {
            wasCalled = true;
            return nextInt;
        }
    }
}