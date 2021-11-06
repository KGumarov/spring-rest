package web.dao;

import web.model.User;
import java.util.List;

public interface UserDao {
    void saveUser(User user);
    void deleteUser(long id);
    List<User> getAllUsers();
    User getUser(long id);
    User getUserByLogin(String login);
}