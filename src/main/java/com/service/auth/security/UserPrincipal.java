package com.service.auth.security;

import com.service.auth.model.role.Role;
import com.service.auth.model.user.User;
import com.service.auth.util.Constants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
    @Getter
    private final Long id;
    @Getter
    private final String firstname;
    @Getter
    private final String lastname;
    @Getter
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String firstname, String lastname, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = getGrantedAuthorities(user.getRoles());
        return new UserPrincipal(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public static List<GrantedAuthority> getGrantedAuthorities(Set<Role> roles) {
        ArrayList<String> allPrivileges = new ArrayList<>();
        List<GrantedAuthority> authorities = new ArrayList<>();

        roles.forEach(role -> {
            if (!allPrivileges.contains(role.getName()))
                allPrivileges.add(Constants.ROLE_PREFIX + role.getName()); //Spring security is adding 'ROLE_' prefix while checking for hasRole

            role.getPrivileges().forEach(privilege -> {
                if (!allPrivileges.contains(privilege.getName()))
                    allPrivileges.add(privilege.getName());
            });
        });

        allPrivileges.forEach(privilege -> authorities.add(new SimpleGrantedAuthority(privilege)));

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Set<String> getRoles() {
        return getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

}
