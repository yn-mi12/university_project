package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    @Test
    void checkConstructor() {
        Participant x = new Participant("a","b","c");
        assertEquals("a",x.getFirstName());
        assertEquals("b",x.getLastName());
        assertEquals("c",x.getEmail());
    }

    void checkConstructorNoEmail() {
        Participant x = new Participant("a","b");
        assertEquals("a",x.getFirstName());
        assertEquals("b",x.getLastName());
        assertNull(x.getEmail());
    }

    @Test
    void getId() {
        Participant x = new Participant("a","b");
        Participant y = new Participant("a","b");
        assertEquals(x.getId(),y.getId());
    }

    @Test
    void setId() {
        Participant x = new Participant("a","b");
        x.setId(1);
        assertEquals(1,x.getId());
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
        assertEquals("Participant{" +
                "id=" + x.getId() +
                ", firstName='" + x.getFirstName() + '\'' +
                ", lastName='" + x.getLastName() + '\'' +
                '}',x.toString());
    }
}