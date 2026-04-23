using LAB10.Enum;
using Task = LAB10.Domain.Task;

namespace LAB10.Container;

public class StackContainer:AbstractContainer
{
    public StackContainer(int capacity) : base(capacity)
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
        Task task = _tasks[_size - 1];
        _size--;
        return task;
    }
}