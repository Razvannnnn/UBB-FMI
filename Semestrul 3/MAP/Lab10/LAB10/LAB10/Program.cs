using System;
using LAB10.Domain;
using LAB10.Enum;
using LAB10.Tests;

namespace LAB10
{
    class Program
    {
        static void Main(string[] args)
        {
            string cmd = "";
            if(args.Length > 0)
            {
                cmd = args[0];
            }
            
            SortingTask sortingTask = new SortingTask("1", "Sortare", new int[] { 3, 4, 8, 5, 10, 2, 1 }, Enum.SortStrategy.BUBBLESORT);
            sortingTask.execute();
            SortingTask sortingTask2 = new SortingTask("1", "Sortare", new int[] { 3, 4, 8, 5, 10, 2, 1 }, Enum.SortStrategy.QUICKSORT);
            sortingTask2.execute();
            MessageTask messageTask = new MessageTask("2", "Mesaj", "Hello, World!", "Ion", "Ana", DateTime.Now);
            messageTask.execute();

            if (cmd.Equals("FIFO"))
            {
                Console.WriteLine("--------------------");
                StrategyTaskRunnerTest.test(ContainerStrategy.QUEUE, MessageTaskTest.generateTasks());
                
                Console.WriteLine("--------printer------------");
                PrinterTaskRunnerTest.printerTaskRunnerTest(ContainerStrategy.QUEUE, MessageTaskTest.generateTasks());
                
                Console.WriteLine("--------delay------------");
                DelayTaskRunnerTest.delayTaskRunnerTest(ContainerStrategy.QUEUE, MessageTaskTest.generateTasks());
            } 
            else if (cmd.Equals("LIFO"))
            {
                Console.WriteLine("--------------------");
                StrategyTaskRunnerTest.test(ContainerStrategy.STACK, MessageTaskTest.generateTasks());

                Console.WriteLine("--------printer------------");
                PrinterTaskRunnerTest.printerTaskRunnerTest(ContainerStrategy.STACK, MessageTaskTest.generateTasks());

                Console.WriteLine("--------delay------------");
                DelayTaskRunnerTest.delayTaskRunnerTest(ContainerStrategy.STACK, MessageTaskTest.generateTasks());
            }
        }
    }
}
