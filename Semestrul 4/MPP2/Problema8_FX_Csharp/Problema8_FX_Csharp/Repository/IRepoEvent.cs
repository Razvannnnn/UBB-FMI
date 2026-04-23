using Problema8_FX_Csharp.Domain;

namespace Problema8_FX_Csharp.Repository;

public interface IRepoEvent : IRepository<Event, long>
{
    public IEnumerable<Event> GetEventsByAgeGroup(long ageGroupId);
}