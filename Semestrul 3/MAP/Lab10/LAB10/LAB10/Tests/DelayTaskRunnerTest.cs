using LAB10.Runners;

namespace LAB10.Tests;
using LAB10.Domain;

public class DelayTaskRunnerTest
{
    public static void delayTaskRunnerTest(Enum.ContainerStrategy strategy, MessageTask[] tasks)
    {
        StrategyTaskRunner strategyTaskRunner = new StrategyTaskRunner(strategy);
        DelayTaskRunner delayTaskRunner = new DelayTaskRunner(strategyTaskRunner);
        delayTaskRunner.AddTask(tasks[0]);
        delayTaskRunner.AddTask(tasks[1]);
        delayTaskRunner.AddTask(tasks[2]);
        delayTaskRunner.AddTask(tasks[3]);
        while (delayTaskRunner.HasTask()) 
        {
            delayTaskRunner.ExecuteOneTask();
        }
    }
}