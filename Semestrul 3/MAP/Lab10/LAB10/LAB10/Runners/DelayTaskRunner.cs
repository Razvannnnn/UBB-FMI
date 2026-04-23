namespace LAB10.Runners;

public class DelayTaskRunner: AbstractTaskRunner
{
    public DelayTaskRunner(TaskRunner runner): base(runner) {}
    
    public new void ExecuteOneTask()
    {
        base.ExecuteOneTask();
        Console.WriteLine("..waiting..");
        Thread.Sleep(1000);
    }
}