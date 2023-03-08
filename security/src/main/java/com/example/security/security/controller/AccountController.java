package com.example.security.security.controller;

import com.example.security.security.dto.CustomerDTO;
import com.example.security.security.model.Customer;
import com.example.security.security.repository.CustomerRepository;
import com.example.security.security.response.JwtResponse;
import com.example.security.security.service.CustomerService;
import com.example.security.security.service.JwtTokenBuilder;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final CustomerService customerService;

    private JwtTokenBuilder jwtTokenBuilder;

    @GetMapping("/getAll")
    public ResponseEntity<List<Customer>> getAllAccounts() {
        return  ResponseEntity.ok(customerService.getCustomers());
    }
    @GetMapping("/register")
    public ResponseEntity<String> register(@RequestBody CustomerDTO customerDTO) {
        customerService.saveCustomer(customerDTO);
        /**  JwtResponse response = new JwtResponse();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customerDTO.getUsername(), customerDTO.getPassword());
        Authentication auth = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenBuilder.generateToken(customerDTO.getUsername());
        response.setMessage("User registered succesfully");
        response.setAccessToken(jwtToken);
        response.setUsername(customerDTO.getUsername()); */
        return new ResponseEntity<>("ok", HttpStatus.CREATED);

    }
    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("You have logged in succesffully");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/deneme")
    public ResponseEntity<String> deneme() {
        return ResponseEntity.ok("merhaba");
    }
}
