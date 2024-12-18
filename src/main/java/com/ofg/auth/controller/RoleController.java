package com.ofg.auth.controller;

import com.ofg.auth.core.util.response.ResponseUtil;
import com.ofg.auth.core.util.results.ApiDataResponse;
import com.ofg.auth.core.util.results.ApiResponse;
import com.ofg.auth.model.request.RoleCreateRequest;
import com.ofg.auth.model.request.RoleUpdateRequest;
import com.ofg.auth.model.response.RoleResponse;
import com.ofg.auth.service.abstracts.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    private static final String ROLES_FETCH_SUCCESS = "app.msg.roles.fetch.success";
    private static final String ROLE_FETCH_SUCCESS = "app.msg.role.fetch.success";
    private static final String ROLE_CREATE_SUCCESS = "app.msg.role.create.success";
    private static final String ROLE_UPDATE_SUCCESS = "app.msg.role.update.success";
    private static final String ROLE_DELETE_SUCCESS = "app.msg.role.delete.success";

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<ApiDataResponse<Page<RoleResponse>>> getAllRoles(Pageable pageable) {
        Page<RoleResponse> roles = roleService.getAllRoles(pageable);
        return ResponseUtil.createApiDataResponse(roles, ROLES_FETCH_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<ApiDataResponse<RoleResponse>> getRoleById(@PathVariable UUID roleId) {
        RoleResponse roleResponse = roleService.getRoleById(roleId);
        return ResponseUtil.createApiDataResponse(roleResponse, ROLE_FETCH_SUCCESS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiDataResponse<RoleResponse>> createRole(@Valid @RequestBody RoleCreateRequest roleCreateRequest) {
        RoleResponse roleResponse = roleService.addRole(roleCreateRequest);
        return ResponseUtil.createApiDataResponse(roleResponse, ROLE_CREATE_SUCCESS, HttpStatus.CREATED);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<ApiDataResponse<RoleResponse>> updateRole(
            @PathVariable UUID roleId,
            @Valid @RequestBody RoleUpdateRequest roleUpdateRequest) {
        RoleResponse roleResponse = roleService.updateRole(roleId, roleUpdateRequest);
        return ResponseUtil.createApiDataResponse(roleResponse, ROLE_UPDATE_SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable UUID roleId) {
        roleService.deleteRole(roleId);
        return ResponseUtil.createApiResponse(ROLE_DELETE_SUCCESS, HttpStatus.NO_CONTENT);
    }
}
