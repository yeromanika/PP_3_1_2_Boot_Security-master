package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        initializeDefaultRoles();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Set<Role> getRolesByIds(List<Long> ids) {
        return ids.stream()
                .map(this::getRoleById)
                .collect(Collectors.toSet());
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @PostConstruct
    private void initializeDefaultRoles() {
        if (roleRepository.count() == 0) {
            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");
            roleRepository.save(adminRole);
            roleRepository.save(userRole);
        }
    }
    public Set<Role> findByIdIn(List<Long> ids) {
        return roleRepository.findByIdIn(ids);
    }
}