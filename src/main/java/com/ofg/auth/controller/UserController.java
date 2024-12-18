package com.ofg.auth.controller;

import com.ofg.auth.core.util.results.ApiDataResponse;
import com.ofg.auth.core.util.results.ApiResponse;
import com.ofg.auth.model.request.*;
import com.ofg.auth.model.response.UserResponse;
import com.ofg.auth.model.response.UserWithoutRolesResponse;
import com.ofg.auth.service.abstracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ofg.auth.core.util.response.ResponseUtil;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private static final String USERS_FETCH_SUCCESS = "app.msg.users.fetch.success";
    private static final String USER_FETCH_SUCCESS = "app.msg.user.fetch.success";
    private static final String USER_CREATE_SUCCESS = "app.msg.user.create.success";
    private static final String USER_UPDATE_SUCCESS = "app.msg.user.update.success";
    private static final String USER_DELETE_SUCCESS = "app.msg.user.delete.success";
    private static final String PASSWORD_UPDATE_SUCCESS = "app.msg.password.update.success";
    private static final String PASSWORD_RESET_LINK_SENT = "app.msg.password.reset.link.sent";
    private static final String PASSWORD_SET_SUCCESS = "app.msg.password.set.success";
    private static final String USER_ACTIVATE_SUCCESS = "app.msg.user.activate.success";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiDataResponse<Page<UserWithoutRolesResponse>>> getAllUsers(Pageable pageable) {
        Page<UserWithoutRolesResponse> users = userService.getAllUsers(pageable);
        return ResponseUtil.createApiDataResponse(users, USERS_FETCH_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiDataResponse<UserResponse>> getUserById(@PathVariable UUID userId) {
        UserResponse userResponse = userService.getUserResponseById(userId);
        return ResponseUtil.createApiDataResponse(userResponse, USER_FETCH_SUCCESS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiDataResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        UserResponse userResponse = userService.addUser(userCreateRequest);
        return ResponseUtil.createApiDataResponse(userResponse, USER_CREATE_SUCCESS, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<ApiDataResponse<UserResponse>> updateUser(
            @PathVariable UUID userId,
            @Valid @ModelAttribute UserUpdateRequest userUpdateRequest) {
        UserResponse userResponse = userService.updateUser(userId, userUpdateRequest);
        return ResponseUtil.createApiDataResponse(userResponse, USER_UPDATE_SUCCESS, HttpStatus.OK);
    }

    @PutMapping("/activate/{token}")
    public ResponseEntity<ApiDataResponse<UserResponse>> activateUser(@PathVariable String token) {
        UserResponse userResponse = userService.activateUser(token);
        return ResponseUtil.createApiDataResponse(userResponse, USER_ACTIVATE_SUCCESS, HttpStatus.OK);
    }

    @PutMapping("/update-password/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<ApiResponse> updatePassword(
            @Valid @PathVariable UUID userId,
            @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {
        userService.updatePassword(userId, userPasswordUpdateRequest);
        return ResponseUtil.createApiResponse(PASSWORD_UPDATE_SUCCESS, HttpStatus.OK);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody UserPasswordResetRequest userPasswordResetRequest) {
        userService.resetPassword(userPasswordResetRequest);
        return ResponseUtil.createApiResponse(PASSWORD_RESET_LINK_SENT, HttpStatus.OK);
    }

    @PutMapping("/reset-password/verify/{token}")
    public ResponseEntity<ApiResponse> setPassword(
            @Valid @PathVariable String token,
            @RequestBody UserPasswordSetRequest userPasswordSetRequest) {
        userService.setPassword(token, userPasswordSetRequest);
        return ResponseUtil.createApiResponse(PASSWORD_SET_SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseUtil.createApiResponse(USER_DELETE_SUCCESS, HttpStatus.NO_CONTENT);
    }
}
