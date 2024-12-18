package com.ofg.auth.service.concretes;

import com.ofg.auth.exception.general.NotFoundException;
import com.ofg.auth.exception.general.UniqueConstraintViolationException;
import com.ofg.auth.model.entity.Role;
import com.ofg.auth.model.request.RoleCreateRequest;
import com.ofg.auth.model.request.RoleUpdateRequest;
import com.ofg.auth.model.response.RoleResponse;
import com.ofg.auth.repository.RoleRepository;
import com.ofg.auth.service.abstracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<RoleResponse> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable).map(RoleResponse::new);
    }

    @Override
    public RoleResponse getRoleById(UUID roleId) {
        return roleRepository.findById(roleId)
                .map(RoleResponse::new)
                .orElseThrow(() -> new NotFoundException(roleId));
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
    }

    @Override
    public RoleResponse addRole(RoleCreateRequest roleCreateRequest) {
        Role role = new Role();
        role.setName(roleCreateRequest.name());
        try {
            Role savedRole = roleRepository.save(role);
            return new RoleResponse(savedRole);
        } catch (DataIntegrityViolationException ex) {
            throw new UniqueConstraintViolationException();
        }
    }

    @Override
    public RoleResponse updateRole(UUID roleId, RoleUpdateRequest roleUpdateRequest) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(roleId));

        role.setName(roleUpdateRequest.name());
        try {
            Role updatedRole = roleRepository.save(role);
            return new RoleResponse(updatedRole);
        } catch (DataIntegrityViolationException ex) {
            throw new UniqueConstraintViolationException();
        }
    }

    @Override
    public void deleteRole(UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(roleId));
        roleRepository.delete(role);
    }
}
