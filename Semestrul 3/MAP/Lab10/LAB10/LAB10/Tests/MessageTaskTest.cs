using LAB10.Domain;

namespace LAB10.Tests;

public class MessageTaskTest
{
    public static MessageTask[] generateTasks()
    {
         MessageTask messageTask = new MessageTask("2", "Mesaj", "Hello, World!", "Ion", "Ana", DateTime.Now);
         MessageTask messageTask2 = new MessageTask("3", "Mesaj", "Hello, World!", "Mihai", "Mirel", DateTime.Now);
         MessageTask messageTask3 = new MessageTask("4", "Mesaj", "Hello, World!", "Ion", "Gigel", DateTime.Now);
         MessageTask messageTask4 = new MessageTask("5", "Mesaj", "Hello, World!", "Florin", "Ionel", DateTime.Now);
         MessageTask messageTask5 = new MessageTask("6", "Mesaj", "Hello, World!", "Gica", "Laura", DateTime.Now);
         
         return new MessageTask[] { messageTask, messageTask2, messageTask3, messageTask4, messageTask5 };
}

    public static void testExecute()
    {
        MessageTask[] tasks = generateTasks();
        foreach (MessageTask task in tasks)
        {
            task.execute();
        }
    }
}