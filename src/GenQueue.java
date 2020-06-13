import java.util.LinkedList;


/**
 * This Class represnts a generic queue where all the file
 * are stored until the user starts all tasks
 * @author Lorenzo
 */


/**
 * @param <E> Coda
 */
class GenQueue<E> {
	  private LinkedList<E> list = new LinkedList<E>();

	  public void enqueue(E item) {
	    list.addLast(item);
	  }

	  public E dequeue() {
	    return list.poll();
	  }

	  public boolean hasItems() {
	    return !list.isEmpty();
	  }

	  public int size() {
	    return list.size();
	  }


}
