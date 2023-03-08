package com.example.security.security.service.serviceimpl;

import com.example.security.security.dto.CustomerDTO;
import com.example.security.security.model.Customer;
import com.example.security.security.repository.CustomerRepository;
import com.example.security.security.service.CustomerService;
import com.example.security.security.util.Roles;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService, UserDetailsService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Customer saveCustomer(CustomerDTO customerDTO) {
        String ex = customerDTO.getPassword();
        log.info("Saving the customer with the username {}", customerDTO.getUsername());
        String password = passwordEncoder.encode(ex);
        Customer customer = new Customer(customerDTO.getUsername(), password, Roles.ROLE_ADMIN.name()+ "," + Roles.ROLE_USER.name());
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomer(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public void updatePassword() {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username);
        if(customer == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found:"+ username);
        }
        else {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            String[] role = customer.getRoles().split(",");
            Arrays.stream(role).forEach(authRoles -> authorities.add(new SimpleGrantedAuthority(authRoles)));
            return new User(customer.getUsername(), customer.getPassword(), authorities);
        }

    }
}