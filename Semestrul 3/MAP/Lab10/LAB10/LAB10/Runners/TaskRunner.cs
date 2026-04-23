namespace LAB10.Runners;
using LAB10.Domain;

public interface TaskRunner
{
    void ExecuteOneTask();
    void ExecuteAll();
    void AddTask(Task task);
    bool HasTask();
}