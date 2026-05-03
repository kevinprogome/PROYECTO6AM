/*
 * Proyecto: GreenHouse Manager
 * Archivo: CustomOAuth2UserService.java
 * Descripcion: Servicio OAuth2 para crear o actualizar usuarios.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.security;

import com.greenhouse.manager.domain.entity.Usuario;
import com.greenhouse.manager.domain.enums.AuthProvider;
import com.greenhouse.manager.domain.enums.UserRole;
import com.greenhouse.manager.domain.repository.UsuarioRepository;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Custom OAuth2 user service for Google login.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Creates a new CustomOAuth2UserService.
     *
     * @param usuarioRepository user repository
     */
    public CustomOAuth2UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Loads an OAuth2 user and syncs it with local persistence.
     *
     * @param userRequest oauth2 request
     * @return oauth2 user
     * @throws OAuth2AuthenticationException when user info is invalid
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.getOrDefault("name", email);
        String providerId = (String) attributes.get("sub");

        if (email == null) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error("invalid_oauth2"),
                "error.oauth2.email_no_encontrado"
            );
        }

        String safeName = name == null ? email : name;
        String safeProviderId = providerId == null ? email : providerId;

        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseGet(() -> createUsuario(email, safeName, safeProviderId));

        if (!email.equals(usuario.getEmail()) || !safeName.equals(usuario.getNombre())
            || !safeProviderId.equals(usuario.getProviderId())) {
            usuario.setNombre(safeName);
            usuario.setProvider(AuthProvider.GOOGLE);
            usuario.setProviderId(safeProviderId);
            usuario.setActivo(true);
            usuarioRepository.save(usuario);
        }

        String userNameAttribute = userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())),
            attributes,
            userNameAttribute
        );
    }

    private Usuario createUsuario(String email, String name, String providerId) {
        Usuario usuario = new Usuario();
        usuario.setNombre(name == null ? email : name);
        usuario.setEmail(email);
        usuario.setRol(UserRole.OPERADOR);
        usuario.setProvider(AuthProvider.GOOGLE);
        usuario.setProviderId(providerId == null ? email : providerId);
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }
}
