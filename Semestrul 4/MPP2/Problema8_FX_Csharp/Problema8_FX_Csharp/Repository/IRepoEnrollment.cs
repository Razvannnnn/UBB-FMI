namespace Problema8_FX_Csharp.Repository;

using Problema8_FX_Csharp.Domain;

public interface IRepoEnrollment : IRepository<Enrollment, long>
{
    long GetNumberOfEvents(long id);
}