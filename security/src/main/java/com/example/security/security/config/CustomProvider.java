package com.example.security.security.config;

import com.example.security.security.model.Customer;
import com.example.security.security.repository.CustomerRepository;
import com.example.security.security.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomProvider implements AuthenticationProvider {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        Customer customer = customerService.getCustomer(username);
        if(customer == null) {
            throw new BadCredentialsException("No user registered with this details!");
        }
        else {
            if(passwordEncoder.matches(password, customer.getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, password, getGrantedAuthorities(customer.getRoles()));
            }
            else {
                throw new BadCredentialsException("Invalid password!");
            }
        }


    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private List<GrantedAuthority> getGrantedAuthorities(String roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Arrays.stream(roles.split(",")).forEach(authority -> grantedAuthorities.add(new SimpleGrantedAuthority(authority)));
        return grantedAuthorities;
    }
}
