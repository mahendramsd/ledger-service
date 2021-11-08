package com.msd.ledgerservice.service;

import com.msd.ledgerservice.config.SecurityConfig;
import com.msd.ledgerservice.domain.CustomUserDetail;
import com.msd.ledgerservice.domain.User;
import com.msd.ledgerservice.dto.request.LedgerServiceLoginRequest;
import com.msd.ledgerservice.dto.response.LedgerServiceLoginResponse;
import com.msd.ledgerservice.exception.CustomException;
import com.msd.ledgerservice.repositories.UserRepository;
import com.msd.ledgerservice.util.CustomErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;

    public UserService(UserRepository userRepository, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
    }


    /**
     * Find User by username for security config
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    /**
     * Find CustomUserDetail bu username
     * @param username
     * @return
     */
    public CustomUserDetail findByCustomUserDetail(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new CustomUserDetail(user);
    }

    /**
     * Login User & create Token
     * @param ledgerServiceLoginRequest
     * @return
     */
    public LedgerServiceLoginResponse loginUser(LedgerServiceLoginRequest ledgerServiceLoginRequest) {
        User user =  userRepository.findByUsername(ledgerServiceLoginRequest.getUsername()).orElseThrow(() ->
                new CustomException(CustomErrorCodes.USER_NOT_FOUND));
        LedgerServiceLoginResponse ledgerServiceLoginResponse =  new LedgerServiceLoginResponse();
        ledgerServiceLoginResponse.setUserId(user.getId());
        ledgerServiceLoginResponse.setAccessToken(securityConfig.generateToken(user.getUsername()));
        return ledgerServiceLoginResponse;
    }

    public void createTestUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if(!user.isPresent()) {
            User user1 = new User();
            user1.setUsername(username);
            user1.setPassword(passwordEncoder.encode(password));
            user1.setAccessToken(securityConfig.generateToken(username));
            userRepository.save(user1);
        }
    }
}
