package com.Mbakara.Banking_System.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;


    private UserDetailsService userDetailsService; // Ask where this class is coming from


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Checking details of a particular user who is sending request
        String token = getTokenFromRequest(request);
        /**  The getTokeFromRequest method is saying, every request sent by a user, should filter the jwtToken sent with it
         Meaning we are extracting the token from HttpRequest(Getting), checking if the Token is not null
         */
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){ // check if the toke is valid.
            String username = jwtTokenProvider.getUsername(token); /** Getting the username from the
             Token using the method created earlier (JwtTokenProvider).**/
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Loading the username from the db

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()/** So from here the token was generated
             which will contain the username, password. Then getting authorities from the host
             assign to the user.
             **/
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    /**  So here every single request will carry a Bearer with the Token.
     */
    //private String getTokenFromRequest(HttpServletRequest request){
//        String bearerToken = request.getHeader("Authorization");
//
//        // Check if the text is empty
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
//            return bearerToken.substring(7 );
//        }
//
//
//        return null;
//    }


        private String getTokenFromRequest(HttpServletRequest request){

        String bearerToken = request.getHeader("Authorization");

        // Check if the Authorization header is missing or does not start with "Bearer"
        if(bearerToken == null || !bearerToken.startsWith("Bearer ")){
            return  null; // This returns null if no valid token is found.
        }

        // Extract the actual Token (remove "Bearer " prefix)
        String token = bearerToken.substring(7).trim();
        //Ensure the extracted token is not empty
        return token.isEmpty() ? null : token;
    }

}
