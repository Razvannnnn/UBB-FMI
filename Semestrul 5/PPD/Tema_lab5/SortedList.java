import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SortedList {
    private final Node head;
    private final Node tail;

    public SortedList() {
        head = new Node(-1, Integer.MAX_VALUE);
        tail = new Node(-1, Integer.MIN_VALUE);
        head.next = tail;
    }

    public void insert(int id, int grade) {
        head.lock.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock.lock();
            try {
                // Avansam daca nota curenta e mai mare
                // SAU nota e egala DAR ID-ul curent e mai mic => ID crescator
                while (curr.grade > grade || (curr.grade == grade && curr.id < id)) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }

                Node newNode = new Node(id, grade);
                newNode.next = curr;
                pred.next = newNode;

            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public void writeToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            Node current = head.next;

            while (current != tail) {
                bw.write(current.id + "," + current.grade);
                bw.newLine();
                current = current.next;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}