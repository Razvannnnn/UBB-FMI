package org.example.reteasocializare.Repository.Paging;

public class Page<E> {
    private Iterable<E> content;
    private int elementCount;

    public Page(Iterable<E> content, int elementCount) {
        this.content = content;
        this.elementCount = elementCount;
    }

    public Iterable<E> getContent() {
        return content;
    }

    public int getElementCount() {
        return elementCount;
    }
}
