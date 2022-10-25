import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PriorityQueueTest {
    PriorityQueue pq;

    @Before
    public void setUp() {
        pq = new PriorityQueue();
    }

    /*
     * UNIT TESTS FOR PUSH
     */
    @Test
    public void testPush() {
        pq.push(10, 15);
        pq.push(11, 40);
        pq.push(33, 20);
        pq.push(1, 100);
        pq.push(2, 103);
        assertEquals(5, pq.size());

        assertEquals(1, (int) pq.heap.get(0).priority);
        assertEquals(2, (int) pq.heap.get(1).priority);
        assertEquals(33, (int) pq.heap.get(2).priority);
        assertEquals(11, (int) pq.heap.get(3).priority);
        assertEquals(10, (int) pq.heap.get(4).priority);

        assertEquals(0, (int) pq.location.get(100));
        assertEquals(1, (int) pq.location.get(103));
        assertEquals(2, (int) pq.location.get(20));
        assertEquals(3, (int) pq.location.get(40));
        assertEquals(4, (int) pq.location.get(15));
    }

    /*
     * UNIT TESTS FOR POP
     */
    @Test
    public void testPop() {
        pq.push(10, 15);
        pq.push(11, 40);
        pq.push(33, 20);
        pq.push(1, 100);
        pq.push(2, 103);
        assertEquals(5, pq.size());

        pq.pop();
        assertEquals(2, (int) pq.heap.get(0).priority);
        assertEquals(10, (int) pq.heap.get(1).priority);
        assertEquals(33, (int) pq.heap.get(2).priority);
        assertEquals(11, (int) pq.heap.get(3).priority);
        assertEquals(0, (int) pq.location.get(103));
        assertEquals(1, (int) pq.location.get(15));
        assertEquals(2, (int) pq.location.get(20));
        assertEquals(3, (int) pq.location.get(40));

        pq.pop();
        assertEquals(10, (int) pq.heap.get(0).priority);
        assertEquals(11, (int) pq.heap.get(1).priority);
        assertEquals(33, (int) pq.heap.get(2).priority);
        assertEquals(0, (int) pq.location.get(15));
        assertEquals(1, (int) pq.location.get(40));
        assertEquals(2, (int) pq.location.get(20));

        pq.pop();
        assertEquals(11, (int) pq.heap.get(0).priority);
        assertEquals(33, (int) pq.heap.get(1).priority);
        assertEquals(0, (int) pq.location.get(40));
        assertEquals(1, (int) pq.location.get(20));

        pq.pop();
        assertEquals(33, (int) pq.heap.get(0).priority);
        assertEquals(0, (int) pq.location.get(20));
    }

    /*
     * UNIT TESTS FOR CHANGE PRIORITY
     */
    @Test
    public void testIncreasePriorityOfRoot() {
        pq.push(10, 15);
        pq.push(11, 40);
        pq.push(33, 20);

        // Change root node from priority 10 to priority 1 (should not affect ordering
        // in heap)
        // Element 15 goes from priority 10 to priority 1
        // BEFORE: [ (10,15), (11,40), (33,20)]
        // AFTER: [ (1, 15), (11,40), (33,20)]
        pq.changePriority(1, 15);

        assertEquals(1, pq.topPriority());
        assertEquals(15, pq.topElement());

        pq.pop();
        assertEquals(11, pq.topPriority());
        assertEquals(40, pq.topElement());

        pq.pop();
        assertEquals(33, pq.topPriority());
        assertEquals(20, pq.topElement());
    }

    @Test
    public void testIncreasePriorityOfLeaf() {
        pq.push(10, 15);
        pq.push(11, 40);
        pq.push(33, 20);

        // Change leaf node to highest priority
        // Element 20 goes from priority 33 to priority 1
        // BEFORE: [ (10,15), (11,40), (33,20)]
        // AFTER: [ (1, 20), (10,15), (11,40)]

        pq.changePriority(1, 20);

        assertEquals(1, pq.topPriority());
        assertEquals(20, pq.topElement());

        pq.pop();
        assertEquals(10, pq.topPriority());
        assertEquals(15, pq.topElement());

        pq.pop();
        assertEquals(11, pq.topPriority());
        assertEquals(40, pq.topElement());
    }

    @Test
    public void testDecreasePriorityOfRoot() {
        pq.push(10, 15);
        pq.push(11, 40);
        pq.push(33, 20);

        // Change root node to lowest priority
        // Element 15 goes from priority 10 to priority 50
        // BEFORE: [ (10,15), (11,40), (33,20)]
        // AFTER: [ (11,40), (33,20), (50,15)]

        pq.changePriority(50, 15);

        assertEquals(11, pq.topPriority());
        assertEquals(40, pq.topElement());

        pq.pop();
        assertEquals(33, pq.topPriority());
        assertEquals(20, pq.topElement());

        pq.pop();
        assertEquals(50, pq.topPriority());
        assertEquals(15, pq.topElement());
    }

    /*
     * UNIT TESTS FOR ALL OTHER METHODS
     */
    @Test
    public void OTHERtestGetPriority() {
        pq.push(10, 100);
        pq.push(11, 101);
        pq.push(33, 102);

        assertEquals(10, pq.getPriority(100));
        assertEquals(11, pq.getPriority(101));
        assertEquals(33, pq.getPriority(102));
    }

    @Test
    public void OTHERtestIsEmptyInitially() {
        assertTrue(pq.isEmpty());
    }

    @Test
    public void OTHERtestIsEmptyPopping() {
        pq.push(10, 100);
        pq.push(11, 101);
        pq.push(33, 102);
        pq.pop();
        pq.pop();
        pq.pop();
        assertTrue(pq.isEmpty());
    }

    @Test
    public void OTHERtestIsEmptyClear() {
        pq.push(10, 100);
        pq.push(11, 101);
        pq.push(33, 102);
        pq.clear();
        assertTrue(pq.isEmpty());
    }

    @Test
    public void OTHERtestIsPresent() {
        pq.push(10, 15);
        pq.push(11, 40);
        pq.push(33, 20);
        assertTrue(pq.isPresent(15)); // these are the elements
        assertTrue(pq.isPresent(40));
        assertTrue(pq.isPresent(20));
        assertFalse(pq.isPresent(10)); // these are the priorities
        assertFalse(pq.isPresent(11));
        assertFalse(pq.isPresent(33));
    }

    @Test
    public void OTHERtestClear() {
        pq.push(10, 15);
        pq.push(11, 40);
        pq.push(33, 20);
        pq.clear();
        assertEquals(0, pq.size());
        assertTrue(pq.isEmpty());
    }

    // Calling top and pop on non-empty queue
    @Test
    public void OTHERtestTopAndPop() {
        pq.push(10, 15);
        pq.push(11, 40);
        pq.push(33, 20);

        assertEquals(3, pq.size());
        assertEquals(10, pq.topPriority());
        assertEquals(15, pq.topElement());

        pq.pop();
        assertEquals(2, pq.size());

        assertEquals(11, pq.topPriority());
        assertEquals(40, pq.topElement());

        pq.pop();
        assertEquals(1, pq.size());

        assertEquals(33, pq.topPriority());
        assertEquals(20, pq.topElement());

        pq.pop();
        assertEquals(0, pq.size());
    }

    /*
     * UNIT TESTS FOR PRECONDITIONS
     */

    // Pushing a negative priority
    @Test(expected = AssertionError.class)
    public void ERRORtestPushNegative() {
        pq.push(-10, 0);
    }

    // Pushing the same element twice
    @Test(expected = AssertionError.class)
    public void ERRORtestDoublePush() {
        pq.push(10, 5);
        pq.push(11, 5);
    }

    // Calling pop on empty queue
    @Test(expected = AssertionError.class)
    public void ERRORtestPopEmptyQueue() {
        pq.pop();
    }

    // Calling top priority on empty queue
    @Test(expected = AssertionError.class)
    public void ERRORtestTopPriorityEmptyQueue() {
        pq.topPriority();
    }

    // Calling top element on empty queue
    @Test(expected = AssertionError.class)
    public void ERRORtestTopElementEmptyQueue() {
        pq.topElement();
    }

    // Calling change priority on an empty queue
    @Test(expected = AssertionError.class)
    public void ERRORtestChangePriorityElementNotPresent() {
        pq.changePriority(3, 5);
    }

    // Calling get priority on an element that doesn't exist
    @Test(expected = AssertionError.class)
    public void ERRORtestGetPriorityElementNotPresent() {
        pq.getPriority(100);
    }

}