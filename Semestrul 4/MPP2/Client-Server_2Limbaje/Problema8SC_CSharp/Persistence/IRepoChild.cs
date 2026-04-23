namespace Problema8SC_CSharp.Persistence;

using Problema8SC_CSharp.Model;

public interface IRepoChild : IRepository<Child, long>
{
    public Child FindOneCNP(string cnp);
    public IEnumerable<Child> FindByEvent(long eventId);
}