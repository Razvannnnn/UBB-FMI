package problema8.persistence.irepository;

import problema8.persistence.IRepository;
import problema8.model.Child;

public interface IRepoChild extends IRepository<Child, Long> {
    Child findOneCNP(String cnp);
    Iterable<Child> findByEvent(Long id);
}
