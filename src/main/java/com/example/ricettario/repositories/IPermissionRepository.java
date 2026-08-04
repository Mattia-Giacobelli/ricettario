package com.example.ricettario.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.Permission;
import com.example.ricettario.utilities.PermissionType;

public interface IPermissionRepository extends JpaRepository<Permission, Integer> {

    public Optional<Permission> findByPermissionType(PermissionType permissionType);

}
