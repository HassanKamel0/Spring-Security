package com.example.spring_security.config;

import com.example.spring_security.entity.Customer;
import com.example.spring_security.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EazyBankUserDetailsService implements UserDetailsService {
    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer=customerRepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User detials are not found for the user: "+username));
        List<GrantedAuthority> authorities=List.of(new SimpleGrantedAuthority(customer.getRole()));
        return new User(customer.getEmail(),customer.getPwd(),authorities);
    }

    public void saveUser(Customer customer) {
        String password=passwordEncoder.encode(customer.getPwd());
        customer.setPwd(password);
        customerRepo.save(customer);
    }
}
