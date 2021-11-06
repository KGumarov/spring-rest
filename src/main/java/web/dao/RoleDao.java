package web.dao;

import web.model.Role;

import java.util.List;

public interface RoleDao {

    Role getRole(long id);
    List<Role> getAllRoles();
}