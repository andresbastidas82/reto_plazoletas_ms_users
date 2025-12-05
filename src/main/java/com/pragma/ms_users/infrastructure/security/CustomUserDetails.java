package com.pragma.ms_users.infrastructure.security;

import com.pragma.ms_users.infrastructure.out.jpa.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String name;
    private String email;
    private String password;
    private List<SimpleGrantedAuthority> authorities;

    // Metodo estático para construir CustomUserDetails desde tu UserEntity
    public static CustomUserDetails build(UserEntity userEntity) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(userEntity.getRole().name())
        );
        String fullName = userEntity.getName() + " " + userEntity.getLastName();
        return new CustomUserDetails(
                userEntity.getId(),
                fullName ,
                userEntity.getEmail(),
                userEntity.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Usamos el email como el nombre de usuario para Spring Security
    }

    // Métodos de la interfaz UserDetails, puedes dejarlos como true si no
    // tienes lógica de expiración/bloqueo
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
