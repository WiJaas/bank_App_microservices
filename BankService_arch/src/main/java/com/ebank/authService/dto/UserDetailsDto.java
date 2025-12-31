package com.ebank.authService.dto;

import com.ebank.authService.model.Role;
import com.ebank.authService.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

@Getter
public class UserDetailsDto implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final Set<Role> roles;

    public UserDetailsDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return roles.stream()
                .flatMap(role -> {
                    // role authority
                    Stream<GrantedAuthority> roleAuth =
                            Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));

                    // permission authorities
                    Stream<GrantedAuthority> permAuth =
                            role.getPermissions().stream()
                                    .map(permission ->
                                            new SimpleGrantedAuthority(permission.getName()));

                    return Stream.concat(roleAuth, permAuth);
                })
                .toList();
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
