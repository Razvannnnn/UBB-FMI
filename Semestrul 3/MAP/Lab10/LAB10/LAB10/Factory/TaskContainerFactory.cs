using LAB10.Container;
using LAB10.Domain;
using LAB10.Enum;

namespace LAB10.Factory;

public class TaskContainerFactory: Factory
{
    
    public TaskContainerFactory()
    {
    }
    
    private static TaskContainerFactory _instance;
    
    public static TaskContainerFactory getInstance()
    {
        if (_instance == null)
        {
            _instance = new TaskContainerFactory();
        }
        return _instance;
    }

    public Container.Container createContainer(ContainerStrategy strategy)
    {
        switch (strategy)
        {
            case ContainerStrategy.QUEUE:
                return new QueueContainer(100);
            case ContainerStrategy.STACK:
                return new StackContainer(100);
            default:
                return null;
        }
    }
}