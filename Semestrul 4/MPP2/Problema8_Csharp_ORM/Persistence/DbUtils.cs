namespace Persistence;

using System;
using System.Configuration;
using System.Data.SQLite;
using log4net;

public class DbUtils
{
    private static readonly ILog logger = LogManager.GetLogger(typeof(DbUtils));
    private static readonly string _connectionString;
    private static SQLiteConnection instance = null;

    static DbUtils()
    {
        _connectionString = ConfigurationManager.ConnectionStrings["Concurs"].ConnectionString;
    }

    private static SQLiteConnection GetNewConnection()
    {

        if (string.IsNullOrEmpty(_connectionString))
        {
            logger.Error("Connection string not found in configuration");
            throw new InvalidOperationException("Missing connection string in App.config");
        }

        try
        {
            var connection = new SQLiteConnection(_connectionString);
            connection.Open();
            return connection;
        }
        catch (SQLiteException ex)
        {
            return null;
        }
    }

    public static SQLiteConnection GetConnection()
    {
        if (instance == null || instance.State != System.Data.ConnectionState.Open)
        {
            instance = GetNewConnection();
        }

        return instance;
    }
}