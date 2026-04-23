using Problema8SC_CSharp.Model;


namespace Problema8SC_CSharp.Persistence;

public interface IRepoUser: IRepository<User, long>
{
    public User Login(String username, String password);
    
}