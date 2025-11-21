package com.senai.projeto.mytraining.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Tag(name = "JwtUtil", description = "Utilitário para operações com JWT (geração, validação, extração)")
@Schema(description = "Gerencia tokens JWT para autenticação - Expiração: 1 hora")
public class JwtUtil {

    private final SecretKey key = Jwts.SIG.HS256.key().build();

    @Operation(summary = "Gerar token JWT", description = "Cria um novo token JWT contendo username e roles do usuário")
    @Schema(description = "Token gerado contém subject (email), roles, data de emissão e expiração (1 hora)")
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }

    @Operation(summary = "Validar token JWT", description = "Verifica se o token é válido e não está expirado")
    @Schema(description = "Retorna true se token é válido para o usuário, false caso contrário")
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    @Operation(summary = "Extrair username do token", description = "Decodifica o token e retorna o subject (email)")
    @Schema(description = "Extrai o email/username contido no claims do token JWT")
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    @Operation(summary = "Extrair todos os claims", description = "Decodifica e valida o token, retornando todos os claims")
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Operation(summary = "Verificar expiração do token", description = "Compara data de expiração com data atual")
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
