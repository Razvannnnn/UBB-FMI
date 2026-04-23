package problema8.persistence.irepository;

import problema8.persistence.IRepository;
import problema8.model.Event;

public interface IRepoEvent extends IRepository<Event, Long> {
    Iterable<Event> findByAgeGroup(Long ageGroupId);
}
