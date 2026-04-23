using LAB10.Domain;
using LAB10.Runners;

namespace LAB10.Tests;

public class PrinterTaskRunnerTest
{
    public static void printerTaskRunnerTest(Enum.ContainerStrategy strategy, MessageTask[] tasks)
    {
        StrategyTaskRunner strategyTaskRunner = new StrategyTaskRunner(strategy);
        PrinterTaskRunner printerTaskRunner = new PrinterTaskRunner(strategyTaskRunner);
        printerTaskRunner.AddTask(tasks[0]);
        printerTaskRunner.AddTask(tasks[1]);
        printerTaskRunner.AddTask(tasks[2]);
        printerTaskRunner.AddTask(tasks[3]);
        while (printerTaskRunner.HasTask()) 
        {
            printerTaskRunner.ExecuteOneTask();
        }
    }
}