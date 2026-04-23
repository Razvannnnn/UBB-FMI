using System.Data.SQLite;
using log4net;
using Persistence;
using Problema8SC_CSharp.Model;
namespace Problema8SC_CSharp.Persistence;

public class RepoChild: IRepoChild
{
    IDictionary<String, string> props;
    private SQLiteConnection _connection = DbUtils.GetConnection();
    private static readonly ILog log = LogManager.GetLogger("RepoChild");
    
    public RepoChild(IDictionary<String, string> props)
    {
        this.props = props;
    }
    
    public Child FindOne(long id)
    {
        log.Info($"Finding child {id}");
        String select = "SELECT * FROM child WHERE child_id = @Id";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        command.Parameters.AddWithValue("@Id", id);
        SQLiteDataReader reader = command.ExecuteReader();
        if (reader.Read())
        {
            var child = new Child(
                (long)reader["child_id"],
                (string)reader["name"],
                (string)reader["CNP"]
            );
            log.Info($"Found child {child}");
            return child;
        }
        log.Info($"Child {id} not found");
        return null;
    }

    public IEnumerable<Child> FindAll()
    {
        log.Info("Finding all children");
        String select = "SELECT * FROM child";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        SQLiteDataReader reader = command.ExecuteReader();
        List<Child> children = new List<Child>();
        while (reader.Read())
        {
            var child = new Child(
                (long)reader["child_id"],
                (string)reader["name"],
                (string)reader["CNP"]
            );
            children.Add(child);
        }
        log.Info($"Found {children.Count} children");
        return children;
    }

    public void Save(Child entity)
    {
        log.Info($"Saving child {entity}");
        String insert = "INSERT INTO child (name, CNP) VALUES (@Name, @CNP)";
        SQLiteCommand command = new SQLiteCommand(insert, _connection);
        command.Parameters.AddWithValue("@Name", entity.Name);
        command.Parameters.AddWithValue("@CNP", entity.CNP);
        command.ExecuteNonQuery();
    }

    public void Delete(long id)
    {
        log.Info($"Deleting child {id}");
        String delete = "DELETE FROM child WHERE child_id = @Id";
        SQLiteCommand command = new SQLiteCommand(delete, _connection);
        command.Parameters.AddWithValue("@Id", id);
        command.ExecuteNonQuery();
    }

    public void Update(Child entity)
    {
        log.Info($"Updating child {entity}");
        String update = "UPDATE child SET name = @Name, CNP = @CNP WHERE child_id = @Id";
        SQLiteCommand command = new SQLiteCommand(update, _connection);
        command.Parameters.AddWithValue("@Name", entity.Name);
        command.Parameters.AddWithValue("@CNP", entity.CNP);
        command.Parameters.AddWithValue("@Id", entity.GetId());
        command.ExecuteNonQuery();
    }

    public Child FindOneCNP(string cnp)
    {
        log.Info($"Finding child {cnp}");
        String select = "SELECT * FROM child WHERE CNP = @CNP";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        command.Parameters.AddWithValue("@CNP", cnp);
        SQLiteDataReader reader = command.ExecuteReader();
        if (reader.Read())
        {
            var child = new Child(
                (long)reader["child_id"],
                (string)reader["name"],
                (string)reader["CNP"]
            );
            log.Info($"Found child {child}");
            return child;
        }
        log.Info($"Child {cnp} not found");
        return null;
    }

    public IEnumerable<Child> FindByEvent(long id)
    {
        log.Info($"Finding child {id}");
        String select = "SELECT * FROM child WHERE child_id IN (SELECT child_id FROM enrollment WHERE event_id = ?)";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        command.Parameters.AddWithValue("@Id", id);
        SQLiteDataReader reader = command.ExecuteReader();
        List<Child> children = new List<Child>();
        while (reader.Read())
        {
            var child = new Child(
                (long)reader["child_id"],
                (string)reader["name"],
                (string)reader["CNP"]
            );
            children.Add(child);
        }
        log.Info($"Found {children.Count} children");
        return children;
    }
}