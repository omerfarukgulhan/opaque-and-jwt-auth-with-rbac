package com.ofg.auth.model.entity;

import com.ofg.auth.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEntity {
    @Column(length = 100, nullable = false, unique = true)
    @Size(min = 3, max = 100)
    private String name;
}