package com.example.ricettario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ricettario.entities.Permission;
import com.example.ricettario.repositories.IPermissionRepository;
import com.example.ricettario.utilities.PermissionType;

@Service
public class PermissionService {

    private final IPermissionRepository permissionRepository;

    public PermissionService(IPermissionRepository permissionRepository) {

        this.permissionRepository = permissionRepository;

    }

    public List<Permission> findAll() {

        return permissionRepository.findAll();

    }

    public Permission findByType(PermissionType permissionType) {

        return permissionRepository.findByPermissionType(permissionType).orElseThrow();

    }

}
