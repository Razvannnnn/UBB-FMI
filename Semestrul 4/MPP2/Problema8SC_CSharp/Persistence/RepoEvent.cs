using System.Data.SQLite;
using log4net;
using Persistence;
using Problema8SC_CSharp.Model;

namespace Problema8SC_CSharp.Persistence;

public class RepoEvent: IRepoEvent
{
    IDictionary<String, string> props;
    private SQLiteConnection _connection = DbUtils.GetConnection();
    private static readonly ILog log = LogManager.GetLogger("RepoEvent");

    public RepoEvent(IDictionary<String, string> props)
    {
        this.props = props;
    }
    
    public Event FindOne(long id)
    {
        log.Info($"Finding event {id}");
        String select = "SELECT * FROM event WHERE event_id = @Id";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        command.Parameters.AddWithValue("@Id", id);
        SQLiteDataReader reader = command.ExecuteReader();
        if (reader.Read())
        {
            var ev = new Event(
                (long)reader["event_id"],
                (string)reader["name"],
                (int)reader["distance"],
                (long)reader["age_group_id"]

            );
            log.Info($"Found event {ev}");
            return ev;
        }
        log.Info($"Event {id} not found");
        return null;
    }

    public IEnumerable<Event> FindAll()
    {
        log.Info("Finding all events");
        String select = "SELECT * FROM event";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        SQLiteDataReader reader = command.ExecuteReader();
        List<Event> events = new List<Event>();
        while (reader.Read())
        {
            var ev = new Event(
                (long)reader["event_id"],
                (string)reader["name"],
                (int)reader["distance"],
                (long)reader["age_group_id"]

            );
            events.Add(ev);
        }
        log.Info($"Found {events.Count} events");
        return events;
    }

    public void Save(Event entity)
    {
        log.Info($"Saving event {entity}");
        String insert = "INSERT INTO event (name, distance) VALUES (@Name, @Distance)";
        SQLiteCommand command = new SQLiteCommand(insert, _connection);
        command.Parameters.AddWithValue("@Name", entity.Name);
        command.Parameters.AddWithValue("@Distance", entity.Distance);
        command.ExecuteNonQuery();
    }

    public void Delete(long id)
    {
        log.Info($"Deleting event {id}");
        String delete = "DELETE FROM event WHERE event_id = @Id";
        SQLiteCommand command = new SQLiteCommand(delete, _connection);
        command.Parameters.AddWithValue("@Id", id);
        command.ExecuteNonQuery();
    }

    public void Update(Event entity)
    {
        log.Info($"Updating event {entity}");
        String update = "UPDATE event SET name = @Name, distance = @Distance WHERE event_id = @Id";
        SQLiteCommand command = new SQLiteCommand(update, _connection);
        command.Parameters.AddWithValue("@Name", entity.Name);
        command.Parameters.AddWithValue("@Distance", entity.Distance);
        command.Parameters.AddWithValue("@Id", entity.GetId());
        command.ExecuteNonQuery();
    }
    
    public IEnumerable<Event> GetEventsByAgeGroup(long ageGroupId)
    {
        log.Info($"Finding events for age group {ageGroupId}");
        String select = "SELECT * FROM event WHERE age_group_id = @AgeGroupId";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        command.Parameters.AddWithValue("@AgeGroupId", ageGroupId);
        SQLiteDataReader reader = command.ExecuteReader();
        List<Event> events = new List<Event>();
        while (reader.Read())
        {
            var ev = new Event(
                (long)reader["event_id"],
                (string)reader["name"],
                (int)reader["distance"],
                (long)reader["age_group_id"]

            );
            events.Add(ev);
        }
        log.Info($"Found {events.Count} events for age group {ageGroupId}");
        return events;
    }
}