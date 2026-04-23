namespace Problema8SC_CSharp.Model;

public class User: Entity<long>
{
    public long Id
    {
        get => GetId();
        set => SetId(value);
    }
    public string Username { get; set; }
    public string Password { get; set; }
    
    public User(long id, string username, string password)
    {
        Id = id;
        this.Username = username;
        this.Password = password;
    }

    public User(string username, string password) : this(0, username, password)
    {
    }
    
    public User(long id, string password) : this(id, "", password)
    {
    }
    
    public override string ToString()
    {
        return $"Username: {Username}, Password: {Password}";
    }
    
    public override bool Equals(object? obj)
    {
        if (obj == null || GetType() != obj.GetType())
        {
            return false;
        }
        
        User user = (User)obj;
        return Username.Equals(user.Username) && Password.Equals(user.Password);
    }

    protected bool Equals(User other)
    {
        return Username == other.Username && Password == other.Password;
    }

    public override int GetHashCode()
    {
        return HashCode.Combine(Username, Password);
    }


    public string GetUsername()
    {
        return Username;
    }

    public string GetPassword()
    {
        return Password;
    }
}