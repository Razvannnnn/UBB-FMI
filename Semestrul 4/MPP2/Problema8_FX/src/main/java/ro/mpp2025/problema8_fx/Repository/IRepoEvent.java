package ro.mpp2025.problema8_fx.Repository;

import ro.mpp2025.problema8_fx.Domain.Event;

public interface IRepoEvent extends IRepository<Event, Long> {
    Iterable<Event> findByAgeGroup(Long ageGroupId);
}
