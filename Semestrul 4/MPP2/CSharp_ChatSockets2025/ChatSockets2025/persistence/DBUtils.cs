using System.Data;
using System.Reflection;
using log4net;
using Microsoft.Data.Sqlite;

namespace chat.persistence;

public static class DBUtils
{
    private static IDbConnection instance = null;
    public static IDbConnection getConnection(IDictionary<String, string> props)
    {
        if (instance == null || instance.State == System.Data.ConnectionState.Closed)
        {
            instance = getNewConnection(props);
            instance.Open();
        }
        return instance;
    }

    private static IDbConnection getNewConnection(IDictionary<String, string> props)
    {

        return ConnectionFactory.getInstance().createConnection(props);


    }
}
public abstract class ConnectionFactory
{
    protected ConnectionFactory()
    {
    }

    private static ConnectionFactory instance;

    public static ConnectionFactory getInstance()
    {
        if (instance == null)
        {

            Assembly assem = Assembly.GetExecutingAssembly();
            Type[] types = assem.GetTypes();
            foreach (var type in types)
            {
                if (type.IsSubclassOf(typeof(ConnectionFactory)))
                    instance = (ConnectionFactory)Activator.CreateInstance(type);
            }
        }
        return instance;
    }

    public abstract  IDbConnection createConnection(IDictionary<String, string> props);
}

public class SqliteConnectionFactory : ConnectionFactory
{
    private static readonly ILog log = LogManager.GetLogger(typeof(SqliteConnectionFactory));
    public override IDbConnection createConnection(IDictionary<String, string> props)
    {
       
           
        //	String connectionString = "URI=file:ChatMPP2017.db,Version=3";
        String connectionString = props["ConnectionString"];
        log.DebugFormat("creating ... sqlite connection for {0}", connectionString);
        return new SqliteConnection(connectionString);
        //return null;


    }
}