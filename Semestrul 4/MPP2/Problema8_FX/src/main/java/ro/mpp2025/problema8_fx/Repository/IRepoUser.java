package ro.mpp2025.problema8_fx.Repository;

import ro.mpp2025.problema8_fx.Domain.User;

public interface IRepoUser extends IRepository<User, Long> {
    User login(String username, String password);
}
