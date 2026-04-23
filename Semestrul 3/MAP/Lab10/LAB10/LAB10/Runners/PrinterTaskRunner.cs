namespace LAB10.Runners;

public class PrinterTaskRunner: AbstractTaskRunner
{
    public PrinterTaskRunner(TaskRunner runner): base(runner) {}

    public new void ExecuteOneTask()
    {
        base.ExecuteOneTask();
        Console.WriteLine($"Task executed at {DateTime.Now}");
    }
}