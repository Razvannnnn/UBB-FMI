using System.Data.SQLite;
using log4net;
using Problema8_FX_Csharp.Domain;
using Problema8_FX_Csharp.Utils;

namespace Problema8_FX_Csharp.Repository;

public class RepoUser: IRepoUser
{
    private readonly SQLiteConnection _connection = DbUtils.GetConnection();
    private static readonly ILog log = LogManager.GetLogger("RepoUser");

    
    public User FindOne(long id)
    {
        log.Info($"Finding user {id}");
        String select = "SELECT * FROM user WHERE user_id = @Id";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        command.Parameters.AddWithValue("@Id", id);
        SQLiteDataReader reader = command.ExecuteReader();
        if (reader.Read())
        {
            var user = new User(
                (long)reader["user_id"],
                (string)reader["username"],
                (string)reader["password"]
            );
            log.Info($"Found user {user}");
            return user;
        }
        log.Info($"User {id} not found");
        return null;
    }

    public IEnumerable<User> FindAll()
    {
        log.Info("Finding all users");
        String select = "SELECT * FROM user";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        SQLiteDataReader reader = command.ExecuteReader();
        List<User> users = new List<User>();
        while (reader.Read())
        {
            var user = new User(
                (long)reader["user_id"],
                (string)reader["username"],
                (string)reader["password"]
            );
            users.Add(user);
        }
        log.Info($"Found {users.Count} users");
        return users;
    }

    public void Save(User entity)
    {
        log.Info($"Saving user {entity}");
        String insert = "INSERT INTO user (username, password) VALUES (@Username, @Password)";
        SQLiteCommand command = new SQLiteCommand(insert, _connection);
        command.Parameters.AddWithValue("@Username", entity.Username);
        command.Parameters.AddWithValue("@Password", entity.Password);
        command.ExecuteNonQuery();
    }

    public void Delete(long id)
    {
        log.Info($"Deleting user {id}");
        String delete = "DELETE FROM user WHERE user_id = @Id";
        SQLiteCommand command = new SQLiteCommand(delete, _connection);
        command.Parameters.AddWithValue("@Id", id);
        command.ExecuteNonQuery();
    }

    public void Update(User entity)
    {
        log.Info($"Updating user {entity}");
        String update = "UPDATE user SET username = @Username, password = @Password WHERE user_id = @Id";
        SQLiteCommand command = new SQLiteCommand(update, _connection);
        command.Parameters.AddWithValue("@Username", entity.Username);
        command.Parameters.AddWithValue("@Password", entity.Password);
        command.Parameters.AddWithValue("@Id", entity.GetId());
        command.ExecuteNonQuery();
    }

    public User Login(String username, String password)
    {
        log.Info($"Logging in user {username}");
        String select = "SELECT * FROM user WHERE username = @Username AND password = @Password";
        SQLiteCommand command = new SQLiteCommand(select, _connection);
        command.Parameters.AddWithValue("@Username", username);
        command.Parameters.AddWithValue("@Password", password);
        SQLiteDataReader reader = command.ExecuteReader();
        if (reader.Read())
        {
            var user = new User(
                (long)reader["user_id"],
                (string)reader["username"],
                (string)reader["password"]
            );
            log.Info($"Logged in user {user}");
            return user;
        }

        log.Info($"User {username} not found");
        return null;
    }
}