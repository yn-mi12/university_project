package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseParticipantTest {

    private ExpenseParticipant ep1;
    private ExpenseParticipant ep2;
    private ExpenseParticipant ep3;
    private final Expense e1 = new Expense();
    private final Expense e2 = new Expense("Misc", "EUR", 100, null);
    private final Participant p1 = new Participant();
    private final Participant p2 = new Participant("A", "B");

    @BeforeEach
    void setUp() {
        ep1 = new ExpenseParticipant(e1, p1, 100, false);
        ep2 = new ExpenseParticipant(e2, p2, 0, true);
        ep3 = new ExpenseParticipant();
        ep1.setId(1);
        ep2.setId(2);
        ep3.setId(3);
    }

    @Test
    void constructorTest() {
        assertNotNull(ep1);
        ExpenseParticipant empty = new ExpenseParticipant();
        assertNotNull(empty);
    }
    @Test
    void getId() {
        assertEquals(1, ep1.getId());
        assertNotEquals(ep1.getId(), ep2.getId());
        assertNotEquals(ep1.getId(), ep3.getId());
        assertNotEquals(ep2.getId(), ep3.getId());
    }

    @Test
    void getExpense() {
        assertEquals(e1, ep1.getExpense());
        assertEquals(e2, ep2.getExpense());
        assertNull(ep3.getExpense());
    }

    @Test
    void setExpense() {
        assertNull(ep3.getExpense());
        ep3.setExpense(e2);
        assertEquals(e2, ep3.getExpense());
    }

    @Test
    void getParticipant() {
        assertEquals(p1, ep1.getParticipant());
        assertEquals(p2, ep2.getParticipant());
        assertNull(ep3.getParticipant());
    }

    @Test
    void setParticipant() {
        assertNull(ep3.getParticipant());
        ep3.setParticipant(p2);
        assertEquals(p2, ep3.getParticipant());
    }

    @Test
    void getShare() {
        assertEquals(100,ep1.getShare());
        assertEquals(0,ep2.getShare());
    }

    @Test
    void setShare() {
        ep3.setShare(50);
        assertEquals(50,ep3.getShare());
    }

    @Test
    void isOwner() {
        assertFalse(ep1.isOwner());
        assertTrue(ep2.isOwner());
    }

    @Test
    void setOwner() {
        ep1.setOwner(true);
        assertTrue(ep1.isOwner());
    }

    @Test
    void testEquals() {
        assertNotEquals(ep1, ep2);
        assertNotEquals(ep1, ep3);
        assertNotEquals(ep2, ep3);
        ExpenseParticipant ep4 = new ExpenseParticipant(e1, p1, 100, false);
        assertNotEquals(ep1, ep4);
        ep4.setId(1);
        assertEquals(ep1, ep4);
    }

    @Test
    void testHashCode() {
        assertNotEquals(ep1.hashCode(), ep2.hashCode());
        assertNotEquals(ep1.hashCode(), ep3.hashCode());
        assertNotEquals(ep2.hashCode(), ep3.hashCode());
        ExpenseParticipant ep4 = new ExpenseParticipant(e1, p1, 100, false);
        assertEquals(ep1.hashCode(), ep4.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("ExpenseParticipant{id=" + ep1.getId()
                + ", participant=" + ep1.getParticipant().toString()
                + ", share=100.0, owner=false}", ep1.toString());
        assertEquals("ExpenseParticipant{id=" + ep2.getId()
                + ", participant=" + ep2.getParticipant().toString()
                + ", share=0.0, owner=true}", ep2.toString());
        assertEquals("ExpenseParticipant{id=" + ep3.getId()
                + ", participant=null, share=0.0, owner=false}",ep3.toString());
    }
}