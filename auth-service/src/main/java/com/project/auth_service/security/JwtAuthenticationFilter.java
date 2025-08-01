package com.project.auth_service.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.auth_service.service.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@EnableWebSecurity
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Se não tiver Header ou não começar com "Bearer ", passa adiante.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraindo o token
        final String jwt = authHeader.substring(7);

        // Extraindo o email (subject) do token
        final String userEmail = jwtService.extractUsername(jwt);

        // Se já estiver autenticado, não faz nada.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carregando usuário do banco
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Verificando se o token é válido
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Cria o token de autenticação para o Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Define o usuário autenticado no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua a execução da requisição
        filterChain.doFilter(request, response);
    }
}
