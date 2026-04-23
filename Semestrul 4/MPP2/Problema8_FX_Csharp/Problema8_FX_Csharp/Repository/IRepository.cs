using Problema8_FX_Csharp.Domain;

namespace Problema8_FX_Csharp.Repository;

public interface IRepository<T, ID> where T : Entity<ID>
{
    T FindOne(ID id);
    IEnumerable<T> FindAll();
    void Save(T entity);
    void Delete(ID id);
    void Update(T entity);
}