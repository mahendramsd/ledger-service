package com.msd.ledgerservice.controller;

import com.msd.ledgerservice.dto.request.LedgerServiceLoginRequest;
import com.msd.ledgerservice.dto.response.LedgerServiceLoginResponse;
import com.msd.ledgerservice.exception.CustomException;
import com.msd.ledgerservice.service.UserService;
import com.msd.ledgerservice.util.CustomErrorCodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Api(value = "Auth Controller")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    @ApiOperation(value = "Login endpoint for ledger service", response = LedgerServiceLoginResponse.class)
    public ResponseEntity<LedgerServiceLoginResponse> authenticateUser(@RequestBody LedgerServiceLoginRequest ledgerServiceLoginRequest) {
        if (logger.isDebugEnabled()) {
            logger.info("Login endpoint called");
        }
        authenticate(ledgerServiceLoginRequest.getUsername(), ledgerServiceLoginRequest.getPassword());
        return ResponseEntity.ok(userService.loginUser(ledgerServiceLoginRequest));
    }


    /**
     * Ledger User Authenticate
     * @param username
     * @param password
     */
    private void authenticate(String username, String password) {
        if (logger.isDebugEnabled()) {
            logger.info("Authenticate User {} {}", username , password);
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new CustomException(CustomErrorCodes.USER_DISABLED);
        } catch (BadCredentialsException e) {
            throw new CustomException(CustomErrorCodes.INVALID_CREDENTIALS);

        }
    }
}
