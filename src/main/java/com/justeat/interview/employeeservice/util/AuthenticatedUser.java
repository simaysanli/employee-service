package com.justeat.interview.employeeservice.util;


import com.justeat.interview.employeeservice.entity.User;
import com.justeat.interview.employeeservice.repository.UserRepository;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class AuthenticatedUser {

    private final UserRepository userRepository;

    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = "";

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByEmail(username).orElse(null);
    }

}
