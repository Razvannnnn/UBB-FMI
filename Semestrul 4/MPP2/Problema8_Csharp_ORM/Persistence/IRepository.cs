using Problema8SC_CSharp.Model;


namespace Problema8SC_CSharp.Persistence;

public interface IRepository<T, ID> where T : Entity<ID>
{
    T FindOne(ID id);
    IEnumerable<T> FindAll();
    void Save(T entity);
    void Delete(ID id);
    void Update(T entity);
}