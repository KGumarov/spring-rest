package web.dao;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteUser(long id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager
                .createQuery("SELECT DISTINCT user FROM User user JOIN FETCH user.roles", User.class)
                .getResultList();

    }

    @Override
    public User getUser(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUserByLogin(String login) {
        /*TypedQuery<User> query = entityManager.createQuery(
                "SELECT user FROM User user JOIN FETCH user.roles WHERE user.username = :login",
                User.class);
        query.setParameter("login", login);
        List<User> list = query.getResultList();
        if (list.isEmpty()) {
            throw new UsernameNotFoundException("User " + login + "not found");
        }
        return list.get(0);*/

        User user = entityManager.
                createQuery("SELECT user FROM User user JOIN FETCH user.roles WHERE user.username = :login", User.class).
                setParameter("login", login).getSingleResult();
        return user;

    }
}