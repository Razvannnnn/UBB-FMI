import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue {
    private Node head = null;
    private Node tail = null;
    private int size = 0;
    private final int CAPACITY = 50;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public void push(int id, int grade) {
        lock.lock();
        try {
            while (size >= CAPACITY) {
                try {
                    notFull.await(); // Asteapta daca coada e plina
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            Node newNode = new Node(id, grade);
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            size++;
            notEmpty.signal(); // Anunta consumatorii
        } finally {
            lock.unlock();
        }
    }

    public Node pop() {
        lock.lock();
        try {
            while (size == 0) {
                try {
                    notEmpty.await(); // Asteapta daca coada e goala
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }

            Node result = head;
            head = head.next;
            if (head == null) {
                tail = null;
            }
            size--;

            // Logica pentru oprire
            if (result.id == -1 && result.grade == -1) {
                return null; // Semnal de oprire pentru worker
            }

            notFull.signal(); // Anunta producatorii
            return result;
        } finally {
            lock.unlock();
        }
    }
}