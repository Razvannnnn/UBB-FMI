package org.example.reteasocializare.Repository.Paging;

import org.example.reteasocializare.Domain.Entity;
import org.example.reteasocializare.Repository.Repository;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findAll(Pageable pageable);
}
