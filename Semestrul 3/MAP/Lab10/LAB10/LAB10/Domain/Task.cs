namespace LAB10.Domain;

public abstract class Task
{
    public string taskID { get; set; }
    public string descriere { get; set; }

    protected Task(string taskId, string descriere)
    {
        taskID = taskId;
        this.descriere = descriere;
    }

    public virtual void execute() {}
}