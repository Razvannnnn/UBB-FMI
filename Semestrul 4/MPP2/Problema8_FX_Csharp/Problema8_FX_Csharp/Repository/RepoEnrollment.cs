using System.Data.SQLite;
using log4net;
using Problema8_FX_Csharp.Domain;
using Problema8_FX_Csharp.Utils;

namespace Problema8_FX_Csharp.Repository;

public class RepoEnrollment: IRepoEnrollment
{
    private SQLiteConnection _connection = DbUtils.GetConnection();
    private static readonly ILog log = LogManager.GetLogger("RepoEnrollment");

    
    public Enrollment FindOne(long id)
    {
        log.Info($"Finding enrollment {id}");
        String select = "SELECT e.id, e.child_id, e.event_id, c.name AS child_name, c.cnp AS child_cnp, " +
                        "ev.name AS event_name, ev.distance AS event_distance " +
                        "FROM enrollment e " +
                        "JOIN child c ON e.child_id = c.id " +
                        "JOIN event ev ON e.event_id = ev.id " +
                        "WHERE e.id = @Id";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        command.Parameters.AddWithValue("@Id", id);
        SQLiteDataReader reader = command.ExecuteReader();
        if (reader.Read())
        {
            var child = new Child(
                (long)reader["child_id"],
                (string)reader["child_name"],
                (string)reader["child_cnp"]
            );
            var ev = new Event(
                (long)reader["event_id"],
                (string)reader["event_name"],
                (int)reader["event_distance"]
            );
            var enrollment = new Enrollment(
                (long)reader["id"],
                child,
                ev
            );
            log.Info($"Found enrollment {enrollment}");
            return enrollment;
        }
        log.Info($"Enrollment {id} not found");
        return null;
    }

    public IEnumerable<Enrollment> FindAll()
    {
        log.Info("Finding all enrollments");
        String select = "SELECT e.enrollment_id, e.child_id, e.event_id, c.name AS child_name, c.cnp AS child_cnp, " +
                        "ev.name AS event_name, ev.distance AS event_distance " +
                        "FROM enrollment e " +
                        "JOIN child c ON e.child_id = c.child_id " +
                        "JOIN event ev ON e.event_id = ev.event_id";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        SQLiteDataReader reader = command.ExecuteReader();
        List<Enrollment> enrollments = new List<Enrollment>();
        while (reader.Read())
        {
            var ev = new Event(
                (long)reader["event_id"],
                (string)reader["event_name"],
                (int)reader["event_distance"]
            );
            var child = new Child(
                (long)reader["child_id"],
                (string)reader["child_name"],
                (string)reader["child_cnp"]
            );
            var enrollment = new Enrollment(
                (long)reader["enrollment_id"],
                child,
                ev
            );
            enrollments.Add(enrollment);
        }
        log.Info($"Found {enrollments.Count} enrollments");
        return enrollments;
    }

    public void Save(Enrollment entity)
    {
        log.Info($"Saving enrollment {entity}");
        String insert = "INSERT INTO enrollment (child_id, event_id) VALUES (@ChildId, @EventId)";
        SQLiteCommand command = new SQLiteCommand(insert, _connection);
        command.Parameters.AddWithValue("@ChildId", entity.Child.GetId());
        command.Parameters.AddWithValue("@EventId", entity.Event.GetId());
        command.ExecuteNonQuery();
    }

    public void Delete(long id)
    {
        log.Info($"Deleting enrollment {id}");
        String delete = "DELETE FROM enrollment WHERE enrollment_id = @Id";
        SQLiteCommand command = new SQLiteCommand(delete, _connection);
        command.Parameters.AddWithValue("@Id", id);
        command.ExecuteNonQuery();
    }

    public void Update(Enrollment entity)
    {
        log.Info($"Updating enrollment {entity}");
        String update = "UPDATE enrollment SET child_id = @ChildId, event_id = @EventId WHERE enrollment_id = @Id";
        SQLiteCommand command = new SQLiteCommand(update, _connection);
        command.Parameters.AddWithValue("@ChildId", entity.Child.GetId());
        command.Parameters.AddWithValue("@EventId", entity.Event.GetId());
        command.Parameters.AddWithValue("@Id", entity.GetId());
        command.ExecuteNonQuery();
    }

    public long GetNumberOfEvents(long idChild)
    {
        log.Info($"Getting number of events for child {idChild}");
        String select = "SELECT COUNT(*) FROM enrollment WHERE child_id = @Id";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        command.Parameters.AddWithValue("@Id", idChild);
        SQLiteDataReader reader = command.ExecuteReader();
        if (reader.Read())
        {
            long count = (long)reader[0];
            log.Info($"Found {count} events for child {idChild}");
            return count;
        }
        log.Info($"No events found for child {idChild}");
        return 0;
    }

}