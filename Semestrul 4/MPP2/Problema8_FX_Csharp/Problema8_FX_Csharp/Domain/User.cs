namespace Problema8_FX_Csharp.Domain;

public class User: Entity<long>
{
    public String Username { get; set; }
    public String Password { get; set; }
    
    public User(long id, String username, String password)
    {
        SetId(id);
        this.Username = username;
        this.Password = password;
    }
    
    public override String ToString()
    {
        return $"Username: {Username}, Password: {Password}";
    }
    
    public override bool Equals(Object obj)
    {
        if (obj == null || GetType() != obj.GetType())
        {
            return false;
        }
        
        User user = (User)obj;
        return Username.Equals(user.Username) && Password.Equals(user.Password);
    }
}