package com.ofg.auth.service.concretes;

import com.ofg.auth.exception.general.NotFoundException;
import com.ofg.auth.exception.general.UniqueConstraintViolationException;
import com.ofg.auth.model.entity.Role;
import com.ofg.auth.model.entity.User;
import com.ofg.auth.model.request.AssignRoleRequest;
import com.ofg.auth.service.abstracts.RoleService;
import com.ofg.auth.service.abstracts.UserRoleService;
import com.ofg.auth.service.abstracts.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final RoleService roleService;
    private final UserService userService;

    public UserRoleServiceImpl(RoleService roleService,
                               UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void assignRoleToUser(AssignRoleRequest assignRoleRequest) {
        Role role = roleService.getRoleByName(assignRoleRequest.roleName());
        User user = userService.getUserEntityById(assignRoleRequest.userId());
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userService.updateUserRoles(user.getId(), user);
        } else {
            throw new UniqueConstraintViolationException();
        }
    }

    @Override
    public void revokeRoleFromUser(UUID userId, String roleName) {
        Role role = roleService.getRoleByName(roleName);
        User user = userService.getUserEntityById(userId);
        if (user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            userService.updateUserRoles(user.getId(), user);
        } else {
            throw new NotFoundException("Role " + roleName + " not assigned to user.");
        }
    }
}