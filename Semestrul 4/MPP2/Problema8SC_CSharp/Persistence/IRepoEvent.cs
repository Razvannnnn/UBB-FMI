namespace Problema8SC_CSharp.Persistence;

using Problema8SC_CSharp.Model;


public interface IRepoEvent : IRepository<Event, long>
{
    public IEnumerable<Event> GetEventsByAgeGroup(long ageGroupId);
}