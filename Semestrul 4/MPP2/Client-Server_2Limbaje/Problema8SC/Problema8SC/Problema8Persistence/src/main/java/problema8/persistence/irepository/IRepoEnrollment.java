package problema8.persistence.irepository;

import problema8.model.Enrollment;
import problema8.persistence.IRepository;

public interface IRepoEnrollment extends IRepository<Enrollment, Long> {
    int getNumberOfEvents(Long id);
}
