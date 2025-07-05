package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Set<Role> getRolesByIds(List<Long> ids);
    void saveRole(Role role);
    Role findByName(String name);

    Set<Role> findByIdIn(List<Long> roleIds);
}
