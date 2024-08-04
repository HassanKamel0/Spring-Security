package com.example.spring_security.repo;

import com.example.spring_security.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends CrudRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
}
