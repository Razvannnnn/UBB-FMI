package problema8.persistence.irepository;

import problema8.persistence.IRepository;
import problema8.model.User;

public interface IRepoUser extends IRepository<User, Long> {
    User login(String username, String password);
}
