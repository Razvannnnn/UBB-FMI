namespace LAB10.Runners;
using LAB10.Domain;

public class AbstractTaskRunner: TaskRunner
{
    private TaskRunner runner;
    
    public AbstractTaskRunner(TaskRunner runner)
    {
        this.runner = runner;
    }
        
    public void ExecuteOneTask()
    {
        runner.ExecuteOneTask();
    }
    
    public void ExecuteAll()
    {
        runner.ExecuteAll();
    }
    
    public void AddTask(Task task)
    {
        runner.AddTask(task);
    }
    
    public bool HasTask()
    {
        return runner.HasTask();
    }
}