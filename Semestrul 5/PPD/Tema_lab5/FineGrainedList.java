public class FineGrainedList {
    public final Node head;
    public final Node tail;

    public FineGrainedList() {
        head = new Node(Integer.MIN_VALUE, 0);
        tail = new Node(Integer.MAX_VALUE, 0);
        head.next = tail;
    }

    public void addOrUpdate(int id, int grade) {
        head.lock.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock.lock();
            try {
                while (curr.id < id) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }

                if (curr.id == id) {
                    if (grade == -1) {
                        curr.isCheater = true;
                        curr.grade = 0;
                    } else {
                        if (!curr.isCheater) {
                            curr.grade += grade;
                        }
                    }
                } else {
                    int initialGrade = (grade == -1) ? 0 : grade;
                    Node newNode = new Node(id, initialGrade);
                    if (grade == -1) {
                        newNode.isCheater = true;
                    }

                    newNode.next = curr;
                    pred.next = newNode;
                }
            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }
}