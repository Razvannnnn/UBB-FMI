using Task = LAB10.Domain.Task;

namespace LAB10.Container;

public interface Container
{
    Task remove();
    void add(Task task);
    int size();
    bool isEmpty();
}