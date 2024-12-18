package com.ofg.auth.service.abstracts;

import com.ofg.auth.model.entity.User;
import com.ofg.auth.model.request.*;
import com.ofg.auth.model.response.UserResponse;
import com.ofg.auth.model.response.UserWithoutRolesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    Page<UserWithoutRolesResponse> getAllUsers(Pageable pageable);

    UserResponse getUserResponseById(UUID userId);

    User getUserEntityById(UUID userId);

    User getUserByEmail(String email);

    UserResponse addUser(UserCreateRequest userCreateRequest);

    UserResponse updateUser(UUID userId, UserUpdateRequest userUpdateRequest);

    UserResponse activateUser(String token);

    void updateUserRoles(UUID id, User user);

    void updatePassword(UUID userId, UserPasswordUpdateRequest userPasswordUpdateRequest);

    void resetPassword(UserPasswordResetRequest userPasswordResetRequest);

    void setPassword(String token, UserPasswordSetRequest userPasswordSetRequest);

    void deleteUser(UUID userId);
}