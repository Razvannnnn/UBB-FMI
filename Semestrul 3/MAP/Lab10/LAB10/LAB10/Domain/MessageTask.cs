namespace LAB10.Domain;

public class MessageTask: Task
{
    public string mesaj { get; set; }
    public string from { get; set; }
    public string to { get; set; }
    public DateTime date { get; set; }
    
    public MessageTask(string taskId, string descriere, string mesaj, string from, string to, DateTime date) : base(taskId, descriere)
    {
        this.mesaj = mesaj;
        this.from = from;
        this.to = to;
        this.date = date;
    }
    
    public override void execute()
    {
        Console.WriteLine($"id{taskID}|description{descriere}|message{mesaj}|from={from}|to={to}|date={date.ToString("yyyy-mm-dd hh:mm")}");
    }
    
}