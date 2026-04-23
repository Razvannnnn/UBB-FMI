using Problema8_FX_Csharp.Domain;

namespace Problema8_FX_Csharp.Repository;

public interface IRepoUser: IRepository<User, long>
{
    public User Login(String username, String password);
    
}