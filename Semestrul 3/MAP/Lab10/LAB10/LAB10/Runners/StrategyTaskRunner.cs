using LAB10.Enum;
using LAB10.Domain;
using Task = LAB10.Domain.Task;

namespace LAB10.Runners;

public class StrategyTaskRunner: TaskRunner
{
    private Container.Container _container;
    private ContainerStrategy _strategy;
    
    public StrategyTaskRunner(ContainerStrategy strategy)
    {
        _strategy = strategy;
        _container = Factory.TaskContainerFactory.getInstance().createContainer(_strategy);
    }

    public void ExecuteOneTask()
    {
        _container.remove().execute();
    }

    public void ExecuteAll()
    {
        while (_container.size() > 0)
        {
            ExecuteOneTask();
        }
    }

    public void AddTask(Task task)
    {
        _container.add(task);
    }

    public bool HasTask()
    {
        return !_container.isEmpty();
    }
}