package server.api;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParticipantControllerTest {

    public int nextInt;
    private MyRandom random;
    private TestParticipantRepository repo;
    private ParticipantController partc;

    @BeforeEach
    void setUp() {
        random = new MyRandom();
        repo = new TestParticipantRepository();
        partc = new ParticipantController(random, repo);
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

    @Test
    void getEmail() {
    }

    @SuppressWarnings("serial")
    public class MyRandom extends Random {

        public boolean wasCalled = false;

        @Override
        public int nextInt(int bound) {
            wasCalled = true;
            return nextInt;
        }
    }
}