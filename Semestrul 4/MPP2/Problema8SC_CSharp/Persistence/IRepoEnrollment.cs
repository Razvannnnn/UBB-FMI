namespace Problema8SC_CSharp.Persistence;

using Problema8SC_CSharp.Model;

public interface IRepoEnrollment : IRepository<Enrollment, long>
{
    long GetNumberOfEvents(long id);
}