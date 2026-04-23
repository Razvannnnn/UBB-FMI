using LAB10.Domain;
using LAB10.Enum;
using LAB10.Runners;

namespace LAB10.Tests;

public class StrategyTaskRunnerTest
{
    public static void test(ContainerStrategy strategy, MessageTask[] tasks)
    {
        TaskRunner runner = new StrategyTaskRunner(strategy);
        runner.AddTask(tasks[0]);
        runner.AddTask(tasks[1]);
        runner.AddTask(tasks[2]);
        runner.AddTask(tasks[3]);
        runner.AddTask(tasks[4]);
        runner.ExecuteAll();
    }
}