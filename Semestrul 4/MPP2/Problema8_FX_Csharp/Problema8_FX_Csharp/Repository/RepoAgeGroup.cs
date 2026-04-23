using System.Data.SQLite;
using log4net;
using Problema8_FX_Csharp.Domain;
using Problema8_FX_Csharp.Utils;

namespace Problema8_FX_Csharp.Repository;

public class RepoAgeGroup : IRepoAgeGroup
{
    private SQLiteConnection _connection = DbUtils.GetConnection();
    private static readonly ILog log = LogManager.GetLogger("RepoAgeGroup");
    
    public AgeGroup FindOne(long id)
    {
        log.Info($"Finding age group {id}");
        String select = "SELECT * FROM age_group WHERE age_group_id = @Id";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        command.Parameters.AddWithValue("@Id", id);
        SQLiteDataReader reader = command.ExecuteReader();
        if (reader.Read())
        {
            var ageGroup = new AgeGroup(
                (long)reader["age_group_id"],
                (string)reader["name"],
                (int)reader["min_age"],
                (int)reader["max_age"]
            );
            log.Info($"Found age group {ageGroup}");
            return ageGroup;
        }
        log.Info($"Age group {id} not found");
        return null;
    }

    public IEnumerable<AgeGroup> FindAll()
    { 
        log.Info("Finding all age groups");
        String select = "SELECT * FROM age_group";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        SQLiteDataReader reader = command.ExecuteReader();
        List<AgeGroup> ageGroups = new List<AgeGroup>();
        while (reader.Read())
        {
            var ageGroup = new AgeGroup(
                (long)reader["age_group_id"],
                (string)reader["name"],
                (int)reader["min_age"],
                (int)reader["max_age"]
            );
            ageGroups.Add(ageGroup);
        }
        log.Info($"Found {ageGroups.Count} age groups");
        return ageGroups;
    }

    public void Save(AgeGroup entity)
    {
        log.Info($"Saving age group {entity}");
        String insert = "INSERT INTO age_group (name, min_age, max_age) VALUES (@Name, @MinAge, @MaxAge)";
        SQLiteCommand command = new SQLiteCommand(insert, _connection);
        command.Parameters.AddWithValue("@Name", entity.Name);
        command.Parameters.AddWithValue("@MinAge", entity.MinAge);
        command.Parameters.AddWithValue("@MaxAge", entity.MaxAge);
        command.ExecuteNonQuery();
    }

    public void Delete(long id)
    {
        log.Info($"Deleting age group {id}");
        String delete = "DELETE FROM age_group WHERE age_group_id = @Id";
        SQLiteCommand command = new SQLiteCommand(delete, _connection);
        command.Parameters.AddWithValue("@Id", id);
        command.ExecuteNonQuery();
    }

    public void Update(AgeGroup entity)
    {
        log.Info($"Updating age group {entity}");
        String update = "UPDATE age_group SET name = @Name, min_age = @MinAge, max_age = @MaxAge WHERE age_group_id = @Id";
        SQLiteCommand command = new SQLiteCommand(update, _connection);
        command.Parameters.AddWithValue("@Name", entity.Name);
        command.Parameters.AddWithValue("@MinAge", entity.MinAge);
        command.Parameters.AddWithValue("@MaxAge", entity.MaxAge);
        command.Parameters.AddWithValue("@Id", entity.GetId());
        command.ExecuteNonQuery();
    }
}