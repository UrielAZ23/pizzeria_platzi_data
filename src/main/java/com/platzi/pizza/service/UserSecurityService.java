package com.platzi.pizza.service;

import com.platzi.pizza.persitence.entity.UserEntity;
import com.platzi.pizza.persitence.entity.UserRoleEntity;
import com.platzi.pizza.persitence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository.findById(username)
                .orElseThrow(()-> new RuntimeException("User "+username+" not found"));

        String[] roles = userEntity.getRoles().stream().map(UserRoleEntity::getRole).toArray(String[]::new);        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
//                .roles("ADMIN")
//                .roles(roles)
                .authorities(this.grantedAuthorities(roles))
                .accountLocked(userEntity.getLocked())
                .disabled(userEntity.getDisabled())
                .build();
    }

    private String [] getAuthorities(String role){
        if ("ADMIN".equals(role) || "CUSTOMER".equals(role)){
            return new String[]{"random_order"};
        }
        return new String[]{};
    }
    private List<GrantedAuthority> grantedAuthorities (String[] roles){
        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
        for (String rol:roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+rol));
            for (String authority:this.getAuthorities(rol)) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }
        return authorities;
    }
}
