/*
 * Proyecto: GreenHouse Manager
 * Archivo: UsuarioService.java
 * Descripcion: Servicio de negocio para usuarios.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.service;

import com.greenhouse.manager.api.dto.request.UsuarioRequest;
import com.greenhouse.manager.api.dto.response.UsuarioResponse;
import com.greenhouse.manager.api.exception.NotFoundException;
import com.greenhouse.manager.domain.entity.Usuario;
import com.greenhouse.manager.domain.repository.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing users.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Creates a new UsuarioService instance.
     *
     * @param usuarioRepository repository for usuarios
     */
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Creates a new user.
     *
     * @param request user request
     * @return created user
     */
    @Transactional
    public UsuarioResponse createUsuario(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        applyRequest(usuario, request);
        Usuario saved = usuarioRepository.save(usuario);
        return toResponse(saved);
    }

    /**
     * Updates an existing user.
     *
     * @param id user id
     * @param request user request
     * @return updated user
     */
    @Transactional
    public UsuarioResponse updateUsuario(Long id, UsuarioRequest request) {
        Usuario usuario = getUsuarioEntity(id);
        applyRequest(usuario, request);
        Usuario saved = usuarioRepository.save(usuario);
        return toResponse(saved);
    }

    /**
     * Retrieves a user by id.
     *
     * @param id user id
     * @return user response
     */
    @Transactional(readOnly = true)
    public UsuarioResponse getUsuarioById(Long id) {
        return toResponse(getUsuarioEntity(id));
    }

    /**
     * Retrieves all users.
     *
     * @return list of users
     */
    @Transactional(readOnly = true)
    public List<UsuarioResponse> getAllUsuarios() {
        return usuarioRepository.findAll()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Deletes a user by id.
     *
     * @param id user id
     */
    @Transactional
    public void deleteUsuario(Long id) {
        Usuario usuario = getUsuarioEntity(id);
        usuarioRepository.delete(usuario);
    }

    private Usuario getUsuarioEntity(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.usuario.no_encontrado"));
    }

    private void applyRequest(Usuario usuario, UsuarioRequest request) {
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setRol(request.getRol());
        usuario.setProvider(request.getProvider());
        usuario.setProviderId(request.getProviderId());
        usuario.setActivo(request.getActivo());
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setEmail(usuario.getEmail());
        response.setRol(usuario.getRol());
        response.setProvider(usuario.getProvider());
        response.setProviderId(usuario.getProviderId());
        response.setActivo(usuario.getActivo());
        response.setCreatedAt(usuario.getCreatedAt());
        response.setUpdatedAt(usuario.getUpdatedAt());
        return response;
    }
}
