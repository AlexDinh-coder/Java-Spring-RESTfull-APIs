package vn.tuantrung.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.tuantrung.jobhunter.domain.Permission;
import vn.tuantrung.jobhunter.domain.response.ResultPaginationDTO;
import vn.tuantrung.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExist(Permission p) {
        return permissionRepository.existsByModuleAndApiPathAndMethod(
                p.getModule(),
                p.getApiPath(),
                p.getMethod());
    }

    public Permission createPermission(Permission permission) {
        return this.permissionRepository.save(permission);
    }

    public Permission fetchById(Long id) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        if (permissionOptional.isPresent())
            return permissionOptional.get();

        return null;
    }

    public Permission updatePermission(Permission p) {
        Permission permissionDB = this.fetchById(p.getId());
        if (permissionDB != null) {
            permissionDB.setName(p.getName());
            permissionDB.setModule(p.getModule());
            permissionDB.setApiPath(p.getApiPath());
            permissionDB.setMethod(p.getMethod());

            // update
            permissionDB = this.permissionRepository.save(permissionDB);
            return permissionDB;
        }
        return null;
    }

    public void deletePermissionById(long id) {
        //delete permission_role
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        //delete permission
        this.permissionRepository.delete(currentPermission);
    }

    public ResultPaginationDTO getPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> permissionPage = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(permissionPage.getTotalPages());
        mt.setTotal(permissionPage.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(permissionPage.getContent());

        return rs;
    }

}
