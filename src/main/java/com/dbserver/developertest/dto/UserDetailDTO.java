package com.dbserver.developertest.dto;

import com.dbserver.developertest.model.HungryProfessional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;

public class UserDetailDTO implements UserDetails {

    private String login;
    private String password;

    public UserDetailDTO (Optional<HungryProfessional> hungryProfessional) {
        this.login = hungryProfessional.get().getNickname();
        this.password = hungryProfessional.get().getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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
}
