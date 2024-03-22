package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EventControllerTest {

    public int nextInt;
    public MyRandom random;
    private TestEventRepository repo;
    private EventController eventc;

    @BeforeEach
    void setUp() {
        random = new MyRandom();
        repo = new TestEventRepository();
        eventc = new EventController(random, repo);
    }

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void save() {
    }

    @Test
    void getRandom() {
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