package com.ofg.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleId implements Serializable {
    private Long userId;
    private Long roleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleId userRoleId)) return false;

        return userId.equals(userRoleId.userId) && roleId.equals(userRoleId.roleId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode() + roleId.hashCode();
    }
}
