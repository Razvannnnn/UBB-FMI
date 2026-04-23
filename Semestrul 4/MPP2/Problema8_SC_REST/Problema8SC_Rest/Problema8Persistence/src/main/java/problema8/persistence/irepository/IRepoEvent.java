package problema8.persistence.irepository;

import problema8.model.Event;
import problema8.persistence.IRepository;

public interface IRepoEvent extends IRepository<Event, Long> {
    Iterable<Event> findByAgeGroup(Long ageGroupId);
}
