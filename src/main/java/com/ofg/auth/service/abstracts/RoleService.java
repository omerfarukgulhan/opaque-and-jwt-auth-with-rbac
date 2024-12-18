package com.ofg.auth.service.abstracts;

import com.ofg.auth.model.entity.Role;
import com.ofg.auth.model.request.RoleCreateRequest;
import com.ofg.auth.model.request.RoleUpdateRequest;
import com.ofg.auth.model.response.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RoleService {
    Page<RoleResponse> getAllRoles(Pageable pageable);

    RoleResponse getRoleById(UUID roleId);

    Role getRoleByName(String name);

    RoleResponse addRole(RoleCreateRequest roleCreateRequest);

    RoleResponse updateRole(UUID roleId, RoleUpdateRequest roleUpdateRequest);

    void deleteRole(UUID roleId);
}

