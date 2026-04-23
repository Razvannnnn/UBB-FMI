package org.example.reteasocializare.Repository.Paging;

import org.example.reteasocializare.Domain.Entity;

public interface PrieteniePagingRepository<ID, E extends Entity<ID>> extends PagingRepository<ID, E> {
    Page<E> findAllFriendRequests(Pageable pageable);

    Page<E> findAllUserFriends(Pageable pageable, Long id);

    Page<E> findAllUserFriendRequests(Pageable pageable, Long id);
}