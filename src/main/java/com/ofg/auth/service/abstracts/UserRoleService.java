package com.ofg.auth.service.abstracts;

import com.ofg.auth.model.request.AssignRoleRequest;

import java.util.UUID;

public interface UserRoleService {
    void assignRoleToUser(AssignRoleRequest assignRoleRequest);

    void revokeRoleFromUser(UUID userId, String roleName);
}
