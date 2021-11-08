package com.msd.ledgerservice.filter;

import com.google.common.base.Optional;
import com.msd.ledgerservice.config.SecurityConfig;
import com.msd.ledgerservice.domain.CustomUserDetail;
import com.msd.ledgerservice.domain.User;
import com.msd.ledgerservice.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestHeaderFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestHeaderFilter.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityConfig jwtTokenConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String username = null;
        String jwtToken = null;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Optional<String> hashToken =
                Optional.fromNullable(httpRequest.getHeader("bearer-access-token"));

        /**
         * Remove Bearer word and get only the Token
         */
        if (hashToken.isPresent() && hashToken.get().startsWith("Bearer ")) {
            jwtToken = hashToken.get().substring(7);
            try {
                username = jwtTokenConfig.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
            } catch (SignatureException e) {
                logger.error("JWT Token has Invalid");
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            CustomUserDetail userDetails = this.userService.findByCustomUserDetail(username);

            // if token is valid configure Spring Security to manually set authentication
            if (jwtTokenConfig.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    httpRequest.setAttribute("user-id", userDetails.getUser().getId());
                    httpRequest.setAttribute("bearer-access-token", jwtToken);
            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "JWT token replaced or expired.");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {

    }
}
