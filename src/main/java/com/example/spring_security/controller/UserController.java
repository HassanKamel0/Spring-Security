package com.example.spring_security.controller;

import com.example.spring_security.config.EazyBankUserDetailsService;
import com.example.spring_security.entity.Customer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final EazyBankUserDetailsService eazyBankUserDetailsService;
    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody Customer customer) {
        eazyBankUserDetailsService.saveUser(customer);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
