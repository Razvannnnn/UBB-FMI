using Task = LAB10.Domain.Task;

namespace LAB10.Container;

public abstract class AbstractContainer: Container
{
    protected Task[] _tasks;
    protected int _size;
    
    public AbstractContainer(int capacity)
    {
        _tasks = new Task[capacity];
        _size = 0;
    }

    public void add(Task task)
    {
        if (_size == _tasks.Length)
        {
            Task[] t = new Task[_size * 2];
            for (int i = 0; i < _size; i++)
            {
                t[i] = _tasks[i];
            }
            _tasks = t;
        }
        _tasks[_size++] = task; 
    }

    public abstract Task remove();
    

    public int size()
    {
        return _size;
    }
    
    public bool isEmpty()
    {
        return _size == 0;
    }
}