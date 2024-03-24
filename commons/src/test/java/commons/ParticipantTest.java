package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    @Test
    void checkConstructor() {
        Participant x = new Participant("a","b","c");
        assertEquals("a",x.getFirstName());
        assertEquals("b",x.getLastName());
        assertEquals("c",x.getEmail());
    }

    @Test
    void checkConstructorNoEmail() {
        Participant x = new Participant("a","b");
        assertEquals("a",x.getFirstName());
        assertEquals("b",x.getLastName());
        assertNull(x.getEmail());
    }

    @Test
    void setId() {
        Participant x = new Participant("a","b");
        Participant y = new Participant("a","b");
        x.setId(1);
        y.setId(1);
        assertEquals(x.getId(),y.getId());
    }

    @Test
    void getFirstName() {
        Participant x = new Participant("a","b");
        assertEquals("a",x.getFirstName());
    }

    @Test
    void setFirstName() {
        Participant x = new Participant("a","b");
        x.setFirstName("c");
        assertEquals("c",x.getFirstName());
    }

    @Test
    void getLastName() {
        Participant x = new Participant("a","b");
        assertEquals("b",x.getLastName());
    }

    @Test
    void setLastName() {
        Participant x = new Participant("a","b");
        x.setLastName("c");
        assertEquals("c",x.getLastName());
    }

    @Test
    void setEmail() {
        Participant x = new Participant("a","b");
        x.setEmail("test");
        assertEquals("test", x.getEmail());
    }

    @Test
    void testEquals() {
        Participant x = new Participant("a","b");
        Participant y = new Participant("a","b");
        assertEquals(x,y);
        x.setLastName("abc");
        assertNotEquals(x,y);
        assertNotEquals(null,x);
    }

    @Test
    void testHashCode() {
        Participant x = new Participant("a","b");
        Participant y = new Participant("a","b","c");
        assertEquals(x.hashCode(),y.hashCode());
        x.setLastName("z");
        assertNotEquals(x.hashCode(),y.hashCode());
    }

    @Test
    void testToString() {
        Participant x = new Participant("a","b");
        assertEquals("Participant{id=0, firstName='a', lastName='b', email='null'}", x.toString());
    }

    @Test
    void getByIdTest() {
        List<Participant> ps = new ArrayList<>();
        Participant x = new Participant("a","b");
        Participant y = new Participant("a","b");
        x.setId(1);
        y.setId(2);
        ps.add(x);
        ps.add(y);
        assertEquals(x, Participant.getById(ps, 1));
    }
}