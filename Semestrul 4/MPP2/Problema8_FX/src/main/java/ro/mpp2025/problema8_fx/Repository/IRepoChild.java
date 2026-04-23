package ro.mpp2025.problema8_fx.Repository;

import ro.mpp2025.problema8_fx.Domain.Child;

public interface IRepoChild extends IRepository<Child, Long> {
    Child findOneCNP(String cnp);
    Iterable<Child> findByEvent(Long id);
}
