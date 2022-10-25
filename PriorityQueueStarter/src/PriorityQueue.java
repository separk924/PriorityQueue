import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A priority queue class implemented using a min heap.
 * Priorities cannot be negative.
 * 
 * @author Seung Park
 * @version 10.24.22
 *
 */
public class PriorityQueue {

	protected Map<Integer, Integer> location;
	protected List<Pair<Integer, Integer>> heap;

	/**
	 * Constructs an empty priority queue
	 */
	public PriorityQueue() {
		heap = new ArrayList<Pair<Integer, Integer>>();
		location = new HashMap<Integer, Integer>();
	}

	/**
	 * Insert a new element into the queue with the
	 * given priority.
	 *
	 * @param priority priority of element to be inserted
	 * @param element  element to be inserted
	 *                 <br>
	 *                 <br>
	 *                 <b>Preconditions:</b>
	 *                 <ul>
	 *                 <li>The element does not already appear in the priority
	 *                 queue.</li>
	 *                 <li>The priority is non-negative.</li>
	 *                 </ul>
	 * 
	 */
	public void push(int priority, int element) {
		if (location.containsKey(element) || priority < 0) {
			throw new AssertionError();
		}
		Pair<Integer, Integer> thePair = new Pair<Integer, Integer>(priority, element);
		heap.add(thePair);
		location.put(thePair.element, heap.indexOf(thePair));
		percolateUp(heap.indexOf(thePair));
	}

	/**
	 * Remove the highest priority element
	 * 
	 * @return the element with the highest priority
	 *         <br>
	 *         <br>
	 *         <b>Preconditions:</b>
	 *         <ul>
	 *         <li>The priority queue is non-empty.</li>
	 *         </ul>
	 * 
	 */
	public Integer pop() {
		if (heap.isEmpty()) {
			throw new AssertionError();
		}
		Pair<Integer, Integer> top = heap.get(0);
		Integer elem = top.element;
		if (heap.size() == 1) {
			heap.remove(heap.size() - 1);
			location.remove(top.element);
			return elem;
		}
		swap(0, heap.size() - 1); // swaps the top and bottom element
		heap.remove(heap.size() - 1);
		location.remove(top.element);
		pushDown(0);
		return elem;
	}

	/**
	 * Returns the highest priority in the queue
	 * 
	 * @return highest priority value
	 *         <br>
	 *         <br>
	 *         <b>Preconditions:</b>
	 *         <ul>
	 *         <li>The priority queue is non-empty.</li>
	 *         </ul>
	 */
	public int topPriority() {
		if (heap.isEmpty()) {
			throw new AssertionError();
		}
		return heap.get(0).priority; // gets first element
	}

	/**
	 * Returns the element with the highest priority
	 * 
	 * @return element with highest priority
	 *         <br>
	 *         <br>
	 *         <b>Preconditions:</b>
	 *         <ul>
	 *         <li>The priority queue is non-empty.</li>
	 *         </ul>
	 */
	public int topElement() {
		if (heap.isEmpty()) {
			throw new AssertionError();
		}
		return heap.get(0).element; // gets first element of heap
	}

	/**
	 * Change the priority of an element already in the
	 * priority queue.
	 * 
	 * @param newpriority the new priority
	 * @param element     element whose priority is to be changed
	 *                    <br>
	 *                    <br>
	 *                    <b>Preconditions:</b>
	 *                    <ul>
	 *                    <li>The element exists in the priority queue</li>
	 *                    <li>The new priority is non-negative</li>
	 *                    </ul>
	 */
	public void changePriority(int newpriority, int element) {
		if (!location.containsKey(element) || newpriority < 0) {
			throw new AssertionError();
		}
		int index = location.get(element);
		int oldP = heap.get(index).priority;
		heap.get(index).priority = newpriority; // change priority
		int newP = heap.get(index).priority; // new priority
		int parentP = heap.get(parent(index)).priority;
		if (newP < parentP) { // if the new priority is larger than the old priority
			percolateUp(index);
		}
		if (newP > oldP) {
			pushDown(index);
		}
	}

	/**
	 * Gets the priority of the element
	 * 
	 * @param element the element whose priority is returned
	 * @return the priority value
	 *         <br>
	 *         <br>
	 *         <b>Preconditions:</b>
	 *         <ul>
	 *         <li>The element exists in the priority queue</li>
	 *         </ul>
	 */
	public int getPriority(int element) {
		if (!location.containsKey(element)) {
			throw new AssertionError();
		}
		int index = location.get(element);
		int priority = heap.get(index).priority;
		return priority;
	}

	/**
	 * Returns true if the priority queue contains no elements
	 * 
	 * @return true if the queue contains no elements, false otherwise
	 */
	public boolean isEmpty() {
		if (heap.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the element exists in the priority queue.
	 * 
	 * @param element to be checked
	 * @return true if the element exists, false otherwise
	 */
	public boolean isPresent(int element) {
		if (location.containsKey(element)) {
			return true;
		}
		return false;
	}

	/**
	 * Removes all elements from the priority queue
	 */
	public void clear() {
		heap.clear();
		location.clear();
	}

	/**
	 * Returns the number of elements in the priority queue
	 * 
	 * @return number of elements in the priority queue
	 */
	public int size() {
		return heap.size();
	}

	/*********************************************************
	 * Private helper methods
	 *********************************************************/

	/**
	 * Push down the element at the given position in the heap
	 * 
	 * @param start_index the index of the element to be pushed down
	 * @return the index in the list where the element is finally stored
	 */
	private int pushDown(int start_index) {
		int curr = start_index;
		int currP = heap.get(curr).priority;
		int left = 0;
		int leftP = 0;
		int right = 0;
		int rightP = 0;
		if (hasTwoChildren(curr)) {
			right = right(curr);
			rightP = heap.get(right).priority;
		}
		if (heap.size() > 1) {
			left = left(curr);
			leftP = heap.get(left).priority;
		}
		int smaller = curr; // find the smallest priority
		while (!isLeaf(curr)) {
			smaller = curr;
			if (hasTwoChildren(curr)) {
				if (leftP < currP) {
					smaller = left;
				}
				if (rightP < heap.get(smaller).priority) {
					smaller = right;
				}
				if (curr != smaller) {
					swap(curr, smaller);
					curr = smaller;
				}
			}
		}
		if (!hasTwoChildren(curr) && leftP < currP) {
			smaller = left;
			swap(curr, smaller);
			curr = smaller;
		}
		if (heap.size() == 1) {
			return curr;
		}
		return curr;
	}

	/**
	 * Percolate up the element at the given position in the heap
	 * 
	 * @param start_index the index of the element to be percolated up
	 * @return the index in the list where the element is finally stored
	 */
	private int percolateUp(int start_index) {
		int currI = start_index;
		int currP = heap.get(currI).priority;
		int parentI = 0;
		int parentP = 0;
		if (currI != 0) {
			parentI = parent(start_index);
			parentP = heap.get(parentI).priority;
		}
		while (currI > 0 && currP < parentP) { // while current is not top node & is less than parent priority
			swap(currI, parentI);
			currI = parentI;
			parentI = parent(currI);
			currP = heap.get(currI).priority;
			parentP = heap.get(parentI).priority;
		}
		return currI;
	}

	/**
	 * Swaps two elements in the priority queue by updating BOTH
	 * the list representing the heap AND the map
	 * 
	 * @param i The index of the element to be swapped
	 * @param j The index of the element to be swapped
	 */
	private void swap(int i, int j) {
		Pair<Integer, Integer> first = heap.get(i);
		Pair<Integer, Integer> sec = heap.get(j);
		heap.set(i, sec);
		heap.set(j, first);
		int fValue = location.get(first.element);
		int sValue = location.get(sec.element);
		location.put(first.element, sValue);
		location.put(sec.element, fValue);
	}

	/**
	 * Computes the index of the element's left child
	 * 
	 * @param parent index of element in list
	 * @return index of element's left child in list
	 */
	private int left(int parent) {
		int l = (2 * parent) + 1;
		return l;
	}

	/**
	 * Computes the index of the element's right child
	 * 
	 * @param parent index of element in list
	 * @return index of element's right child in list
	 */
	private int right(int parent) {
		int r = (2 * parent) + 2;
		return r;
	}

	/**
	 * Computes the index of the element's parent
	 * 
	 * @param child index of element in list
	 * @return index of element's parent in list
	 */

	private int parent(int child) {
		int p = (child - 1) / 2;
		return p;
	}

	/**
	 * Returns true if element is a leaf in the heap
	 * 
	 * @param i index of element in heap
	 * @return true if element is a leaf
	 */
	private boolean isLeaf(int i) {
		if (left(i) <= heap.size() - 1 && right(i) <= heap.size() - 1) {
			return false;
		}
		return true;
	}

	/**
	 * Returns true if element has two children in the heap
	 * 
	 * @param i index of element in the heap
	 * @return true if element in heap has two children
	 */
	private boolean hasTwoChildren(int i) {
		if (left(i) > heap.size() - 1 || right(i) > heap.size() - 1) {
			return false;
		}
		return true;
	}
}
