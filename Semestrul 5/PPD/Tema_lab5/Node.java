import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    int id;
    int grade;
    boolean isCheater;
    Node next;
    Lock lock;

    public Node(int id, int grade) {
        this.id = id;
        this.grade = grade;
        this.isCheater = false;
        this.next = null;
        this.lock = new ReentrantLock();
    }
}