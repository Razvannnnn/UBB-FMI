using System.Diagnostics.CodeAnalysis;

namespace chat.model;

public class User : IIdentifiable<string>
{
    private  String username, passwd, name;
    private readonly IList<User> friends;

    public User(string id):this(id,"","")
    {
    }

    public User(string id, string passwd):this(id,passwd,"")
    {
    }

    public User(string id, string passwd, string name)
    {
        this.username = id;
        this.passwd = passwd;
        this.name = name;
        friends = new List<User>();
    }

    public string Id
    {
        get { return username; }
        set { username = value; }
       
    }

    public string Password
    {
        get { return passwd; }
        set{ passwd = value;}
        
    }

    public string Name
    {
        get { return name; }
        set { name = value;}
       
    }
    public User[] Friends
    {
        get { return friends.ToArray(); }
    }

    public void addFriend(User user)
    {
        if (!friends.Contains(user))
            friends.Add(user);
    }

    public void removeFriend(User user)
    {
        friends.Remove(user);
    }

    public bool Equals(User obj)
    {
        if (ReferenceEquals(null, obj)) return false;
        if (ReferenceEquals(this, obj)) return true;
        return Equals(obj.username, username);
    }

    
    public override bool Equals(object? obj)
    {
        if (ReferenceEquals(null, obj)) return false;
        if (ReferenceEquals(this, obj)) return true;
        if (obj.GetType() != typeof (User)) return false;
        return Equals((User) obj);
    }

    public override int GetHashCode()
    {
        return username.GetHashCode();
    }

    public override string ToString()
    {
        return string.Format("Id: {0}, Name: {1}", username, name);
    }
}