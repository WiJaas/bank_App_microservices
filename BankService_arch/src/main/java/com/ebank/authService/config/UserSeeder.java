package com.ebank.authService.config;

import com.ebank.authService.model.Role;
import com.ebank.authService.model.User;
import com.ebank.authService.model.Permission;
import com.ebank.authService.repository.RoleRepository;
import com.ebank.authService.repository.UserRepository;
import com.ebank.authService.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private static final String ROLE_AGENT = "AGENT_GUICHET";
    private static final String ROLE_CLIENT = "CLIENT";

    private static final String PERM_CLIENT_CREATE = "CLIENT_CREATE";
    private static final String PERM_CLIENT_READ = "CLIENT_READ";
    private static final String PERM_CLIENT_UPDATE = "CLIENT_UPDATE";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // 1) permissions
        Permission pCreate = getOrCreate(PERM_CLIENT_CREATE);
        Permission pRead   = getOrCreate(PERM_CLIENT_READ);
        Permission pUpdate = getOrCreate(PERM_CLIENT_UPDATE);

        // 2) roles
        Role agentRole = roleRepository.findByName(ROLE_AGENT)
                .orElseGet(() -> roleRepository.save(
                        new Role(null, ROLE_AGENT, new HashSet<>())
                ));

        Role clientRole = roleRepository.findByName(ROLE_CLIENT)
                .orElseGet(() -> roleRepository.save(
                        new Role(null, ROLE_CLIENT, new HashSet<>())
                ));

        // assign permissions to roles
        agentRole.setPermissions(Set.of(pCreate, pRead, pUpdate));
        clientRole.setPermissions(Set.of(pRead));

        roleRepository.save(agentRole);
        roleRepository.save(clientRole);

        // 3) users
        seedUserIfMissing("agent@bank.com", agentRole);
        seedUserIfMissing("client@bank.com", clientRole);
    }

    private Permission getOrCreate(String name) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(
                        new Permission(null, name)
                ));
    }

    private void seedUserIfMissing(String email, Role role) {
        userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            u.setUsername(email);
            u.setPassword(passwordEncoder.encode("password123"));
            u.setEnabled(true);
            u.setRoles(Set.of(role));
            userRepository.save(u);
            return u;
        });
    }
}
