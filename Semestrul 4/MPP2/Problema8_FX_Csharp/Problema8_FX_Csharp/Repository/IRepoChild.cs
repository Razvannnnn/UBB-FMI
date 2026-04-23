namespace Problema8_FX_Csharp.Repository;

using Problema8_FX_Csharp.Domain;

public interface IRepoChild : IRepository<Child, long>
{
    public Child FindOneCNP(string cnp);
    public IEnumerable<Child> FindByEvent(long eventId);
}