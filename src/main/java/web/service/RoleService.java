package web.service;

import web.model.Role;
import web.model.User;

import java.util.List;

public interface RoleService {
    Role getRole(long id);
    List<Role> getAllRoles();
}
