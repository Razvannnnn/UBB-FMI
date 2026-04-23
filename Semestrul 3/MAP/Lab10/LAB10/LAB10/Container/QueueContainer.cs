namespace LAB10.Container;
using Task = LAB10.Domain.Task;


public class QueueContainer:AbstractContainer
{
    public QueueContainer(int capacity) : base(capacity)
    {
        _tasks = new Task[capacity];
        _size = 0;
    }
    
    public override Task remove()
    {
        if (_size == 0)
        {
            return null;
        }
        Task task = _tasks[0];
        for (int i = 0; i < _size - 1; i++)
        {
            _tasks[i] = _tasks[i + 1];
        }
        _size--;
        return task;
    }
    
}